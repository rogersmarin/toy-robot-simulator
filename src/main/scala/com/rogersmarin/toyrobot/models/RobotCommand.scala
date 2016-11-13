package com.rogersmarin.toyrobot.models

sealed trait Command
case class PlaceCommand(coord:Coordinate, orientation: Orientation){
  override def toString():String = s"${coord},${orientation}"
}
case object Left extends Command {
  override def toString():String = "LEFT"
}
case object Right extends Command {
  override def toString():String = "RIGHT"
}
case object Move extends Command {
  override def toString():String = "MOVE"
}
case object Report extends Command {
  override def toString():String = "REPORT"
}
case class Place(command:PlaceCommand) extends Command {
  override def toString():String = command.toString
}

case object InvalidCommand
case object RobotNotPlaced
case object EmptyResponse
