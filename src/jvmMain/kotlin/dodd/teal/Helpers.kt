package dodd.teal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dodd.teal.Global.whitespaceRegex
import java.io.File

object Helpers {
    fun <T, V> T.letIfNotNull(value: V?, block: (V) -> T) = if (value == null) this else block(value)

    fun File.child(path: String) = File("$absolutePath/$path")

    fun String.splitByWhitespace() = split(whitespaceRegex)

    fun Modifier.fill(color: Color? = null, width: Float = 1.0f, height: Float = 1.0f) = letIfNotNull(color) { background(it) }.fillMaxWidth(width).fillMaxHeight(height)
}
