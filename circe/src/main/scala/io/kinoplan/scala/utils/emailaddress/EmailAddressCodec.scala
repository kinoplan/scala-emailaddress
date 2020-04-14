package io.kinoplan.scala.utils.emailaddress

import io.circe.{Decoder, Encoder, Json}

trait EmailAddressCodec {
  implicit val decoder: Decoder[EmailAddress] = Decoder.decodeString.emap { email =>
    Either.cond(EmailAddress.isValid(email), EmailAddress(email), "Decode email error")
  }

  implicit val encode: Encoder[EmailAddress] = (email: EmailAddress) => Json.fromString(email.toString)
}

object EmailAddressCodec extends EmailAddressCodec
