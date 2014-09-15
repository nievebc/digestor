package com.example.actors

import akka.actor.Actor
import akka.actor.ActorLogging
import org.apache.poi.ss.usermodel._
import scala.collection.JavaConversions._

object DigestorMessages {
  case class Start(file: String)
  case class NextRow(row: Map[String, String])
  case object Done
}

class RowTransformer extends Actor with ActorLogging {
  def receive = {
    case DigestorMessages.NextRow(row) => {
      if (row.contains("eof")) sender ! DigestorMessages.Done
      else row.foreach{case (col, value) => println(col + "-" + value)}
    }
  }
}