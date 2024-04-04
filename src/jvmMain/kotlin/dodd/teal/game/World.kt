package dodd.teal.game

class World(val id: String, private val start: String, val ext: String = "png") {

    private val internal = mutableMapOf<String, Node>()

    fun start() = this[start]

    operator fun get(id: String) = internal[id]!!

    operator fun set(id: String, node: Node) {
        internal[id] = node
        node.world = this
        node.id = id
    }
}
