package com.example.akka.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.example.akka.actor.Counter.{Decrement, Increment}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Unit test for Counter Actor
  *
  * The TestKit contains an actor named 'testActor' which is the entry point for messages to be examined
  * with the various expectMsg() assertions.
  *
  * When mixing in the trait ImplicitSender this test actor is implicitly used as sender reference
  * when dispatching messages from the test procedure.
  *
  */
class CounterSpec extends TestKit(ActorSystem("test-system")) with ImplicitSender
  with FlatSpecLike with  Matchers with BeforeAndAfterAll {

  val logger = LoggerFactory getLogger this.getClass.getSimpleName


  override def afterAll: Unit = {
    logger debug "Shutting down the actor system once all the tests have been completed " +
      "so that all actors—including the test actor—are stopped. ..."
    TestKit shutdownActorSystem system
  }

  "Counter Actor" should "handle Increment and Decrement messages and not send any response" in {
    val counter = system actorOf Counter.props
    counter! Increment(1)
    counter! Decrement(1)
    expectNoMsg(2 seconds)
  }

}
