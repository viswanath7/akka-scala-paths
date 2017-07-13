package com.example.akka.application

import akka.actor.{ActorSystem, Kill}
import com.example.akka.actor.Counter
import com.example.akka.actor.Counter.{Decrement, Increment}
import org.slf4j.LoggerFactory


object Client extends App {

  val logger = LoggerFactory getLogger Client.getClass

  private val actorName = Counter.name

  logger info "Creating the actor system"
  private val actorSystem: ActorSystem = ActorSystem("actor-system")

  private val counterActorRef = actorSystem.actorOf(Counter.props, actorName)
  logger info s"Created a counter actor ${counterActorRef.path}"


  logger info s"Finding the $actorName actor through actor selection ..."
  private val actorSelection = actorSystem.actorSelection("akka://actor-system/user/counter")

  logger info s"Using actor selection '${actorSelection.pathString}' to send increment message to $actorName actor ..."
  actorSelection ! Increment(1)

  logger info s"Killing the $actorName actor ..."

  /**
   * Kill message causes the actor to throw an ActorKilledException which gets handled using the normal supervisor mechanism.
   */
  counterActorRef ! Kill
  Thread sleep 5000 // Please wait a bit till the actor is killed as it's done asynchronously

  private val actorRef = actorSystem.actorOf(Counter.props, actorName)
  logger info s"Recreated counter actor ${actorRef.path}"


  logger info s"Using actor selection ${actorSelection.pathString} to send decrement message to $actorName actor ..."
  actorSelection ! Decrement(1)

  Thread sleep 1000
  logger info "Terminating actor system ..."
  actorSystem terminate
}
