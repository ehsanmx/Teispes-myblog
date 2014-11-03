package models

import play.api.libs.json.Json
import play.api.data._
import play.api.data.Forms._

case class Post(title: String,body :String)

object JsonFormats {
  implicit val postFormat = Json.format[Post]
}
