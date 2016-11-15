package com.rogersmarin.toyrobot

import com.rogersmarin.toyrobot.fsm.CommandParser
import com.rogersmarin.toyrobot.exceptions.InvalidCommandException
import com.rogersmarin.toyrobot.models._
import org.scalatest.{Matchers, WordSpec}

class CommandParserSpec extends WordSpec with Matchers{

  val parser = CommandParser

  "A CommandParser" must {
    "Parse valid commands" in {
      parser.parse("MOVE") shouldBe Move
      parser.parse("LEFT") shouldBe Left
      parser.parse("RIGHT") shouldBe Right
      parser.parse("REPORT") shouldBe Report
      val placeCommand = Place(PlaceCommand(Coordinate(0,0),North))
      parser.parse("PLACE 0,0,NORTH") shouldBe placeCommand
    }

    "Throw an InvalidCommandException when attempting to parse invalid commands" in {
      val invalidCommands = List("MOVE LEFT","MOVE RIGHT","STOP", "GO", "move",
                                 "left", "right", "report", "PLACE", "PLACE 2",
                                 "PLACE SOUTHWEST", "PLACE SOUTH", "PLACE 2,SOUTH", "PLACE 2,SOUTHWEST",
                                 "PLACE 2,2,SOUTHWEST", "place 2,2,NORTH", "")
      invalidCommands.foreach(command=> {
        val thrown = intercept[InvalidCommandException] { parser.parse(command) }
        assert(thrown.message === s"Unable to parse command: ${command}")
      })
    }
  }
}
