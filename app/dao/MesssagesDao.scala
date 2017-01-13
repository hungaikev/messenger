package dao

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

/**
  * Created by hungai on 1/13/17.
  */

trait MessagesComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>
  import driver.api._

  class Messages(tag:Tag) extends Table[Message](tag, "messages"){

    def messageId = column[Long]("question_id",O.PrimaryKey,O.AutoInc)
    def from = column[String]("from")
    def description = column[String]("description")
    def to = column[String]("to")

    def * = (from,description,to,messageId) <> ((Message.apply _).tupled,Message.unapply _)
  }
}

@Singleton
class MesssagesDao @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends MessagesComponent with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  lazy val messages = TableQuery[Messages]

  lazy val insertMessage = messages returning messages.map(_.messageId)

  /**
    * Add a Message to the Database
    * @param message
    * @return  Id belong to the message
    */
  def addMessage(message: Message):Future[Long] =
    db.run(insertMessage += message)

  /**
    * Add Messages to the database
    * @param messages
    * @return
    */
  def addMessages(messages: Seq[Message]):Future[Unit] =
    db.run(this.messages ++=messages).map(_ => ())

  /**
    * Find a message using an Id
    * @param messageId
    * @return
    */
  def findMessageById(messageId:Long):Future[Option[Message]] =
    db.run(messages.filter(_.messageId === messageId).result.headOption)

  /**
    * Delete a message 
    * @param messageId
    * @return
    */
  def deleteMessage(messageId:Long):Future[Unit] =
    db.run(messages.filter(_.messageId === messageId).delete).map(_ => ())



}
