package io.kinoplan.scala.utils.emailaddress

import scala.util.Random

import org.scalacheck.Gen
import org.scalacheck.Gen._

trait EmailAddressGenerators {
  def nonEmptyString(char: Gen[Char]): Gen[String] =
    nonEmptyListOf(char)
      .map(_.mkString)
      .suchThat(!_.isEmpty)

  def chars(chars: String): Gen[Char] = Gen.choose(0, chars.length - 1).map(chars.charAt)

  val validMailbox: Gen[String] = nonEmptyString(oneOf(alphaChar, chars("!#$%&’'*+/=?^_`{|}~-"))).label("mailbox")

  val currentDomain: String = Random.shuffle(Seq(".com", ".co.uk", ".io", ".london", ".org", ".FOUNDATION", ".ru")).head

  val validDomain: Gen[String] = (for {
    topLevelDomain <- nonEmptyString(alphaChar)
    otherParts <- listOf(nonEmptyString(alphaChar))
  } yield (otherParts :+ topLevelDomain + currentDomain).mkString(".")).label("domain")

  def validEmailAddresses(mailbox: Gen[String] = validMailbox, domain: Gen[String] = validDomain): Gen[String] =
    for {
      mailbox <- mailbox
      domain <- domain
    } yield s"$mailbox@$domain"

  def validEmailAddressStandardList = List(
    "email@domain.ru",
    "email@domain.com",
    "firstname.lastname@domain.com",
    "email@subdomain.domain.com",
    "firstname+lastname@domain.com",
    "email@123.123.123.123",
    "email@[123.123.123.123]",
    "\"email\"@domain.com",
    "1234567890@domain.com",
    "email@domain-one.com",
    "_______@domain.com",
    "email@domain.name",
    "email@domain.co.jp",
    "firstname-lastname@domain.com"
  )
}
