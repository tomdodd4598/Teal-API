package dodd.teal.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.res.painterResource
import dodd.teal.Helpers.fill

class Node(vararg moves: Pair<Move, String>) {

    private val moves = moves.toMap()

    lateinit var world: World
    lateinit var id: String

    @Composable
    fun resource() = painterResource("nodes/$id.${world.ext}")

    @Composable
    fun image(modifier: Modifier = Modifier.fill(), alignment: Alignment = Alignment.Center, alpha: Float = DefaultAlpha) = Image(
        painter = resource(),
        contentDescription = "${world.id}/node/$id",
        modifier = modifier,
        alignment = alignment,
        alpha = alpha,
    )

    fun jump(id: String) = world[id]

    operator fun get(dir: Move) = moves[dir]?.let { jump(it) }
}
