package controllers

import javax.inject._

import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 */
@Singleton
class ScheduleController @Inject() extends Controller {

  def init(userId:String) = Action {
    Ok(views.html.schedule())
  }

  def show(userId:String, month:Int) = Action {
    Ok(views.html.schedule())
  }
}
