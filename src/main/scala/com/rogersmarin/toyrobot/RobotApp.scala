package com.rogersmarin.toyrobot

import akka.actor.{ActorSystem, Props}
import com.rogersmarin.toyrobot.fsm.{RobotCommander, RobotMachine}
import com.rogersmarin.toyrobot.models.{Board, Displayable}

import scala.io.Source
import scala.util.{Failure, Success, Try}

object RobotApp extends Displayable {

  val robotMachine = ActorSystem("ToyRobot").actorOf(Props(new RobotMachine(new Board(5,5))))
  val commander = new RobotCommander(robotMachine)

  def main(args: Array[String]): Unit = {
    var fileName = "/test.txt"
    val fileLines:Try[Iterator[String]] = args.length match {
      case 0 => {
        display("No file provided, using default test file: test.txt")
        Try(getClass.getResourceAsStream(fileName)).map(scala.io.Source.fromInputStream).flatMap(buffered => Try(buffered.getLines()))
      }
      case _ => {
        fileName = args(0)
        Try(Source.fromFile(fileName)).flatMap(buffered => Try(buffered.getLines()))
      }
    }


    fileLines match {
      case Success(commands) => {
        commands.foreach(commander.execute(_))
      }
      case Failure(e) => {
        error(s"Error reading file: ${fileName} caused by: ${e.getMessage}")
        System.exit(-1)
      }
    }
    System.exit(1)
  }
}
