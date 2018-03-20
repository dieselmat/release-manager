package models

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserGroupRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class UserGroupTable(tag: Tag) extends Table[UserGroup](tag, "user_group") {

    def userId = column[Long]("user_id")
    def groupId = column[Long]("group_id")
    def * = (userId, groupId) <> ((UserGroup.apply _).tupled, UserGroup.unapply)
  }


  private val userGroup = TableQuery[UserGroupTable]


  def create(userId: Long, groupId: Long): Future[UserGroup] = db.run {
    (userGroup.map(ug => (ug.userId, ug.groupId))
      returning userGroup.map(x => (x.userId, x.groupId))
      into ((details, _) => UserGroup(details._1, details._2))
    ) += (userId, groupId)
  }

  /**
   * List all the people in the database.
   */
  def list(): Future[Seq[UserGroup]] = db.run {
    userGroup.result
  }
}
