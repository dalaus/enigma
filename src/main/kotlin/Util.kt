fun <K, V> Map<K, V>.swapKeysAndValues(): Map<V, K> = buildMap {
    this@swapKeysAndValues.forEach { (key, value) ->
        put(value, key)
    }
}
