package moe.aisia.kotlinx.util

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.EmptyCoroutineContext
import kotlin.coroutines.experimental.RestrictsSuspension
import kotlin.coroutines.experimental.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.experimental.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.experimental.startCoroutine

/**
 * 蹦床，用于尾调用消除（tail call elimination）
 */
sealed class Trampolines<out A> {
   private class Done<out A>(@JvmField val value: A) : Trampolines<A>()
   private class Call<out A>(@JvmField val rest: () -> Trampolines<A>) : Trampolines<A>()
   private class Cont<A, out B>(@JvmField val a: Trampolines<A>,
                                @JvmField val f: (A) -> Trampolines<B>) : Trampolines<B>()

   fun <B> map(f: (A) -> B): Trampolines<B> = flatMap { Call { Done(f(it)) } }

   fun <B> flatMap(f: (A) -> Trampolines<B>): Trampolines<B> = when (this) {
      is Done<A> -> Call { f(value) }
      is Call<A> -> Cont(this, f)
      is Cont<*, A> -> {
         this as Cont<Any?, A>?
         Cont(this.a) { this.f(it).flatMap(f) }
      }
   }

   operator fun invoke(): A = result()
   val result: A get() = result()

   companion object {
      @JvmStatic
      fun <T> done(result: T): Trampolines<T> = Done(result)

      @JvmStatic
      inline fun <T> done(result: () -> T): Trampolines<T> = done(result())

      @JvmStatic
      fun <T> tail(rest: () -> Trampolines<T>): Trampolines<T> = Call(rest)

      @JvmStatic
      private tailrec fun <T> Trampolines<T>.result(): T = when (this) {
         is Done<T> -> value
         is Call<T> -> rest().result()
         is Cont<*, T> -> {
            this as Cont<Any?, T>?
            when (a) {
               is Done -> f(a.value).result()
               is Call -> a.rest().flatMap(f).result()
               is Cont<*, Any?> -> {
                  a as Cont<Any?, Any?>?
                  a.a.flatMap { a.f(it).flatMap(f) }.result()
               }
            }
         }
      }

      fun <T> tailRec(f: suspend TailRecBuilder<*>.() -> Trampolines<T>): Trampolines<T> {
         val builder = TailRecBuilder<T>()
         f.startCoroutine(builder, builder)
         return builder.result
      }

      @RestrictsSuspension
      class TailRecBuilder<T> : Continuation<Trampolines<T>> {
         lateinit var result: Trampolines<T>

         fun <T> yield(i: T): Trampolines<T> = done(i)

         suspend fun <U> bind(o: Trampolines<U>): U = suspendCoroutineUninterceptedOrReturn { c ->
            result = o.flatMap { t ->
               c.resume(t)
               result
            }
            COROUTINE_SUSPENDED
         }

         override val context: EmptyCoroutineContext get() = EmptyCoroutineContext
         override fun resume(value: Trampolines<T>): Unit = run { this.result = value }
         override fun resumeWithException(exception: Throwable): Unit = throw exception
      }
   }
}
