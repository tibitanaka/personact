package controllers

import javax.inject._

import filters.Authenticated
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 */
@Singleton
class PortalController @Inject() extends Controller {

  def init(userId:String) = Authenticated {
    Ok(views.html.portal())
  }

}
