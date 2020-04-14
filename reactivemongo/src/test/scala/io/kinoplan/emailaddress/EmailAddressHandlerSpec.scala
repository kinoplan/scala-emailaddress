package io.kinoplan.emailaddress

import org.scalatest.TryValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import reactivemongo.bson.{BSONDocument, BSONString}

class EmailAddressHandlerSpec extends AnyWordSpec with Matchers with EmailAddressHandler with TryValues {

  "Reading an EmailAddress from BSON" should {
    "work for a valid email address" in {
      BSONString("a@b.com").as[EmailAddress] shouldBe EmailAddress("a@b.com")
    }

    "fail for a invalid email address" in {
      BSONString("ab.com").asTry[EmailAddress].failure.exception shouldBe a [IllegalArgumentException]
    }
  }

  "Writing an EmailAddress to BSON" should {
    "work!" in {
      BSONDocument("email" -> EmailAddress("a@b.com")).get("email").get shouldBe BSONString("a@b.com")
    }
  }
}
