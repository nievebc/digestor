package com.example.actors

import akka.actor.Actor
import akka.actor.Props

class Dispatcher extends Actor {
  def receive = {
    case DigestorMessages.Start => 
      val smallestMailboxRouter = context.actorOf(Props[Greeter].withRouter(SmallestMailboxRouter(5)), "router")
      1 to 20 foreach { i ⇒ smallestMailboxRouter ! WhoToGreet(i.toString) }
  }
  override def preStart(): Unit = {
    // create the greeter actor
    val greeter = context.actorOf(Props[GreeterActor], "greeter")

    // Send it the 'Greet' message
    greeter ! GreeterMessages.Greet
  }

//   def receive = {
//     // When we receive the 'Done' message, stop this actor
//     // (which if this is still the initialActor will trigger the deathwatch and stop the entire ActorSystem)
//     case GreeterMessages.Done => {
//       context.stop(self)
//     }
//   }
}

