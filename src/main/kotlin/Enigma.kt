import kotlin.math.abs

typealias Enigma = (Char) -> (Char)

@JvmInline
value class EnigmaChar(val char: Int)

private fun wheel(input: EnigmaChar, mapping: (EnigmaChar) -> EnigmaChar, charsCount: Int, offset: Int): EnigmaChar =
    EnigmaChar((mapping(EnigmaChar((input.char + offset) % charsCount)).char + charsCount - offset) % charsCount)


class RotorsBuilder {
    private var firstRotor: Map<Char, Char>? = null
    fun firstRotor(vararg mapping: Pair<Char, Char>) {
        firstRotor = mapping.toMap().run { this + this.swapKeysAndValues() }
    }

    private val rotors: MutableList<Map<Char, Char>> = mutableListOf()
    fun rotor(vararg mapping: Pair<Char, Char>) {
        rotors += mapping.toMap()
    }

    fun buildChain(): List<Map<Char, Char>> =
        rotors.reversed() + requireNotNull(firstRotor) + rotors.map { it.swapKeysAndValues() }
}

class EnigmaBuilder {
    private var alphabet: String? = null
    infix fun alphabet(alphabet: String) {
        this.alphabet = alphabet
    }

    private var rotorsChain: List<Map<Char, Char>>? = null
    fun rotorsChain(builder: RotorsBuilder.() -> Unit) {
        rotorsChain = RotorsBuilder().apply(builder).buildChain()
    }

    private var keysMapping = emptyMap<Char, Char>()
    fun keysMapping(vararg mapping: Pair<Char, Char>) {
        keysMapping = mapping.toMap().run { this + this.swapKeysAndValues() }
    }

    fun build(): Enigma {
        val alphabet = requireNotNull(alphabet) { "You should set alphabet by alphabet(...)" }
        val rotorsChain = requireNotNull(rotorsChain) { "You should set rotors chain by rotorsChain(...)" }
        val charsMap = alphabet.mapIndexed { index, char -> char to EnigmaChar(index) }.toMap()
        val reversedCharsMap = charsMap.swapKeysAndValues()
        val charsCount = alphabet.length

        val offsets = Array(rotorsChain.size / 2 + 1) { 0 }

        fun shift(index: Int = 0) {
            if (index <= offsets.lastIndex) {
                offsets[index] = (offsets[index] + 1) % charsCount
                if (offsets[index] == 0) shift(index + 1)
            }
        }

        val keysMapping = keysMapping.toEnigmaChars(charsMap)
        val chain = rotorsChain.map { it.toEnigmaChars(charsMap) }.map { it::getValue }

        return { input ->
            val enigmaChar = charsMap[input]!!
            val mapped = keysMapping.getOrElse(enigmaChar) { enigmaChar }

            shift()
            val result = chain.foldIndexed(mapped) { index, char, rotor ->
                val cycleIndex = cycleIndex(chain.size, index)
                wheel(char, rotor, charsCount, offsets[cycleIndex])
            }
            reversedCharsMap[keysMapping.getOrElse(result) { result }]!!
        }
    }
}

fun enigma(builder: EnigmaBuilder.() -> Unit): Enigma = EnigmaBuilder().apply(builder).build()

private fun cycleIndex(size: Int, index: Int) = -abs(-index + (size / 2)) + size / 2
