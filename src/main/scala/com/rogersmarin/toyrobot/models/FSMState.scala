package com.rogersmarin.toyrobot.models

sealed trait State
case object NotPlaced extends State
case object Placed extends State

