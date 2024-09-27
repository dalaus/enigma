import kotlin.math.abs

@JvmInline
value class EnigmaChar(val char: UInt)

class Rotor(private val mapping: Map<EnigmaChar, EnigmaChar>) {
    fun rightInput(char: EnigmaChar): EnigmaChar = mapping[char]!!

    private val swapped = mapping.swapKeysAndValues()
    fun leftInput(char: EnigmaChar): EnigmaChar = swapped[char]!!
}

fun wheel(input: EnigmaChar, mapping: (EnigmaChar) -> EnigmaChar, charsCount: UInt, offset: UInt): EnigmaChar =
    EnigmaChar((mapping(EnigmaChar((input.char + offset) % charsCount)).char + charsCount - offset) % charsCount)

fun createRotors(
    allRotors: List<Map<EnigmaChar, EnigmaChar>>,
    positions: List<UInt>,
): (EnigmaChar) -> EnigmaChar {
    require(allRotors.isNotEmpty())
    require(positions.size == allRotors.size)

    val charsCount = allRotors.first().size.toUInt()
    require(charsCount % 2u == 0u)

    val offsets = positions.toTypedArray()

    val lastRotor = Rotor(allRotors.first())
    val rotors = allRotors.drop(1).map { Rotor(it) }
    // 1 2 3 l 3 2 1
    // где l - самый левый ротор
    val chain = rotors.map { it::rightInput } + lastRotor::rightInput + rotors.map { it::leftInput }

    fun shift(index: Int = 0) {
        if (index <= positions.lastIndex) {
            offsets[index] = (offsets[index] + 1u) % charsCount
            if (offsets[index] == 0u) shift(index + 1)
        }
    }

    return { input ->
        val result = chain.foldIndexed(input) { index, char, rotor ->
            val cycleIndex = cycleIndex(chain.size, index)
            wheel(char, rotor, charsCount, offsets[cycleIndex])
        }
        shift()
        result
    }
}

// например
// size 5
// index  0 1 2 3 4
// output 0 1 2 1 0
private fun cycleIndex(size: Int, index: Int) = -abs(-index + (size / 2)) + size / 2
