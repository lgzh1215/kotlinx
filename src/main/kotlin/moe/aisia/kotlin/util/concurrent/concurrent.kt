@file:Suppress("NOTHING_TO_INLINE")

package moe.aisia.kotlin.util.concurrent

import moe.aisia.kotlin.util.alias.JObject

inline fun Any.wait(timeout: Long = 0) = (this as JObject).wait(timeout)
inline fun Any.wait(timeout: Long, nanos: Int) = (this as JObject).wait(timeout, nanos)
inline fun Any.notify() = (this as JObject).notify()
inline fun Any.notifyAll() = (this as JObject).notifyAll()