package models

import play.api.libs.json.Json

case class User(id: Long, name: String, email: String, isActive: Boolean)

object User {
  implicit val userFormat = Json.format[User]
}
