@file:JvmName("Utils")

package moe.aisia.kotlin.util

inline fun <T> undefined(): T = throw NotImplementedError()

inline fun <T> nullOf(): T? = null

inline fun <reified T> Any?.isInstanceOf() = this is T
inline fun <reified T> Any?.asInstanceOf() = this as T
inline fun <reified T> Any?.cast(): T = asInstanceOf()

inline fun <T> T.println(): T = printlnBy { it }
inline fun <T, U> T.printlnBy(converter: (T) -> U): T = this.also { println(converter(it)) }

fun <T> repeat(t: T): Sequence<T> = Sequence {
    object : Iterator<T> {
        override fun hasNext(): Boolean = true
        override fun next(): T = t
    }
}

fun <T> replicate(i: Int, t: T): Sequence<T> = repeat(t).take(i)

inline fun <T> Iterable<T>.breakIf(predicate: (T) -> Boolean): Pair<List<T>, List<T>> {
    val l = mutableListOf<T>()
    val r = mutableListOf<T>()
    var isBreak = false
    for (t in this) {
        if (isBreak) {
            r.add(t)
        } else {
            if (predicate(t)) {
                r.add(t)
                isBreak = true
            } else {
                l.add(t)
            }
        }
    }
    return l to r
}

fun <T> Iterable<T>.breakAt(i: Int): Pair<List<T>, List<T>> =
        foldIndexed(mutableListOf<T>() to @Suppress("RemoveExplicitTypeArguments")
        mutableListOf<T>()) { index, acc, t ->
            acc.apply { if (index < i) first.add(t) else second.add(t) }
        }

fun Boolean.toInt(): Int = if (this) 1 else 0

fun Int.isOdd(): Boolean = this and 1 == 0
fun Int.isEven(): Boolean = this and 1 == 1

fun Long.isOdd(): Boolean = this and 1 == 0L
fun Long.isEven(): Boolean = this and 1 == 1L

