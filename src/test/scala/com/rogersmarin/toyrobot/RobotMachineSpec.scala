package com.rogersmarin.toyrobot

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestFSMRef, TestKit}
import com.rogersmarin.toyrobot.fsm.RobotMachine
import com.rogersmarin.toyrobot.models._
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.Await

class RobotMachineSpec(as: ActorSystem) extends TestKit(as)
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {


  def this() = this(ActorSystem("testActorSystem"))
  implicit val timeout = akka.util.Timeout(5, TimeUnit.SECONDS)

  "A RobotMachine" must {
    "Not be placed in the board at initial state" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      assert(robotMachine.stateName == NotPlaced)
    }

    "Not be placed in the board when given an invalid X coordinate" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      val result = robotMachine ? Place(PlaceCommand(Coordinate(6,0),North))
      val response = Await.result(result, timeout.duration)
      assert(response.equals(InvalidCommand))
    }

    "Robot should not be placed in the board when given an invalid Y coordinate" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      val result = robotMachine ? Place(PlaceCommand(Coordinate(0,6),North))
      val response = Await.result(result, timeout.duration)
      assert(response.equals(InvalidCommand))
    }

    "Not be placed in the board when given invalid X and Y coordinates" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      val result = robotMachine ? Place(PlaceCommand(Coordinate(6,6),North))
      val response = Await.result(result, timeout.duration)
      assert(response.equals(InvalidCommand))
      assert(robotMachine.stateName == NotPlaced)
      assert(robotMachine.stateData == None)
    }

    "Not be placed in the board when given an invalid placement" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      val result = robotMachine ? Place(PlaceCommand(Coordinate(-1,-1),North))
      val response = Await.result(result, timeout.duration)
      assert(response.equals(InvalidCommand))
    }

    "Produce expected output for a valid set of instructions" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      robotMachine ! Place(PlaceCommand(Coordinate(1,2),East))
      robotMachine ! Move
      robotMachine ! Move
      robotMachine ! Left
      robotMachine ! Move
      var result = robotMachine ? Report
      var response = Await.result(result, timeout.duration)
      assert(response.toString.equals("3,3,NORTH"))

      robotMachine ! Place(PlaceCommand(Coordinate(0,0),North))
      robotMachine ! Move
      result = robotMachine ? Report
      response = Await.result(result, timeout.duration)
      assert(response.toString.equals("0,1,NORTH"))


      robotMachine ! Place(PlaceCommand(Coordinate(0,0),North))
      robotMachine ! Left
      result = robotMachine ? Report
      response = Await.result(result, timeout.duration)
      assert(response.toString.equals("0,0,WEST"))

    }

    "Ignore invalid instructions" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      robotMachine ! Place(PlaceCommand(Coordinate(1,2),East))
      robotMachine ! Move
      robotMachine ! Move
      robotMachine ! Left
      robotMachine ! Move
      var result = robotMachine ? Report
      var response = Await.result(result, timeout.duration)
      assert(response.toString.equals("3,3,NORTH"))

      result = robotMachine ? Place(PlaceCommand(Coordinate(-1,-12),East))
      response = Await.result(result, timeout.duration)
      assert(response.equals(InvalidCommand))

      result = robotMachine ? Report
      response = Await.result(result, timeout.duration)
      assert(response.toString.equals("3,3,NORTH"))
    }

    "Ignore all commands until a valid place command has been issued" in {
      val robotMachine = TestFSMRef(new RobotMachine(new Board(5,5)))
      robotMachine ! Move
      robotMachine ! Move
      robotMachine ! Left
      robotMachine ! Move
      var result = robotMachine ? Report
      var response = Await.result(result, timeout.duration)
      assert(response.equals(RobotNotPlaced))

      result = robotMachine ? Place(PlaceCommand(Coordinate(-1,-12),East))
      response = Await.result(result, timeout.duration)
      assert(response.equals(InvalidCommand))

      robotMachine ! Place(PlaceCommand(Coordinate(1,2),East))
      robotMachine ! Move
      robotMachine ! Move
      robotMachine ! Left
      robotMachine ! Move
      result = robotMachine ? Report
      response = Await.result(result, timeout.duration)
      assert(response.toString.equals("3,3,NORTH"))
    }
  }
}