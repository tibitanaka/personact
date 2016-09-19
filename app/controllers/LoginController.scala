package controllers

import javax.inject._

import filters.{LoginHelpers, Credentials, Password, User}
import play.api.Logger
import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class LoginController @Inject() extends Controller {

 /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def init = Action {
    Ok(views.html.login(""))
  }

  def login = Action { implicit request =>
    val loginForm = Form(
      mapping(
        "userName" -> text,
        "password" -> text
      )(LoginInfo.apply)(LoginInfo.unapply)
    )
    val loginInfo = loginForm.bindFromRequest.get
    val userCredential = Credentials(User(loginInfo.userName), Password(loginInfo.password))

    if (LoginHelpers.validateCredential(userCredential))
      // 認証情報が妥当であれば、ポータル画面に遷移
      Redirect("/" + loginInfo.userName + "/portal").withSession(
        LoginHelpers.registerCredentialToSession(request.session, userCredential))
    else
      // とりあえず元の画面に戻す
      BadRequest(views.html.login("ユーザ名、またはパスワードが違います"))
  }
}
case class LoginInfo(userName:String, password:String)
