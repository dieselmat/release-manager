package models

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]


  import dbConfig._
  import profile.api._


  private class UserTable(tag: Tag) extends Table[User](tag, "user") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email")
    def isActive = column[Boolean]("is_active", O.Default(true))
    def * = (id, name, email, isActive) <> ((User.apply _).tupled, User.unapply)
  }


  private val user = TableQuery[UserTable]


  def create(name: String, email: String, isActive: Boolean): Future[User] = db.run {
    // We create a projection of just the name and age columns, since we're not inserting a value for the id column
    (user.map(u => (u.name, u.email, u.isActive))
      // Now define it to return the id, because we want to know what id was generated for the person
      returning user.map(_.id)
      // And we define a transformation for the returned value, which combines our original parameters with the
      // returned id
      into ((details, id) => User(id, details._1, details._2, details._3))
    // And finally, insert the person into the database
    ) += (name, email, isActive)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[User]] = db.run {
    user.result
  }
}
