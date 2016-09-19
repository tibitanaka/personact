package filters

import java.security.MessageDigest
import SessionHelpers._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.mvc.Security.AuthenticatedBuilder
import scala.collection.mutable

/**
  * AuthenticationBuilderの拡張、認証済みでなければアクションを実行しない
  */
object Authenticated extends AuthenticatedBuilder(
  request => validateSessionCredential(request.session),
  request => {
    Unauthorized(views.html.defaultpages.unauthorized())
  }
)

object SessionHelpers {
  // 既存セッションを管理する
  val sessionKeys =  mutable.Map.empty[String, String]

  // ブラウザセッション上の認証情報を照合する
  def validateSessionCredential(session:Session): Option[User] = {
    val userKey = session.get("UserKey")
    val sessionKey = session.get("SessionKey")
    if (userKey.nonEmpty && sessionKey.nonEmpty) {
      val userId = play.api.libs.Crypto.decryptAES(userKey.get)
      if (sessionKeys.contains(userId) && sessionKeys(userId) == sessionKey.get)
        Some(User(userId))
      else
        None
    } else {
      None
    }
  }
}

object LoginHelpers {
  // テスト用のダミーパスワード
  val validCredentials = Set(
    Credentials(User("michael"), Password(toSha1Digest("correct password")))
    ,Credentials(User("tibitanaka"), Password(toSha1Digest("pass")))
    ,Credentials(User("louvre2489"), Password(toSha1Digest("pass")))
  )
   // SHA1でハッシュ化された値を返却する
  def toSha1Digest(original: String): String = {
    val md = MessageDigest.getInstance("SHA-1")
    md.update(original.getBytes)
    md.digest.foldLeft("") { (s, b) => s + "%02x".format(if (b < 0) b + 256 else b) }
  }

  // 認証情報を検証する
  def validateCredential(c:Credentials):Boolean = {
    val tmpC = Credentials(c.user, Password(toSha1Digest(c.password.value)))
    if (validCredentials.contains(tmpC)) true else false
  }

  // サーバ、ブラウザにセッション情報を書き込む
  def registerCredentialToSession(session:Session, credential: Credentials):Session = {
    // サーバ側にセッションキーを登録
    sessionKeys(credential.user.value)
      = new scala.util.Random(new java.security.SecureRandom()).alphanumeric.take(64).mkString

    // ブラウザ側にセッションにキーを登録
    ((session
      + ("UserKey" -> play.api.libs.Crypto.encryptAES(credential.user.value)))
      + ("SessionKey" -> sessionKeys(credential.user.value)))
  }
}

case class Credentials(user: User, password: Password)
case class User(value: String) extends AnyVal
case class Password(value: String) extends AnyVal
