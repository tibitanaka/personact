package services

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

// H2なのが・・・１枚親クラスをかましておいてH2の記述が散らばらないようにするべきか？
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by 典晃 on 2016/08/13.
  */
class DBSampleService @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) {

  val dbConfig = dbConfigProvider.get[JdbcProfile]

  // パラメータのユーザIDからパスワードを取得する
  def dbSample(userId: String): Option[String] = {
    val users = TableQuery[UserTable]
    val q = users.filter(_.userId === userId.bind).result.headOption

    Await.result(dbConfig.db.run(q), Duration.Inf).map(_.password)
  }
}

// ここから下はmodelsディレクトリを作って移動させる
case class User(userId: String, password: String)

class UserTable(tag: Tag) extends Table[User](tag, "USER") {
  def userId = column[String]("USERID", O.PrimaryKey)
  def password = column[String]("PASSWORD")
  def * = (userId, password) <> (User.tupled, User.unapply)
}

