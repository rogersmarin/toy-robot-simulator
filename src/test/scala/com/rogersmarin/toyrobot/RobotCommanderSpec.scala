package com.rogersmarin.toyrobot

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestFSMRef, TestKit}
import com.rogersmarin.toyrobot.fsm.{RobotCommander, RobotMachine}
import com.rogersmarin.toyrobot.models.{Board, Displayable}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}


trait MockDisplayable extends Displayable {
    var messages: Seq[String] = Seq()
    var errors: Seq[String]   = Seq()
    override def display(message: String) = messages = messages :+ message
    override def error(error: String)     = errors   = errors   :+ error
}

class RobotCommanderSpec(ac: ActorSystem) extends TestKit(ac)
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {


  val board = new Board(5,5)
  def this() = this(ActorSystem("testActorSystem"))

  "A RobotCommander" must {
    "Accept a valid set of commands and produce the expected output" in {
      val robotCommander = new RobotCommander(TestFSMRef(new RobotMachine(board))) with MockDisplayable
      robotCommander.execute("PLACE 1,2,EAST")
      robotCommander.execute("MOVE")
      robotCommander.execute("MOVE")
      robotCommander.execute("LEFT")
      robotCommander.execute("MOVE")
      robotCommander.execute("REPORT")
      robotCommander.messages should contain("3,3,NORTH")

      robotCommander.execute("PLACE 0,0,NORTH")
      robotCommander.execute("MOVE")
      robotCommander.execute("REPORT")
      robotCommander.messages should contain("0,1,NORTH")

      robotCommander.execute("PLACE 0,0,NORTH")
      robotCommander.execute("LEFT")
      robotCommander.execute("REPORT")
      robotCommander.messages should contain("0,0,WEST")
    }

    "Ignore all commands until a valid PLACE command has been issued" in {
      val robotCommander = new RobotCommander(TestFSMRef(new RobotMachine(board))) with MockDisplayable
      robotCommander.execute("MOVE")
      robotCommander.execute("MOVE")
      robotCommander.execute("LEFT")
      robotCommander.execute("MOVE")
      robotCommander.execute("REPORT")
      robotCommander.errors should contain("Ignoring command: MOVE, Robot not placed")

      robotCommander.execute("PLACE -1,-12,EAST")
      robotCommander.errors should contain("Unable to parse command: PLACE -1,-12,EAST")

      robotCommander.execute("PLACE 1,2,EAST")
      robotCommander.execute("MOVE")
      robotCommander.execute("MOVE")
      robotCommander.execute("LEFT")
      robotCommander.execute("MOVE")
      robotCommander.execute("REPORT")
      robotCommander.messages should contain("3,3,NORTH")
    }
  }
}
