package moe.aisia.kotlin

import io.kotlintest.matchers.shouldBe
import moe.aisia.kotlin.util.Trash
import org.junit.Test

class TrashTest {

   @Test
   fun identityFunction() {
      (1..100).forEach { Trash.identity(it) shouldBe it }
      (1..100).map(Trash::identity) shouldBe (1..100).toList()
   }

   @Test
   fun constantFunction() {
      val constString: (TrashTest) -> String = Trash.constant("poi")
      val constLong: (Trash) -> Long = Trash.constant(1231542341L)
      constString(TrashTest()) shouldBe "poi"
      constLong(Trash) shouldBe 1231542341L

      (1..100).map(Trash.constant("1551")) shouldBe List(100) { "1551" }
   }

   @Test
   fun fixedPointFunction() {
      /**
       * 阶乘
       */
      val fac = Trash.fix { f: () -> (Int) -> Int ->
         fun() = { x: Int ->
            if (x <= 0) 1
            else x * f()(x - 1)
         }
      }()
      (1..10).map(fac) shouldBe listOf(1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800)

      /**
       * 斐波那契
       */
      val fib = Trash.fix { f: (Int) -> Int ->
         { x: Int ->
            if (x <= 2) 1
            else f(x - 1) + f(x - 2)
         }
      }
      (1..10).map(fib) shouldBe listOf(1, 1, 2, 3, 5, 8, 13, 21, 34, 55)
   }
}