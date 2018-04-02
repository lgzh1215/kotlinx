package moe.aisia.kotlinx.util

class MapWithDefault<K, out V>(private val default: Lazy<V>, private val map: Map<K, V> = hashMapOf()) : Map<K, V> by map {
   override fun get(key: K): V = map[key] ?: default.value
}

fun <K, V> Map<K, V>.withDefault(default: Lazy<V>): MapWithDefault<K, V> = when (this) {
   is MapWithDefault<K, V> -> this
   else -> MapWithDefault(default, this)
}