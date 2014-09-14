package com.example

import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import com.example.actors.{Dispatcher, DigestorMessages}

case object Start
class Supervisor extends Actor {
  override def preStart(): Unit = {
    // create the greeter actor
    val dispatcher = context.actorOf(Props[Dispatcher], "dispatcher")
    // tell it to perform the greeting
    dispatcher ! DigestorMessages.Start
  }

  def receive = {
    case DigestorMessages.Done => 
      println("terminating Supervisor")
      context stop self
  }
}

/**
 * This is actually just a small wrapper around the generic launcher
 * class akka.Main, which expects only one argument: the class name of
 * the application?s main actor. This main method will then create the
 * infrastructure needed for running the actors, start the given main
 * actor and arrange for the whole application to shut down once the main
 * actor terminates.
 *
 * Thus you could also run the application with a
 * command similar to the following:
 * java -classpath  akka.Main com.example.actors.HelloWorldActor
 *
 * @author nieve
 */
object MainApp {

  def main(args: Array[String]): Unit = {
    // val system = ActorSystem("digestor")

    // val dispatcher = system.actorOf(Props[Dispatcher], "dispatcher")
    // dispatcher ! DigestorMessages.Start("worksheet.xls")
    val initialActor = classOf[Supervisor].getName

    akka.Main.main(Array(initialActor))
  }

}
