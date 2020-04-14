package io.kinoplan.emailaddress

import io.circe.{DecodingFailure, Json}
import io.circe.syntax._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class EmailAddressCodecSpec extends AnyWordSpec with Matchers with EmailAddressCodec {

  "Reading an EmailAddress from JSON" should {

    "work for a valid email address" in {
      val result = Json.fromString("a@b.com").as[EmailAddress]

      result shouldBe Right(EmailAddress("a@b.com"))
    }

    "fail for a invalid email address" in {
      val result = Json.fromString("ab.com").as[EmailAddress]
      result shouldBe Left(DecodingFailure("Decode email error", List()))
    }
  }

  "Writing an EmailAddress to JSON" should {

    "work!" in {
      EmailAddress("a@b.com").asJson should be (Json.fromString("a@b.com"))
    }
  }
}
