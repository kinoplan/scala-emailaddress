package io.kinoplan.scala.utils.emailaddress

import play.api.libs.json._

object EmailAddressFormat {
  implicit val emailAddressReads: Reads[EmailAddress] = (js: JsValue) => js.validate[String].flatMap {
    case s if EmailAddress.isValid(s) => JsSuccess(EmailAddress(s))
    case _ => JsError("not a valid email address")
  }

  implicit val emailAddressWrites: Writes[EmailAddress] = (e: EmailAddress) => JsString(e.value)
}
