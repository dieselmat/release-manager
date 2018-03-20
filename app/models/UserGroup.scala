package models

import play.api.libs.json.Json

case class UserGroup(userId: Long, groupId: Long)

object UserGroup {
  implicit val groupFormat = Json.format[UserGroup]
}




