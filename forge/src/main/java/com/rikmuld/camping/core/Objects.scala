package com.rikmuld.camping.core

class KnifeInfo extends ObjInfo {
  override def NAME = "knife";
}

trait ObjInfo {
  def NAME: String = null
  def NAME_META: Array[String] = null
}