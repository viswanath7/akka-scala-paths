package com.example.akka.application

import akka.actor.{ActorRef, ActorSystem}
import com.example.akka.actor.Counter.{Decrement, Increment}
import com.example.akka.actor.{Counter, CounterWatcher}
import org.slf4j.LoggerFactory

import scala.language.postfixOps


object Watch extends App{
  val logger = LoggerFactory getLogger Counter.getClass

  logger info "Creating the actor system"
  private val actorSystem: ActorSystem = ActorSystem("actor-system")

  logger info "Creating a counter actor"
  actorSystem.actorOf(Counter.props, Counter.name)

  logger info "Creating a counter-watcher actor"
  actorSystem.actorOf(CounterWatcher.props, CounterWatcher.name)

  Thread sleep 1000
  logger info "Terminating actor system"
  actorSystem terminate
}
