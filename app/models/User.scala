package models

import slick.driver.H2Driver.api._

/**
  * Created by 典晃 on 2016/08/14.
  */
case class User(userId: String, password: String)

class UserTable(tag: Tag) extends Table[User](tag, "USER") {
  def userId = column[String]("USERID", O.PrimaryKey)
  def password = column[String]("PASSWORD")
  def * = (userId, password) <> (User.tupled, User.unapply)
}

