package controllers

import javax.inject._

import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 */
@Singleton
class DiscussionController @Inject() extends Controller {

  def init = Action {
    Ok(views.html.discussion())
  }

  def show(articleId:Int) = Action {
    Ok(views.html.discussion())
  }
}
