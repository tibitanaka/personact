package filters

import AuthenticationHelpers._
import java.util.Base64
import play.api.mvc._
import play.api.mvc.Results._
import play.api.mvc.Security.AuthenticatedBuilder

object Authenticated extends AuthenticatedBuilder(
  _.headers.get("Authorization")
    .flatMap(parseAuthHeader)
    .flatMap(validateUser),
  onUnauthorized = { _ ⇒
    Unauthorized(views.html.defaultpages.unauthorized())
      .withHeaders("WWW-Authenticate" -> """Basic realm="Secured"""")
  }
)

object AuthenticationHelpers {
  val validCredentials = Set(
     Credentials(User("michael"), Password("correct password"))
    ,Credentials(User("tibitanaka"), Password("pass"))
    ,Credentials(User("louvre2489"), Password("pass"))
  )

  def authHeaderValue(credentials: Credentials) =
    "Basic " + Base64.getEncoder.encodeToString(s"${credentials.user.value}:${credentials.password.value}".getBytes)

  def parseAuthHeader(authHeader: String): Option[Credentials] =
    authHeader.split("""\s""") match {
      case Array("Basic", userAndPass) ⇒
        new String(Base64.getDecoder.decode(userAndPass), "UTF-8").split(":") match {
          case Array(user, password) ⇒ Some(Credentials(User(user), Password(password)))
          case _                     ⇒ None
        }
      case _ ⇒ None
    }

  def validateUser(c: Credentials): Option[User] =
    if (validCredentials.contains(c))
      Some(c.user)
    else
      None
}

case class Credentials(user: User, password: Password)
case class User(value: String) extends AnyVal
case class Password(value: String) extends AnyVal
