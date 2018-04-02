@file:Suppress("NOTHING_TO_INLINE")

package moe.aisia.kotlinx.concurrent

import moe.aisia.kotlinx.alias.JObject

inline fun Any.wait(timeout: Long = 0) = (this as JObject).wait(timeout)
inline fun Any.wait(timeout: Long, nanos: Int) = (this as JObject).wait(timeout, nanos)
inline fun Any.notify() = (this as JObject).notify()
inline fun Any.notifyAll() = (this as JObject).notifyAll()