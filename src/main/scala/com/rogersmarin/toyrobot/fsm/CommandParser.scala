package com.rogersmarin.toyrobot.fsm


import com.rogersmarin.toyrobot.exceptions.InvalidCommandException
import com.rogersmarin.toyrobot.models._


/**
  * Object that allows parsing of an arbitrary string into a [[com.rogersmarin.toyrobot.models.Command]] object.
  * @throws InvalidCommandException
  */
object CommandParser {

  //The following regular expression ommits the first two capture groups (?:(?:)), we are only interested in the x,y and the Orientation for a PLACE command
  val commandPattern = """^(?:(?:PLACE (\d),(\d),(NORTH|SOUTH|EAST|WEST))|MOVE|LEFT|RIGHT|REPORT)$""".r

  def parse(command:String):Command = {
    command match{
      case commandPattern(x,y,o) => {
        command match {
          case "MOVE"   =>  Move
          case "LEFT"   =>  Left
          case "RIGHT"  =>  Right
          case "REPORT" =>  Report
          case _        => {
            val orientation = o match {
              case "NORTH" => North
              case "SOUTH" => South
              case "EAST"  => East
              case "WEST"  => West
            }
            Place(PlaceCommand(Coordinate(x.toInt,y.toInt),orientation))
          }
        }
      }
      case _ => throw new InvalidCommandException(s"Unable to parse command: ${command}")
    }
  }
}
