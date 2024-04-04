package dodd.teal.game

enum class Move {
    FORWARD, LEFT, RIGHT, UP, DOWN;

    fun biasFactors() = when (this) {
        FORWARD -> Pair(0.0f, 0.0f)
        LEFT -> Pair(1.0f, 0.0f)
        RIGHT -> Pair(-1.0f, 0.0f)
        UP -> Pair(0.0f, 1.0f)
        DOWN -> Pair(0.0f, -1.0f)
    }
}
