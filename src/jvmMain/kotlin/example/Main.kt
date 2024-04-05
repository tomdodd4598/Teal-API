package example

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dodd.teal.Components.TealGame
import dodd.teal.Config
import dodd.teal.game.Move
import dodd.teal.game.Node
import dodd.teal.game.World

private val config = Config("config.txt")

private val world = World("example", "start/1").apply {
    this["start/1"] = Node(Move.FORWARD to "next/1", Move.LEFT to "start/4", Move.RIGHT to "start/2", Move.DOWN to "start/5")
    this["start/2"] = Node(Move.LEFT to "start/1", Move.RIGHT to "start/3", Move.DOWN to "start/5")
    this["start/3"] = Node(Move.LEFT to "start/2", Move.RIGHT to "start/4", Move.DOWN to "start/5")
    this["start/4"] = Node(Move.LEFT to "start/3", Move.RIGHT to "start/1", Move.DOWN to "start/5")
    this["start/5"] = Node(Move.UP to "start/1")

    this["next/1"] = Node(Move.LEFT to "next/4", Move.RIGHT to "next/2", Move.UP to "next/5")
    this["next/2"] = Node(Move.LEFT to "next/1", Move.RIGHT to "next/3", Move.UP to "next5")
    this["next/3"] = Node(Move.FORWARD to "start/3", Move.LEFT to "next/2", Move.RIGHT to "next/4", Move.UP to "next/5")
    this["next/4"] = Node(Move.LEFT to "next/3", Move.RIGHT to "next/1", Move.UP to "next/5")
    this["next/5"] = Node(Move.DOWN to "next/1")
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Teal Example") {
        TealGame(config, world)
    }
}
