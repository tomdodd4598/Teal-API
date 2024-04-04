package example

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.*
import dodd.teal.Config
import dodd.teal.Global.MAX_MOVE_TIME
import dodd.teal.Helpers.fill
import dodd.teal.game.Move
import dodd.teal.game.Node
import dodd.teal.game.World

private val config = Config("config.txt", Pair("speed", "1.0"), Pair("volume", "1.0"))

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

@Composable
fun App(root: ApplicationScope) {

    // INIT

    var speedProperty by config.delegate("speed")
    var volumeProperty by config.delegate("volume")

    if (speedProperty.toFloatOrNull().let { it == null || it < 0.0f || it > 1.0f }) {
        speedProperty = config.default("speed")
    }

    if (volumeProperty.toFloatOrNull().let { it == null || it < 0.0f || it > 1.0f }) {
        volumeProperty = config.default("volume")
    }

    val speed by remember { derivedStateOf { speedProperty.toFloat() } }
    val volume by remember { derivedStateOf { volumeProperty.toFloat() } }

    // MAIN

    var node by remember { mutableStateOf(world.start()) }
    var next by remember { mutableStateOf(world.start()) }

    var moveTime by remember { mutableStateOf(0.0f) }
    var moveType by remember { mutableStateOf(Move.FORWARD) }

    fun finishMove() {
        moveTime = -1.0f
        node = next
    }

    fun clickMove(move: Move) {
        node[move]?.let {
            finishMove()
            next = it
            moveTime = 0.0f
            moveType = move
        }
    }

    Box(modifier = Modifier.fill()) {
        Row {
            Box(modifier = Modifier.fill(width = 0.2f).clickable { clickMove(Move.LEFT) })
            Column(modifier = Modifier.fill(width = 0.75f)) {
                Box(modifier = Modifier.fill(height = 0.2f).clickable { clickMove(Move.UP) })
                Box(modifier = Modifier.fill(height = 0.75f).clickable { clickMove(Move.FORWARD) })
                Box(modifier = Modifier.fill().clickable { clickMove(Move.DOWN) })
            }
            Box(modifier = Modifier.fill().clickable { clickMove(Move.RIGHT) })
        }

        if (moveTime >= 0.0f) {
            moveTime += speed * 1.0f
            if (moveTime >= MAX_MOVE_TIME) {
                finishMove()
            }
        }

        if (moveTime >= 0.0f) {
            if (moveType == Move.FORWARD) {
                node.image(modifier = Modifier.fill(color = Color.Black))
                next.image(alpha = moveTime / MAX_MOVE_TIME)
            }
            else {
                // Currently a hack for rotating to next node
                val mult = 35.0f
                val bias = mult * moveTime / MAX_MOVE_TIME
                val factors = moveType.biasFactors()
                node.image(modifier = Modifier.fill(color = Color.Black), alignment = BiasAlignment(bias * factors.first, bias * factors.second))
                next.image(modifier = Modifier.fill(), alignment = BiasAlignment((bias - mult) * factors.first, (bias - mult) * factors.second))
            }
        }
        else {
            node.image(modifier = Modifier.fill(color = Color.Black))
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Teal Example") {
        App(this@application)
    }
}
