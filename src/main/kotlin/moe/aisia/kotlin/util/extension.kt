@file:JvmName("Utils")
@file:Suppress("NOTHING_TO_INLINE")

package moe.aisia.kotlin.util

import moe.aisia.kotlin.util.alias.JByte
import moe.aisia.kotlin.util.alias.JInt
import moe.aisia.kotlin.util.alias.JLong

inline fun <T> undefined(): T = throw NotImplementedError()

inline fun <T> nullOf(): T? = null

inline fun <reified T> Any?.isInstanceOf() = this is T
inline fun <reified T> Any?.asInstanceOf() = this as T
inline fun <reified T> Any?.cast(): T = asInstanceOf()

inline fun <T> T.println(): T = printlnBy { it }
inline fun <T, U> T.printlnBy(converter: (T) -> U): T = this.also { println(converter(it)) }

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

inline fun <T> Iterable<T>.breakAt(i: Int): Pair<List<T>, List<T>> =
      foldIndexed(mutableListOf<T>() to mutableListOf<T>()) { index, acc, t ->
         acc.apply { if (index < i) first.add(t) else second.add(t) }
      }

inline fun Boolean.toInt(): Int = if (this) 1 else 0
inline fun Number.toBoolean(): Boolean = toInt() != 0

inline fun Int.isOdd(): Boolean = this and 1 == 0
inline fun Int.isEven(): Boolean = this and 1 == 1
inline fun Int.toString(redix: Int): String = JInt.toString(this, redix)
inline fun Int.toHexString(): String = JInt.toHexString(this)
inline fun Int.toOctalString(): String = JInt.toOctalString(this)
inline fun Int.toBinaryString(): String = JInt.toBinaryString(this)

inline fun Long.isOdd(): Boolean = this and 1 == 0L
inline fun Long.isEven(): Boolean = this and 1 == 1L
inline fun Long.toString(redix: Int): String = JLong.toString(this, redix)
inline fun Long.toHexString(): String = JLong.toHexString(this)
inline fun Long.toOctalString(): String = JLong.toOctalString(this)
inline fun Long.toBinaryString(): String = JLong.toBinaryString(this)

inline fun Byte.toUnsignedInt(): Int = JByte.toUnsignedInt(this)
inline fun Byte.toUnsignedLong(): Long = JByte.toUnsignedLong(this)

private class InfiniteSequence<out T>(private var init: () -> T, private val next: (T) -> T) : Sequence<T> {
   override fun iterator(): Iterator<T> = object : Iterator<T> {
      private var value: T? = null
      override fun hasNext(): Boolean = true
      override fun next(): T = (value?.let(next) ?: init()).also { value = it }
   }
}

fun <T> generateSequenceInfinite(next: T): Sequence<T> =
      InfiniteSequence({ next }) { next }

fun <T> generateSequenceInfinite(next: () -> T): Sequence<T> =
      InfiniteSequence(next) { next() }

fun <T> generateSequenceInfinite(seed: T, next: (T) -> T): Sequence<T> =
      InfiniteSequence({ seed }, next)

fun <T> generateSequenceInfinite(seed: () -> T, next: (T) -> T): Sequence<T> =
      InfiniteSequence(seed, next)

fun <T> replicate(i: Int, t: T): Sequence<T> = generateSequenceInfinite(t).take(i)
