package models

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GroupRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class GroupTable(tag: Tag) extends Table[Group](tag, "group") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def isActive = column[Boolean]("is_active")
    def * = (id, name, isActive) <> ((Group.apply _).tupled, Group.unapply)
  }

  private val group = TableQuery[GroupTable]

  def create(name: String, email: String, isActive: Boolean): Future[Group] = db.run {
    (group.map(u => (u.name, u.isActive))
      returning group.map(_.id)
      into ((details, id) => Group(id, details._1, details._2))

    ) += (name, isActive)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[Group]] = db.run {
    group.result
  }
}
