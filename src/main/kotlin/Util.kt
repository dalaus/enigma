fun Map<Char, Char>.toEnigmaChars(charsMap: Map<Char, EnigmaChar>): Map<EnigmaChar, EnigmaChar> = buildMap {
    this@toEnigmaChars.forEach { (key, value) ->
        put(charsMap[key]!!, charsMap[value]!!)
    }
}

fun <K, V> Map<K, V>.swapKeysAndValues(): Map<V, K> = buildMap {
    this@swapKeysAndValues.forEach { (key, value) ->
        put(value, key)
    }
}
