package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import javax.inject.Inject

import dao._
import models._
import play.api.libs.json._

import scala.concurrent.Future


/**
  * Created by hungai on 1/13/17.
  */
class Messages @Inject() (messsagesDao: MesssagesDao) extends Controller {

  /**
    * Action that returns all messages stored
    * @return
    */
  def messages = Action.async { implicit request =>
    messsagesDao.allMessages.map( ms => Ok(Json.toJson(ms)))
      .recover { case e => InternalServerError(s"Error getting all the messages ${e}")}

  }

  /**
    * Action that creates/ sends a new message
    * @return
    */
  def newMessages = Action.async(parse.json) { implicit  request =>
    val message = request.body.validate[Message]
    message.fold(
      errors => Future(BadRequest(Json.obj( "status" -> "Parsing message failed", "error" -> JsError.toJson(errors)))),
      message => messsagesDao.addMessage(message).map(m =>
        Ok(Json.obj("status" -> "succes","message" -> Json.toJson(m)))
      )
    )
  }


  /**
    * Get a single message
    * @param id
    * @return
    */
  def show(id:Int) = Action.async { implicit request =>
    val message = messsagesDao.findMessageById(id)
    message.map( ms => Ok(Json.obj("status" -> "Success", "message" -> Json.toJson(ms))))
      .recover { case e => InternalServerError(s"Error selecting message with id -> ${id}: ${e}") }

  }

  /**
    * Update a single message
    * @param id
    * @param message
    * @return
    */
  def update(id:Int,message:String) = Action.async { implicit rs =>
    messsagesDao.updateMessage(id,message).map(ms =>
      Ok(Json.obj("successfully updated" -> s"Message with id $id")))

  }


  /**
    * Delete a single message
    * @param id
    * @return
    */
  def delete(id:Int) = Action.async { implicit  rs =>
    for {
      _  <- messsagesDao.deleteMessage(id)
    } yield Ok(Json.obj("success" -> "Message Deleted"))
  }

}
