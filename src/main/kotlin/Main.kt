fun main() {
    val charsMap = mapOf(
        'a' to EnigmaChar(0u),
        'b' to EnigmaChar(1u),
        'c' to EnigmaChar(2u),
        'd' to EnigmaChar(3u),
    )
    val reversedCharsMap = charsMap.swapKeysAndValues()

    fun Map<Char, Char>.toEnigmaChars(): Map<EnigmaChar, EnigmaChar> = buildMap {
        this@toEnigmaChars.forEach { (key, value) ->
            put(charsMap[key]!!, charsMap[value]!!)
        }
    }

    val lastRotor = mapOf(
        'a' to 'd',
        'b' to 'c',
        'c' to 'b',
        'd' to 'a',
    ).toEnigmaChars()

    val firstRotor = mapOf(
        'a' to 'd',
        'b' to 'c',
        'd' to 'b',
        'c' to 'a',
    ).toEnigmaChars()

    val enigma = createRotors(listOf(lastRotor, firstRotor), listOf(0u, 0u))

    while (true) {
        val result = readln()
            .asSequence()
            .map { charsMap[it] }
            .map { enigma(it!!) }
            .map { reversedCharsMap[it] }
            .joinToString("")
        println(result)
    }
}
