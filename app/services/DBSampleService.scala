package services

import javax.inject.Inject

import models.UserTable
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by 典晃 on 2016/08/13.
  */
trait DBSampleService {

  /**
    * パラメータのユーザIDからパスワードを取得する
 *
    * @param userId ユーザID
    * @return パスワード
    */
  def dbSample(userId: String): Option[String]
}

class DBSampleServiceImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends DBSampleService {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  /**
    * パラメータのユーザIDからパスワードを取得する
 *
    * @param userId ユーザID
    * @return パスワード
    */
  def dbSample(userId: String): Option[String] = {
    val users = TableQuery[UserTable]
    val q = users.filter(_.userId === userId.bind).result.headOption

    Await.result(dbConfig.db.run(q), Duration.Inf).map(_.password)
  }
}