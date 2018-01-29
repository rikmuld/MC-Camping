package com.rikmuld.camping.misc

object ModRegister {
  final val PRE = 0
  final val PERI = 1
  final val POST = 2
}

trait ModRegister {
  var phase = ModRegister.PRE
  
  def register {}
  def registerClient {}
  def registerServer {}
}