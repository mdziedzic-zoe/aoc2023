import sols.Day

class Day25 : Day("day25.txt") {
    fun count(v: String, g1: MutableMap<String, MutableSet<String>>, s: MutableSet<String>): Int {
        val difference = g1[v]!!.minus(s)
        return difference.size
    }

    override fun solve1() {
        val g1 = mutableMapOf<String, MutableSet<String>>()

        splitInput.forEach { line ->
            val parts = line.replace(":", "").split(" ").toTypedArray()
            val u = parts[0]
            val vs = parts.slice(1 until parts.size)
            for (v in vs) {
                g1.getOrPut(u) { mutableSetOf() }.add(v)
                g1.getOrPut(v) { mutableSetOf() }.add(u)
            }
        }
        val s = g1.keys.toMutableSet()

        var sumOf = s.sumOf { count(it, g1, s) }
        while (sumOf != 3) {
            val maxKey = s.maxByOrNull { count(it, g1, s) } ?: throw Exception("No max key")
            s.remove(maxKey)
            sumOf = s.sumOf { count(it, g1, s) }
        }
        println(s.size * g1.keys.toMutableSet().filter { !s.contains(it) }.size)
    }

    override fun solve2() {
        println("no part 2!")
    }
}

fun main() {
    Day25().solve()
}