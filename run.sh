#!/usr/bin/bash

sbt assembly

java -jar toy-robot-simulator.jar $@