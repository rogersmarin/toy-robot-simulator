package com.rogersmarin.toyrobot.models

sealed trait Orientation {
  def move(coord:Coordinate):Coordinate
}
case object North extends Orientation {
  override def move(coord:Coordinate):Coordinate = Coordinate(coord.x, coord.y+1)
  override def toString():String = "NORTH"
}
case object South extends Orientation{
  override def move(coord:Coordinate):Coordinate = Coordinate(coord.x, coord.y-1)
  override def toString():String = "SOUTH"
}
case object East extends Orientation {
  override def move(coord:Coordinate):Coordinate = Coordinate(coord.x+1, coord.y)
  override def toString():String = "EAST"
}
case object West extends Orientation {
  override def move(coord:Coordinate):Coordinate = Coordinate(coord.x-1, coord.y)
  override def toString():String = "WEST"
}
