package controllers

import javax.inject.Inject

import play.api.mvc._
import services.DBSampleService

/**
  * Created by 典晃 on 2016/08/12.
  */
class DBSampleController @Inject() (protected val myService: DBSampleService) extends Controller {

  def sample = Action {implicit request =>

    val r = myService.dbSample("test")
    val pass = r.getOrElse("")

    Ok(views.html.schedule())
  }
}
