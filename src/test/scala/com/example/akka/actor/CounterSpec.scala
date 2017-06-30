package com.example.akka.actor

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.example.akka.actor.Counter.{Decrement, Increment}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Unit test for Counter Actor
  */
class CounterSpec extends TestKit(ActorSystem("test-system")) with ImplicitSender
  with FlatSpecLike with  Matchers with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Counter Actor" should "handle Increment and Decrement messages and not send any response" in {
    val counter = system.actorOf(Counter.props)
    counter! Increment(1)
    counter! Decrement(1)
    expectNoMsg(2 seconds)
  }

}
