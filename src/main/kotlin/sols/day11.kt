import sols.Day
import kotlin.math.abs


class Day11 : Day("day11.txt") {
    override fun solve1() {
        val (yEmpty, xEmpty) = getEmptyLines()

        val galaxies = (0..<splitInput.size).flatMap { y ->
            (0..<splitInput[0].length).map { x ->
                if (splitInput[y][x] == '#') {
                    (x + xEmpty.filter { it < x }.size).toLong() to y + (yEmpty.filter { it < y }.size).toLong()
                } else {
                    null
                }
            }
        }.filterNotNull()

        getDists(galaxies).also { println(it) }

    }

    override fun solve2() {
        val (yEmpty, xEmpty) = getEmptyLines()
        val mul = 1_000_000L
        val galaxies = (0..<splitInput.size).flatMap { y ->
            (0..<splitInput[0].length).map { x ->
                if (splitInput[y][x] == '#') {
                    Pair(
                        x.toLong() + (mul - 1) * xEmpty.filter { it < x }.size.toLong(),
                        y.toLong() + (mul - 1) * yEmpty.filter { it < y }.size.toLong()
                    )
                } else {
                    null
                }
            }
        }.filterNotNull()

        getDists(galaxies).also { println(it) }
    }

    fun getDists(galaxies: List<Pair<Long, Long>>): Long {
        val dists: MutableMap<Pair<Long, Long>, List<Long>> = mutableMapOf()

        for (i in galaxies.indices) {
            val l = mutableListOf<Long>()
            for (j in galaxies.indices) {
                if (i == j) continue
                val g1 = galaxies[i]
                val g2 = galaxies[j]
                val score = manhattanDist(g1.first, g1.second, g2.first, g2.second)
                l.add(score)
            }
            dists[galaxies[i]] = l
        }
        return dists.flatMap { it.value }.sum() / 2L
    }

    fun getEmptyLines(): Pair<List<Int>, List<Int>> {
        val yEmpty = splitInput.mapIndexed { idx, line ->
            if (line.all { it == '.' }) idx else null
        }.mapNotNull { it }

        val xEmpty = (0..<splitInput[0].length).map { y ->
            splitInput.indices.map { x ->
                splitInput[x][y]
            } to y
        }.filter { it.first.all { it == '.' } }
            .map { it.second }
        return Pair(yEmpty, xEmpty)
    }

    fun manhattanDist(x1: Long, y1: Long, x2: Long, y2: Long) = abs(x1 - x2) + abs(y1 - y2)
}
