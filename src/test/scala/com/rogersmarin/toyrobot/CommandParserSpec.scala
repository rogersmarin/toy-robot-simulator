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
      var thrown = intercept[InvalidCommandException] { parser.parse("MOVE LEFT") }
      assert(thrown.message === "Unable to parse command: MOVE LEFT")
      thrown = intercept[InvalidCommandException] { parser.parse("MOVE RIGHT") }
      assert(thrown.message === "Unable to parse command: MOVE RIGHT")
      thrown = intercept[InvalidCommandException] { parser.parse("STOP") }
      assert(thrown.message === "Unable to parse command: STOP")
      thrown = intercept[InvalidCommandException] { parser.parse("GO") }
      assert(thrown.message === "Unable to parse command: GO")
      thrown = intercept[InvalidCommandException] { parser.parse("move") }
      assert(thrown.message === "Unable to parse command: move")
      thrown = intercept[InvalidCommandException] { parser.parse("left") }
      assert(thrown.message === "Unable to parse command: left")
      thrown = intercept[InvalidCommandException] { parser.parse("right") }
      assert(thrown.message === "Unable to parse command: right")
      thrown = intercept[InvalidCommandException] { parser.parse("report") }
      assert(thrown.message === "Unable to parse command: report")
      thrown = intercept[InvalidCommandException] { parser.parse("PLACE") }
      assert(thrown.message === "Unable to parse command: PLACE")
      thrown = intercept[InvalidCommandException] { parser.parse("PLACE 2") }
      assert(thrown.message === "Unable to parse command: PLACE 2")
      thrown = intercept[InvalidCommandException] { parser.parse("PLACE SOUTHWEST") }
      assert(thrown.message === "Unable to parse command: PLACE SOUTHWEST")
      thrown = intercept[InvalidCommandException] { parser.parse("PLACE SOUTH") }
      assert(thrown.message === "Unable to parse command: PLACE SOUTH")
      thrown = intercept[InvalidCommandException] { parser.parse("PLACE 2,SOUTH") }
      assert(thrown.message === "Unable to parse command: PLACE 2,SOUTH")
      thrown = intercept[InvalidCommandException] { parser.parse("PLACE 2,SOUTHWEST") }
      assert(thrown.message === "Unable to parse command: PLACE 2,SOUTHWEST")
      thrown = intercept[InvalidCommandException] { parser.parse("PLACE 2,2,SOUTHWEST") }
      assert(thrown.message === "Unable to parse command: PLACE 2,2,SOUTHWEST")
      thrown = intercept[InvalidCommandException] { parser.parse("place 2,2,NORTH") }
      assert(thrown.message === "Unable to parse command: place 2,2,NORTH")
      thrown = intercept[InvalidCommandException] { parser.parse("") }
      assert(thrown.message === "Unable to parse command: ")
    }
  }
}
