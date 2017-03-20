package com.example.akka.actor

import java.util.UUID

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSelection, Identify, Props}
import com.example.akka.actor.Counter.{Decrement, Increment}
import org.slf4j.LoggerFactory

object CounterWatcher {
  val props = Props[CounterWatcher]
  val name = "counter-watcher"
}

class CounterWatcher extends Actor {

  val logger = LoggerFactory getLogger Counter.getClass

  private[this] var counterRef: ActorRef = _

  logger debug "Finding counter actor via actor selection ..."
  private val counterActorSelection: ActorSelection = context.actorSelection("/user/counter")

  val correlationId: String = UUID.randomUUID().toString

  logger debug "Sending identify message to counter actor selection ..."
  counterActorSelection ! Identify(correlationId)

  override def receive: Receive = {
    case ActorIdentity(`correlationId`, Some(ref)) =>
      logger debug "Processing ActorIdentity response ..."
      logger info s"Actor reference for counter is: $ref"
      ref ! Increment(1)
      ref ! Decrement(1)
    case ActorIdentity(`correlationId`, None) => logger info s"Actor reference for counter is unavailable!"
    case _ => logger warn "CounterWatcher: Unknown message!"
  }
}
