package com.example.akka.actor

import akka.actor.{Actor, Props}
import com.example.akka.actor.Counter.{Decrement, Increment}
import org.slf4j.LoggerFactory

object Counter {
  val props = Props[Counter]
  val name = "counter"

  sealed trait RequestMessage
  case class Increment(number:Int) extends RequestMessage
  case class Decrement(number:Int) extends RequestMessage
}

class Counter extends Actor {

  val logger = LoggerFactory getLogger Counter.getClass

  private [this] var count :Int = _

  override def receive: Receive = {
    case Increment(num) => count +=num
      logger debug s"Incremented count to $count"
    case Decrement(num) => count -=num
      logger debug s"Decremented count to $count"
    case _ =>
      logger warn "Counter: Unknown message!"
  }
}
