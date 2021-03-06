emailaddress
==================

![Language](https://img.shields.io/badge/Language-Scala-red.svg?logo=Scala&logoColor=red)
![Scala](https://img.shields.io/badge/scala-2.12.10-red)
![Scala](https://img.shields.io/badge/scala-2.13.1-red)
![Sbt](https://img.shields.io/badge/sbt-1.3.8-blue)

Scala micro-library for typing, validating and obfuscating email addresses

### Address Typing & Validation
The `EmailAddress` class will only accept valid addresses:

```scala
scala> import io.kinoplan.emailaddress._
 import io.kinoplan.emailaddress._

scala> EmailAddress("example@test.com")
res0: EmailAddress = example@test.com

scala> EmailAddress("not_a_meaningful_address")
java.lang.IllegalArgumentException: requirement failed: 'not_a_meaningful_address' is not a valid email address
```

You can also use `EmailAddress.isValid(...)`:

```scala
scala> EmailAddress.isValid("example@test.com")
res2: Boolean = true

scala> EmailAddress.isValid("not_a_meaningful_address")
res3: Boolean = false
```

### Accessing the domain and mailbox

You can access the mailbox and domain of a given address:

```scala
scala> EmailAddress("example@test.com").domain
res0: io.kinoplan.emailaddress.EmailAddress.Domain = test.com

scala> EmailAddress("example@test.com").mailbox
res1: io.kinoplan.emailaddress.EmailAddress.Mailbox = example
```

These compare equal as you might expect:

```scala
scala> EmailAddress("example@test.com").domain == EmailAddress("another@test.com").domain
res2: Boolean = true

scala> EmailAddress("example@test.com").domain == EmailAddress("another@test.co.uk").domain
res3: Boolean = false
```

### Obfuscation
Addresses are obfuscated by starring out all of their mailbox part, apart from the first and last letters:

```scala
scala> ObfuscatedEmailAddress("example@test.com")
res4: io.kinoplan.emailaddress.ObfuscatedEmailAddress = e*****e@test.com
```
Unless there are only two letters:

```scala
scala> ObfuscatedEmailAddress("ex@test.com")
res7: io.kinoplan.emailaddress.ObfuscatedEmailAddress = **@test.com```

```

You can also create them directly from an `EmailAddress`:

```scala
scala> EmailAddress("example@test.com").obfuscated
res6: io.kinoplan.emailaddress.ObfuscatedEmailAddress = e*****e@test.com
```


### Converting back to `String`
All classes `toString` and implicitly convert to `String`s nicely:

```scala
scala> val someString: String = EmailAddress("example@test.com")
someString: String = example@test.com

scala> val someString = EmailAddress("example@test.com").toString
someString: String = example@test.com

scala> val someString: String = ObfuscatedEmailAddress("example@test.com")
someString: String = e*****e@test.com

scala> val someString = ObfuscatedEmailAddress("example@test.com").toString
someString: String = e*****e@test.com

scala> EmailAddress("example@test.com").domain.toString
res4: String = test.com

scala> val s: String = EmailAddress("example@test.com").domain
s: String = test.com

scala> EmailAddress("example@test.com").mailbox.toString
res5: String = example

scala> val s: String = EmailAddress("example@test.com").mailbox
s: String = example
```

### Installing

Include the following dependency in your SBT build

#### Core

```scala
libraryDependencies += "io.kinoplan" %% "emailaddress-core" % "<current_version>"
```

#### Play Json 2.8.*

```scala
libraryDependencies ++= Seq(
   "io.kinoplan" %% "emailaddress-core" % "<current_version>",
   "io.kinoplan" %% "emailaddress-play-json" % "<current_version>"
)
```

In your code, use following import
```scala
import io.kinoplan.emailaddress.EmailAddressFormat._
```

#### Circe 0.13.*

```scala
libraryDependencies ++= Seq(
   "io.kinoplan" %% "emailaddress-core" % "<current_version>",
   "io.kinoplan" %% "emailaddress-circe" % "<current_version>"
)
```

In your code, use following import
```scala
import io.kinoplan.emailaddress.EmailAddressCodec._
```

#### Reactivemongo 0.18.*

```scala
libraryDependencies ++= Seq(
   "io.kinoplan" %% "emailaddress-core" % "<current_version>",
   "io.kinoplan" %% "emailaddress-reactivemongo" % "<current_version>"
)
```

In your code, use following import
```scala
import io.kinoplan.emailaddress.EmailAddressHandler._
```

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
