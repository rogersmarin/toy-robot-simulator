package com.rogersmarin.toyrobot.fsm

import akka.actor.{Actor, FSM}
import com.rogersmarin.toyrobot.models._

/**
  * An Akka Finite State Machine [[FSM]] implementation of a Robot.
  * The FSM handles 2 [[State]] types [[Placed]] & [[NotPlaced]] and 5 different [[Command]] types [[Place]],[[Left]],[[Right]],[[Move]],[[Report]]
  * @param board the [[Board]] where the robot will be placed
  */
class RobotMachine(board: Board) extends Actor with FSM[State, Option[Robot]] {

  val orientations = List(North,West,South,East)

  startWith(NotPlaced, None)

  when(NotPlaced){
    case Event(Place(data),_) => {
      if(!board.validate(data.coord)){
        stay replying InvalidCommand
      }else {
        goto(Placed) using Some(new Robot(data.coord, data.orientation)) replying EmptyResponse
      }
    }
    case Event(_,_) => stay replying RobotNotPlaced
  }

  when(Placed){
    case Event(Place(data),robot) => {
      if(!board.validate(data.coord)){
        stay using robot replying InvalidCommand
      }else {
        stay using Some(new Robot(data.coord, data.orientation)) replying EmptyResponse
      }
    }

    case Event(Move, robot) => {
      val coord = robot.get.orientation.move(robot.get.coord)
      if(!board.validate(coord)) {
        stay using robot replying InvalidCommand
      }else{
        stay using Some(new Robot(coord, robot.get.orientation)) replying EmptyResponse
      }
    }

    case Event(Left,robot) => {
      val idx = (orientations.indexOf(robot.get.orientation) + 1) % orientations.size
      stay using Some(new Robot(robot.get.coord,orientations(idx))) replying EmptyResponse
    }

    case Event(Right,robot) => {
      val idx = (orientations.indexOf(robot.get.orientation) - 1) % orientations.size
      stay using Some(new Robot(robot.get.coord,orientations(idx))) replying EmptyResponse
    }

    case Event(Report,robot) => stay using robot replying robot.get
  }

  initialize
}