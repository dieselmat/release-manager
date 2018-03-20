package models

import play.api.libs.json.Json

case class Group(id: Long, name: String, isActive: Boolean)

object Group {
  implicit val groupFormat = Json.format[Group]
}


