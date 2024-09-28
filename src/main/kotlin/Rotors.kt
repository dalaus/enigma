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

// например
// size 5
// index  0 1 2 3 4
// output 0 1 2 1 0
fun cycleIndex(size: Int, index: Int) = -abs(-index + (size / 2)) + size / 2
