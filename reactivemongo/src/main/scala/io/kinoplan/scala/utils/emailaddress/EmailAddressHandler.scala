package io.kinoplan.scala.utils.emailaddress

import reactivemongo.bson.{BSONReader, BSONString, BSONWriter}

trait EmailAddressHandler {
  implicit val emailAddressWriter: BSONWriter[EmailAddress, BSONString] = (email: EmailAddress) => BSONString(email.toString)

  implicit val emailAddressReader: BSONReader[BSONString, EmailAddress] = (bson: BSONString) => EmailAddress(bson.value)
}

object EmailAddressHandler extends EmailAddressHandler
