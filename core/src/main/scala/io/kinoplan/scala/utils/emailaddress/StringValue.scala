package io.kinoplan.scala.utils.emailaddress

object StringValue {
  implicit def stringValueToString(e: StringValue): String = e.value
}

trait StringValue {
  def value: String
  override def toString: String = value
}
