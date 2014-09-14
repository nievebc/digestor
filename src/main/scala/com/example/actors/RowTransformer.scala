package com.example.actors

import akka.actor.Actor
import akka.actor.ActorLogging
import org.apache.poi.ss.usermodel._
import scala.collection.JavaConversions._

object DigestorMessages {
  case class Start(file: String)
  case class NextRow(row: Row)
  case object Done
}

class RowTransformer extends Actor with ActorLogging {

  def receive = {
    case DigestorMessages.NextRow(row) => {
      val cells = row.cellIterator
      cells.zipWithIndex.foreach{ case(cell, ix) => println(cell.toString + "-" + ix)}
      if (row.getRowNum == row.getSheet.getLastRowNum) sender ! DigestorMessages.Done
    }
  }
}