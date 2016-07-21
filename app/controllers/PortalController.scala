package controllers

import javax.inject._

import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 */
@Singleton
class PortalController @Inject() extends Controller {

  def init(userId:String) = Action {
    Ok(views.html.portal())
  }

}
