import sols.Day
import sols.LongPoint
import kotlin.math.abs


class Day18 : Day("day18.txt") {
    override fun solve1() {
        val arr = splitInput.map { it.split("\\s".toRegex()) }.map { it[0] to it[1].toLong() }
        getArea(arr).also { println(it) }
    }

    override fun solve2() {
        val arr = splitInput.map { it.split("\\s".toRegex()) }
            .map { it[2] }
            .map {
                val (l, r) = it.substring(2, 7) to it.substring(7, 8)
                val hexval = l.toLong(radix = 16)
                val dir = "RDLU"[r.toInt()].toString()
                dir to hexval
            }
        getArea(arr).also { println(it) }
    }

    private fun getArea(arr1: List<Pair<String, Long>>): Long {
        var currpos = 0L to 0L
        val coords = mutableListOf<LongPoint>()

        var border = 0L
        for (entry in arr1) {
            val dir = entry.first
            val steps = entry.second
            currpos = when (dir) {
                "R" -> currpos.first + steps to currpos.second
                "L" -> currpos.first - steps to currpos.second
                "U" -> currpos.first to currpos.second - steps
                "D" -> currpos.first to currpos.second + steps
                else -> throw Exception("Unknown direction")
            }
            coords.add(currpos)
            border += steps
        }
        val shoelaced = shoelace(coords)
        val within = shoelaced - border / 2 + 1
        return within + border
    }


    private fun shoelace(coords: List<LongPoint>): Long {
        var area = 0L
        for (i in 0..<coords.size - 1) {
            val (x1, x2) = coords[i]
            val (y1, y2) = coords[i + 1]
            area += x1 * y2 - x2 * y1
        }
        return (abs(area / 2)).toLong()
    }
}
