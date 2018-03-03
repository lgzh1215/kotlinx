package moe.aisia.kotlin.util

/**
 * 一些貌似没用的东西
 */
object Trash {
   /**
    * 恒等函数
    */
   @JvmStatic
   fun <T> identity(t: T): T = t

   /**
    * 常数函数
    */
   @JvmStatic
   fun <P, R> constant(t: R): (P) -> R = fun(_: P) = t

   /**
    * 不定点组合子
    */
   @JvmStatic
   fun <P, R> fix(f: ((P) -> (R)) -> (P) -> R): (P) -> R = fun(it: P): R = f(fix(f))(it)

   /**
    * 不定点组合子（万能版）
    */
   @JvmStatic
   fun <R> fix(f: (() -> (R)) -> () -> R): () -> R = fun(): R = f(fix(f))()
}
