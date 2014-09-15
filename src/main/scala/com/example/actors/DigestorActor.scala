package com.example.actors

import com.github.tototoshi.csv._
import java.io._

import akka.routing.SmallestMailboxRouter
import akka.actor.Actor
import akka.actor.Props

class RowsRouter extends Actor {
  def receive = {
    case DigestorMessages.Start(file) => treatCsvRows(file)
  }

  def treatCsvRows(file: String) = {
    val reader = CSVReader.open(new File("mpd1.csv"))
    val rows = reader.allWithHeaders
    val router = context.actorOf(Props[RowTransformer].withRouter(SmallestMailboxRouter(rows.length)), "router")
    val finalised_rows = (Map("eof" -> "true") :: rows.reverse).reverse
    finalised_rows.foreach(row => router.tell(DigestorMessages.NextRow(row), sender))
  }
}