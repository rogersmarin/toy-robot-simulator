Toy Robot Simulator
===================

A Scala + Akka implementation of the Toy Robot application detailed [here](PROBLEM.md).

Description
-----------

The application uses an [Akka Finite State Machine](http://doc.akka.io/docs/akka/snapshot/scala/fsm.html) to represent a Robot that can be issued commands.

2 States have been defined: Placed & NotPlaced that support the 5 different commands that can be issued as per the problem description (Move, Left, Right, Report & Place).


A Placed robot is able to handle the following events:

* Move: Modifies the state of the Robot by moving it 1 unit in the direction it is facing.
* Left: Modifies the state of the Robot by rotating 90 degrees the orientation of the robot to the Left.
* Right: Modifies the state of the Robot by rotating 90 degrees the orientation of the robot to the Right.
* Place: Places the robot on the board using the specified x,y coordinates and orientation. This event triggers a state change from NotPlaced to Placed.
* Report: This event has no side-effects, actor replies using the current Robot object.

### Finite State Machine Diagram: 

![RobotMachine FSM Diagram](fsm.png)


Unit Tests
----------

3 different test suites have been provided that cover the Robot FSM implementation as well as the Command Parser

* CommandParserSpec: Spec that covers command parsing from arbitrary string commands.
* RobotCommanderSpec: Spec that covers the RobotCommander abstraction that allows issuing String commands to the Robot FSM.
* RobotMachineSpec: Spec that covers the Robot FSM.

Building and Running the application
------------------------------------

* Scala 2.11 and SBT are required to build and package the application.
* Java 8 is required to execute the application.

To build, run the tests and execute the application execute the [run.sh](run.sh) script, it expects an (optional) valid file path as a parameter.

A default [test file](src/main/resources/test.txt) has been bundled with the application, if not given a file path parameter it will execute using this file.





