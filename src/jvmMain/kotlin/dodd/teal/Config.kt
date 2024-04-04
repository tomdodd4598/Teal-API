package dodd.teal

import java.io.File
import kotlin.reflect.KProperty

class Config(path: String, vararg defaults: Pair<String, String>) {

    private val file: File
    private val fallback = defaults.toMap()
    private val indexed = mutableListOf<Pair<String, String>>()
    private val internal = mutableMapOf<String, Pair<Int, String>>()

    init {
        file = File(path)
        if (!file.isFile) {
            file.createNewFile()
        }

        val defined = mutableSetOf<String>()

        file.forEachLine { line ->
            val split = line.split("=", limit = 2).map { it.trim() }
            if (split.size == 2) {
                val (key, value) = split
                if (key in fallback) {
                    indexed.add(Pair(key, value))
                    defined.add(key)
                }
            }
        }

        for ((key, value) in defaults) {
            if (!defined.contains(key)) {
                indexed.add(Pair(key, value))
            }
        }

        val sb = StringBuilder()
        indexed.forEachIndexed { index, (key, value) ->
            internal[key] = Pair(index, value)
            sb.append("$key=$value\n")
        }
        file.writeText(sb.toString())
    }

    operator fun get(key: String) = internal[key]!!.second

    operator fun set(key: String, value: String) {
        val (index) = internal[key]!!
        indexed[index] = Pair(key, value)
        internal[key] = Pair(index, value)

        val sb = StringBuilder()
        for ((k, v) in indexed) {
            sb.append("$k=$v\n")
        }
        file.writeText(sb.toString())
    }

    fun default(key: String) = fallback[key]!!

    inner class Delegate<T>(private val key: String, private val fromString: (String) -> T) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = fromString(this@Config[key])

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            this@Config[key] = value.toString()
        }
    }

    private fun <T> delegate(key: String, fromString: (String) -> T) = Delegate(key, fromString)

    fun delegate(key: String) = delegate(key) { it }
}
