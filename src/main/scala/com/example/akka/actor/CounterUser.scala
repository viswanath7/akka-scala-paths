package com.example.akka.actor

import java.util.UUID

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSelection, Identify, PoisonPill, Props, Terminated}
import com.example.akka.actor.Counter.{Decrement, Increment}
import org.slf4j.LoggerFactory

object CounterUser {
  val props = Props[CounterUser]
  val name = "counter-user"
}

class CounterUser extends Actor {

  val logger = LoggerFactory getLogger Counter.getClass

  private[this] var counterRef: ActorRef = _

  logger debug "Finding counter actor via actor selection using context ..."
  // Note the relative path used here to access the parent actor
  private val counterActorSelection: ActorSelection = context.actorSelection("../counter")

  val correlationId: String = UUID.randomUUID().toString

  logger debug "Sending identify message to counter actor selection ..."
  counterActorSelection ! Identify(correlationId)

  override def receive: Receive = {
    case ActorIdentity(`correlationId`, Some(counterActorRef)) =>
      logger debug "Processing ActorIdentity response ..."
      logger info s"Actor reference for counter is: $counterActorRef"
      counterActorRef ! Increment(1)
      counterActorRef ! Decrement(1)
      logger debug s"Watching ${counterActorRef.path.name} ..."
      context.watch(counterActorRef)
      counterActorRef ! PoisonPill
    case ActorIdentity(`correlationId`, None) => logger info s"Actor reference for counter is unavailable!"
    case Terminated(actorRef) => logger info s"${actorRef.path.name} actor was terminated!"
    case _ => logger warn "CounterWatcher: Unknown message!"
  }
}
