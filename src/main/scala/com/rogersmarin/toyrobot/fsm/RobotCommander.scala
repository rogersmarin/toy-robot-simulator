package com.rogersmarin.toyrobot.fsm

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import com.rogersmarin.toyrobot.exceptions.InvalidCommandException
import com.rogersmarin.toyrobot.models._

import scala.concurrent.Await
import scala.util.{Failure, Success, Try}

/**
  * Used to submit arbitrary String commands to the [[RobotMachine]] FSM
  * @param robotMachine the [[RobotMachine]] FSM [[akka.actor.ActorRef]]
  */
case class RobotCommander(robotMachine:ActorRef) extends Displayable {

  implicit val timeout = akka.util.Timeout(5, TimeUnit.SECONDS)
  val parser = CommandParser

  def execute(command:String) = {
    val parsedCommand = Try(parser.parse(command))
    parsedCommand match {
      case Success(parsed) => {
        val result = robotMachine ? parsed
        val response = Await.result(result, timeout.duration)
        response match {
          case EmptyResponse => //Unhandled
          case InvalidCommand => error(s"Ignoring Invalid command: ${command}")
          case RobotNotPlaced => error(s"Ignoring command: ${command}, Robot not placed")
          case robot:Robot => display(robot.toString())
        }
      }
      case Failure(parsed) => {
        parsed match {
          case e:InvalidCommandException => error(e.message)
        }
      }
    }
  }
}
