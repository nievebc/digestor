package com.example.actors

import info.folone.scala.poi._
import scalaz._
import syntax.monoid._
import syntax.foldable._
import std.list._
import org.apache.poi.hssf.usermodel._
import java.io._
import akka.routing.SmallestMailboxRouter
import scala.collection.JavaConversions._

import akka.actor.Actor
import akka.actor.Props

class Dispatcher extends Actor {
  def receive = {
    case DigestorMessages.Start => treatRows("worksheet.xls")
  }

  def treatRows(file: String) = {
    val book = new HSSFWorkbook(new FileInputStream(file))
    val sheet = book.getSheetAt(0)
    val rowsCount = sheet.getLastRowNum
    val router = context.actorOf(Props[RowTransformer].withRouter(SmallestMailboxRouter(rowsCount)), "router")
    val rows = sheet.rowIterator
    rows.filter(_ != null).foreach((x: org.apache.poi.ss.usermodel.Row) => router.tell(DigestorMessages.NextRow(x), sender))
  }
}