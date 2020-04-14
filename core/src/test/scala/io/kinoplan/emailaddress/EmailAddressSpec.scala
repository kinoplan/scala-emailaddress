package io.kinoplan.emailaddress

import EmailAddress.{Domain, Mailbox}
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class EmailAddressSpec extends AnyWordSpec
  with Matchers
  with ScalaCheckPropertyChecks
  with EmailAddressGenerators {

  ScalaCheckPropertyChecks
  "Creating an EmailAddress class" should {
    "work for a valid email" in {
      forAll(validEmailAddresses()) { address =>
        EmailAddress(address).value should be(address)
      }
      validEmailAddressStandardList.foreach{ address =>
        EmailAddress(address).value should be(address)
      }
    }

    "works for worlds longest active email address" in {
      EmailAddress("contact-admin-hello-webmaster-info-services" +
        "-peter-crazy-but-oh-so-ubber-cool-english-alphabet-loverer" +
        "-abcdefghijklmnopqrstuvwxyz@please-try-to.send-me-an-email-if-" +
        "you-can-possibly-begin-to-remember-this-coz.this-is-the-longest-email-address" +
        "-known-to-man-but-to-be-honest.this-is-such-a-stupidly-long-sub-domain" +
        "-it-could-go-on-forever.pacraig.com").value should be(
        "contact-admin-hello-webmaster-info-services-peter-crazy-but-oh-so-ubber" +
        "-cool-english-alphabet-loverer-abcdefghijklmnopqrstuvwxyz@please-try-to.send-me-an-email" +
        "-if-you-can-possibly-begin-to-remember-this-coz.this-is-the-longest-email-address-known-" +
        "to-man-but-to-be-honest.this-is-such-a-stupidly-long-sub-domain-it-could-go-on-forever.pacraig.com")
    }

    "works for more dots in email" in {
      EmailAddress("example@e.example.co.uk").value should be("example@e.example.co.uk")
      EmailAddress("ex.am.ple@example.com").value should be("ex.am.ple@example.com")
    }

    "throw an exception for an email that's not considered standard" in {
      an [IllegalArgumentException] should be thrownBy {EmailAddress("plainaddress")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("#@%^%#$@#$@#.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("@domain.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("Joe Smith <email@domain.com>")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email.domain.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email@domain@domain.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress(".email@domain.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email.@domain.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email..email@domain.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("あいうえお@domain.com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email@domain.com (Joe Smith)")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email@domain..com")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email@domain")}
      an [IllegalArgumentException] should be thrownBy {EmailAddress("email@-domain.com")}
    }

    "throw an exception for an email that starts with .(dot)" in {
      an [IllegalArgumentException] should be thrownBy {EmailAddress(".example.e@test.com")}
    }

    "throw an exception for an email that ends with .(dot)" in  {
      an [IllegalArgumentException] should be thrownBy {EmailAddress("example@test.co.")}
    }

    "throw an exception for an email that has ..(2 dots) next to each other" in {
    an [IllegalArgumentException] should be thrownBy {EmailAddress("example..e@test.com")}
    an [IllegalArgumentException] should be thrownBy {EmailAddress("example@test..com")}
    }

    "throw an exception for an invalid email" in {
      an [IllegalArgumentException] should be thrownBy { EmailAddress("sausages") }
    }

    "throw an exception for an valid email starting with invalid characters" in {
      forAll(validEmailAddresses()) { address =>
        an [IllegalArgumentException] should be thrownBy { EmailAddress("§"+ address) }
      }
    }

    "throw an exception for an valid email ending with invalid characters" in {
      forAll(validEmailAddresses()) { address =>
        an [IllegalArgumentException] should be thrownBy { EmailAddress(address + "§") }
      }
    }

    "throw an exception for an empty email" in {
      an [IllegalArgumentException] should be thrownBy { EmailAddress("") }
    }

    "throw an exception for a repeated email" in {
      an[IllegalArgumentException] should be thrownBy { EmailAddress("test@domain.comtest@domain.com") }
    }

    "throw an exception when the '@' is missing" in {
      forAll { s: String => whenever(!s.contains("@")) {
        an[IllegalArgumentException] should be thrownBy { EmailAddress(s) }
      }}
    }
  }

  "An EmailAddress class" should {
    "implicitly convert to a String of the address" in {
      val e: String = EmailAddress("test@domain.com")
      e should be ("test@domain.com")
    }
    "toString to a String of the address" in {
      val e = EmailAddress("test@domain.com")
      e.toString should be ("test@domain.com")
    }
    "be obfuscatable" in {
      EmailAddress("abcdef@example.com").obfuscated.value should be("a****f@example.com")
    }
    "have a local part" in forAll (validMailbox, validDomain) { (mailbox, domain) =>
      val exampleAddr = EmailAddress(s"$mailbox@$domain")
      exampleAddr.mailbox should (be (a[Mailbox]) and have (Symbol("value")  (mailbox)))
      exampleAddr.domain should (be (a[Domain]) and have (Symbol("value")  (domain)))
    }
  }

  "A email address domain" should {
    "be extractable from an address" in forAll (validMailbox, validDomain) { (mailbox, domain) =>
      EmailAddress(s"$mailbox@$domain").domain should (be (a[Domain]) and have (Symbol("value") (domain)))
    }
    "be creatable for a valid domain" in forAll (validDomain) { domain =>
      EmailAddress.Domain(domain) should (be (a[Domain]) and have (Symbol("value")  (domain)))
    }
    "not create for invalid domains" in {
      an [IllegalArgumentException] should be thrownBy EmailAddress.Domain("")
      an [IllegalArgumentException] should be thrownBy EmailAddress.Domain("e.")
      an [IllegalArgumentException] should be thrownBy EmailAddress.Domain(".uk")
      an [IllegalArgumentException] should be thrownBy EmailAddress.Domain(".com")
      an [IllegalArgumentException] should be thrownBy EmailAddress.Domain("*domain")
    }
    "compare equal if identical" in forAll (validDomain, validMailbox, validMailbox) { (domain, mailboxA, mailboxB) =>
      val exampleA = EmailAddress(s"$mailboxA@$domain")
      val exampleB = EmailAddress(s"$mailboxB@$domain")
      exampleA.domain should equal (exampleB.domain)
    }
    "not compare equal if completely different" in forAll (validMailbox, validDomain, validDomain) { (mailbox, domainA, domainB) =>
      val exampleA = EmailAddress(s"$mailbox@$domainA")
      val exampleB = EmailAddress(s"$mailbox@$domainB")
      exampleA.domain should not equal exampleB.domain
    }
    "toString to a String of the domain" in {
      Domain("domain.com").toString should be ("domain.com")
    }
    "implicitly convert to a String of the domain" in {
      val e: String = Domain("domain.com")
      e should be ("domain.com")
    }
  }

  "A email address mailbox" should {

    "be extractable from an address" in forAll (validMailbox, validDomain) { (mailbox, domain) =>
      EmailAddress(s"$mailbox@$domain").mailbox should (be (a[Mailbox]) and have (Symbol("value")  (mailbox)))
    }
    "compare equal" in forAll (validMailbox, validDomain, validDomain) { (mailbox, domainA, domainB) =>
      val exampleA = EmailAddress(s"$mailbox@$domainA")
      val exampleB = EmailAddress(s"$mailbox@$domainB")
      exampleA.mailbox should equal (exampleB.mailbox)
    }
    "not compare equal if completely different" in forAll (validDomain, validMailbox, validMailbox) { (domain, mailboxA, mailboxB) =>
      val exampleA = EmailAddress(s"$mailboxA@$domain")
      val exampleB = EmailAddress(s"$mailboxB@$domain")
      exampleA.mailbox should not equal exampleB.mailbox
    }
    "toString to a String of the domain" in {
      EmailAddress("test@domain.com").mailbox.toString should be ("test")
    }
    "implicitly convert to a String of the domain" in {
      val e: String = EmailAddress("test@domain.com").mailbox
      e should be ("test")
    }
  }
}
