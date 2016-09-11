package controllers

import javax.inject._

import filters.{AuthenticationHelpers, Credentials, Password, User}
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
    Ok(views.html.login())
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

    Logger.info(loginInfo.userName)
    Logger.info(loginInfo.password)
    Logger.info(AuthenticationHelpers.authHeaderValue(userCredential))

    Redirect("/" + loginInfo.userName + "/portal").withHeaders(
      "Authorization" -> AuthenticationHelpers.authHeaderValue(userCredential))
 }
}
case class LoginInfo(userName:String, password:String)
