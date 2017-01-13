package models


import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * Created by hungai on 1/13/17.
  */

case class Message(from:String,
                   description:String,
                   to:String,
                   messageId:Long = 0L)

object Message {
  /**
    * Serialization and Deserialization of the Message Model
    */

  implicit val messageReads:Reads[Message] = (
    (JsPath \ "from").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "to").read[String] and
      (JsPath \ "messageId").read[Long]
    )(Message.apply _)

  implicit val messageWrites:Writes[Message] = (
    (JsPath \ "from").write[String] and
      (JsPath \ "description").write[String] and
      (JsPath \ "to").write[String] and
      (JsPath \ "messageId").write[Long]
    )(unlift(Message.unapply))

}
