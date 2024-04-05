package dodd.teal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dodd.teal.Helpers.fill
import dodd.teal.game.Move
import dodd.teal.game.World

object Components {

    class Settings(val speed: Float, val volume: Float)

    @Composable
    fun TealGame(config: Config, world: World, content: @Composable BoxScope.(Settings) -> Unit = { TealInterface(world, it) }) {
        Box {
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

            content(Settings(speed, volume))
        }
    }

    @Composable
    fun TealInterface(world: World, settings: Settings, content: @Composable BoxScope.() -> Unit = {}) {

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
                moveTime += settings.speed * 1.0f
                if (moveTime >= Global.MAX_MOVE_TIME) {
                    finishMove()
                }
            }

            if (moveTime >= 0.0f) {
                if (moveType == Move.FORWARD) {
                    node.image(modifier = Modifier.fill(color = Color.Black))
                    next.image(alpha = moveTime / Global.MAX_MOVE_TIME)
                }
                else {
                    // Currently a hack for rotating to next node, not resolution-independent
                    val mult = 35.0f
                    val bias = mult * moveTime / Global.MAX_MOVE_TIME
                    val factors = moveType.biasFactors()
                    node.image(modifier = Modifier.fill(color = Color.Black), alignment = BiasAlignment(bias * factors.first, bias * factors.second))
                    next.image(modifier = Modifier.fill(), alignment = BiasAlignment((bias - mult) * factors.first, (bias - mult) * factors.second))
                }
            }
            else {
                node.image(modifier = Modifier.fill(color = Color.Black))
            }

            content()
        }
    }
}
