typealias Enigma = (Char) -> (Char)

fun Map<Char, Char>.toEnigmaChars(charsMap: Map<Char, EnigmaChar>): Map<EnigmaChar, EnigmaChar> = buildMap {
    this@toEnigmaChars.forEach { (key, value) ->
        put(charsMap[key]!!, charsMap[value]!!)
    }
}

class RotorsBuilder(private val charsMap: Map<Char, EnigmaChar>) {
    var rotors: MutableList<Rotor> = mutableListOf()

    fun firstRotor(vararg mapping: Pair<Char, Char>) {
        rotors += Rotor(mapping.toMap().toEnigmaChars(charsMap).run {
            this + this.swapKeysAndValues()
        })
    }

    fun rotor(vararg mapping: Pair<Char, Char>) {
        rotors += Rotor(mapping.toMap().toEnigmaChars(charsMap))
    }
}

class EnigmaBuilder {
    private lateinit var alphabet: String
    infix fun alphabet(alphabet: String) {
        this.alphabet = alphabet
    }

    private val charsMap by lazy {
        alphabet.mapIndexed { index, char -> char to EnigmaChar(index.toUInt()) }.toMap()
    }
    private val reversedCharsMap by lazy { charsMap.swapKeysAndValues() }
    private val charsCount get() = alphabet.length.toUInt()


    private lateinit var chain: List<(EnigmaChar) -> EnigmaChar>
    fun rotorsChain(builder: RotorsBuilder.() -> Unit) {
        val rotorsBuilder = RotorsBuilder(charsMap)

        rotorsBuilder.builder()

        val rotors = rotorsBuilder.rotors.drop(1)
        val lastRotor = rotorsBuilder.rotors.first()

        chain = rotors.map { it::rightInput } + lastRotor::rightInput + rotors.map { it::leftInput }
    }

    fun build(): Enigma {
        val offsets = Array(chain.size / 2 + 1) { 0u }

        fun shift(index: Int = 0) {
            if (index <= offsets.lastIndex) {
                offsets[index] = (offsets[index] + 1u) % charsCount
                if (offsets[index] == 0u) shift(index + 1)
            }
        }

        return { input ->
            val result = chain.foldIndexed(charsMap[input]!!) { index, char, rotor ->
                val cycleIndex = cycleIndex(chain.size, index)
                wheel(char, rotor, charsCount, offsets[cycleIndex])
            }
            shift()
            reversedCharsMap[result]!!
        }
    }
}

fun enigma(builder: EnigmaBuilder.() -> Unit): Enigma = EnigmaBuilder().apply { builder() }.build()

