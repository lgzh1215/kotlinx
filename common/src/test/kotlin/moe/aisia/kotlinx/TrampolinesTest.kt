package moe.aisia.kotlinx

import io.kotlintest.matchers.shouldBe
import moe.aisia.kotlinx.util.Trampolines
import moe.aisia.kotlinx.util.Trampolines.Companion.done
import moe.aisia.kotlinx.util.Trampolines.Companion.tail
import moe.aisia.kotlinx.util.Trampolines.Companion.tailRec
import org.junit.Test

class TrampolinesTest {

   @Test
   fun testEvenOrOdd() {
      lateinit var isEven: Int.() -> Trampolines<Boolean>
      lateinit var isOdd: Int.() -> Trampolines<Boolean>

      isEven = {
         if (this == 0) done { true }
         else tail { (this - 1).isOdd() }
      }
      isOdd = {
         if (this == 0) done { false }
         else tail { (this - 1).isEven() }
      }

      (0..1000).forEach {
         it.isEven().result shouldBe (it and 1 == 0)
      }
   }

   @Test
   fun testFibonacci() {
      fun fib(n: Long): Long = run {
         if (n < 3) 1
         else fib(n - 1) + fib(n - 2)
      }

      fun fibTest1(n: Long): Trampolines<Long> = run {
         if (n < 3) done(1)
         else tail { fibTest1(n - 2) }.flatMap { x ->
            tail { fibTest1(n - 1) }.flatMap { y ->
               done(x + y)
            }
         }
      }

      fun fibTest2(n: Long): Trampolines<Long> = if (n < 2) done(n) else {
         Trampolines.tailRec {
            val x = bind(tail { fibTest2(n - 1) })
            val y = bind(tail { fibTest2(n - 2) })
            yield(x + y)
         }
      }

      (1L..20L).let {
         it.map { fibTest1(it).result } shouldBe it.map { fib(it) }
         it.map { fibTest2(it).result } shouldBe it.map { fib(it) }
      }
   }

   @Test
   fun testFactorial() {
      fun fac(n: Long): Long = run {
         if (n < 1) 1
         else n * fac(n - 1)
      }

      fun facTest1(n: Long): Trampolines<Long> = run {
         if (n < 1) done(1)
         else tail { facTest1(n - 1) }.flatMap { done(it * n) }
      }

      fun facTest2(n: Long): Trampolines<Long> = if (n < 1) done(1) else {
         tailRec {
            val l = bind(tail { facTest2(n - 1) })
            yield(n * l)
         }
      }

      (1L..30L).let {
         it.map { facTest1(it).result } shouldBe it.map { fac(it) }
         it.map { facTest2(it).result } shouldBe it.map { fac(it) }
      }
   }
}