package com.example.actors

import com.github.tototoshi.csv._
import java.io._

import akka.routing.SmallestMailboxRouter
import akka.actor.{ActorRef, Actor}
import akka.actor.Props

class RowsRouter extends Actor {
  var rowsCount = 0
  var initialSender: Option[ActorRef] = None
  def receive = {
    case Messages.Start(file) => 
      initialSender = Some(sender)
      treatCsvRows(file)
    case Messages.Done => 
      rowsCount -= 1
      if (rowsCount < 1) initialSender match {
        case Some(supervisor) => supervisor ! Messages.Done
        case None => println("sender not found")
      }
  }

  def treatCsvRows(file: String) = {
    val reader = CSVReader.open(new File("mpd1.csv"))
    val rows = reader.allWithHeaders
    rowsCount = rows.length
    val router = context.actorOf(Props[RowTransformer].withRouter(SmallestMailboxRouter(rowsCount)), "router")
    rows.foreach(row => router.tell(Messages.NextRow(row), self))
  }
}