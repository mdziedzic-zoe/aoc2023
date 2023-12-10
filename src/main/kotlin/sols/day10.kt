import sols.Day


class Day10 : Day("day10.txt") {

    val toCheck = mapOf(
        '|' to listOf(0 to -1, 0 to 1),
        '-' to listOf(-1 to 0, 1 to 0),
        'L' to listOf(0 to -1, 1 to 0),
        'J' to listOf(-1 to 0, 0 to -1),
        '7' to listOf(-1 to 0, 0 to 1),
        'F' to listOf(0 to 1, 1 to 0),
        '.' to listOf()
    )


    val pointsDownwards = listOf('|', '7', 'F')
    val pointsUpwards = listOf('|', 'L', 'J')
    val pointsRightwards = listOf('-', 'L', 'F')
    val pointsLeftwards = listOf('-', 'J', '7')

    override fun solve1() {
        val map = splitInput.map { ".$it." }.toMutableList()
        val w = map[0].length
        val h = map.size

        map.add(0, ".".repeat(w))
        map.add(".".repeat(w))
        val (start, sOverride) = getStart(h, w, map)
        val distances = mutableMapOf<Pair<Int, Int>, Int>()
        distances[start] = 0

        val q = ArrayDeque<Pair<Int, Int>>()

        q.add(start)

        while (q.isNotEmpty()) {
            val point = q.removeFirst()
            var c = map[point.second][point.first]
            if (c == 'S') {
                c = sOverride
            }
            for (ndir in toCheck[c]!!) {
                val newPoint = point.first + ndir.first to point.second + ndir.second
                val newDist = distances[point]!! + 1
                if (distances[newPoint] == null || newDist < distances[newPoint]!!) {
                    distances[newPoint] = newDist
                    q.add(newPoint)
                }
            }
        }
        distances.values.max().also { println(it) }
    }

    override fun solve2() {
        val map = splitInput.map { ".$it." }.toMutableList()
        val w = map[0].length
        val h = map.size

        map.add(0, ".".repeat(w))
        map.add(".".repeat(w))
        val (start, sOverride) = getStart(h, w, map)
        val loop = mutableSetOf<Pair<Int, Int>>()
        val q = ArrayDeque<Pair<Int, Int>>()

        q.add(start)

        while (q.isNotEmpty()) {
            val point = q.removeFirst()
            var c = map[point.second][point.first]
            if (c == 'S') {
                c = sOverride
            }
            for (ndir in toCheck[c]!!) {
                val newPoint = point.first + ndir.first to point.second + ndir.second
                if (!loop.contains(newPoint)) {
                    loop.add(newPoint)
                    q.add(newPoint)
                }
            }
        }

        var acc = 0
        val connectedToAbove = mutableListOf('|', 'J', 'L')
        if (sOverride in connectedToAbove) {
            connectedToAbove.add('S')
        }

        for (i in 1..<h - 1) {
            var leftOf = 0
            for (j in 1..<w - 1) {
                val c = map[i][j]
                if (Pair(j, i) in loop) {
                    if (c in connectedToAbove) {
                        leftOf++
                    }
                } else {
                    acc += if (leftOf % 2 == 1) 1 else 0
                }
            }
        }
        println(acc)

    }

    fun getStart(
        h: Int,
        w: Int,
        map: MutableList<String>
    ): Pair<Pair<Int, Int>, Char> {
        var start: Pair<Int, Int>? = null
        var sOverride: Char? = null

        for (i in 1..<w - 1) {
            for (j in 1..<h - 1) {
                if (map[j][i] == 'S') {
                    start = i to j

                    if (map[j - 1][i] in pointsDownwards && map[j + 1][i] in pointsUpwards) sOverride = '|'
                    else if (map[j][i - 1] in pointsRightwards && map[j][i + 1] in pointsLeftwards) sOverride = '-'
                    else if (map[j - 1][i] in pointsDownwards && map[j][i + 1] in pointsLeftwards) sOverride = 'L'
                    else if (map[j][i - 1] in pointsRightwards && map[j - 1][i] in pointsDownwards) sOverride = 'J'
                    else if (map[j][i - 1] in pointsRightwards && map[j + 1][i] in pointsUpwards) sOverride = '7'
                    else if (map[j][i + 1] in pointsLeftwards && map[j + 1][i] in pointsUpwards) sOverride = 'F'
                }
            }
        }
        return Pair(start!!, sOverride!!)
    }

}
