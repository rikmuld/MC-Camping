package com.rikmuld.camping.utils

object SeqUtils {
  def merge[A, B, C](b: Seq[B])(f: (A, B) => C)(a: Seq[A]): Seq[C] =
    merge(a, b)(f)

  def merge[A, B, C](a: Seq[A], b: Seq[B])(f: (A, B) => C): Seq[C] =
    a zip b map(t => f(t._1, t._2))

  def allOne(all: Int => Boolean)(one: Int => Boolean)(v: Seq[Int]): Boolean =
    (v forall all) && (v exists one)
}