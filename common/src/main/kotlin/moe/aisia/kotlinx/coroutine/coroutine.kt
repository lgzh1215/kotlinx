package moe.aisia.kotlinx.coroutine

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext

object EmptyContinuation : Continuation<Any?> {
   override val context: CoroutineContext get() = EmptyCoroutineContext
   override fun resume(value: Any?) {}
   override fun resumeWithException(exception: Throwable): Unit = throw exception
}

inline fun <T> continuation(context: CoroutineContext = EmptyCoroutineContext,
                            crossinline resumeWithException: (Throwable) -> Unit = { throw it },
                            crossinline resume: (value: T) -> Unit = {}
): Continuation<T> = object : Continuation<T> {
   override val context: CoroutineContext get() = context
   override fun resume(value: T) = value.run(resume)
   override fun resumeWithException(exception: Throwable) = exception.run(resumeWithException)
}
