package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
  * Created by 典晃 on 2016/08/12.
  */
class DBSampleController@Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends Controller {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  def index = Action.async {implicit request =>
    Future(Ok(views.html.schedule()))
  }
}
