package com.rogersmarin.toyrobot.models


/**
  * This trait represents a [[Displayable]] object that supports output to both stdout and err
  */
trait Displayable {
  def display(message:String) = Console.println(message)
  def error(message:String)   = Console.err.println(message)
}

case class Coordinate(x:Int, y:Int) {
  override def toString():String = s"${x},${y}"
}

case class Board(x:Int, y:Int) {
  def validate(coord:Coordinate): Boolean = {
    return (coord.x >= 0 && coord.y>=0 && coord.x <= x && coord.y <= y)
  }
}

case class Robot(coord:Coordinate, orientation:Orientation) {
  override def toString():String = s"${coord},${orientation}"
}