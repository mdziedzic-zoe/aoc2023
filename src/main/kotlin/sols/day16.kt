import sols.Day


class Day16 : Day("day16.txt") {
    val nxt = mapOf(
        'R' to Pair(1, 0),
        'L' to Pair(-1, 0),
        'U' to Pair(0, -1),
        'D' to Pair(0, 1),
    )

    override fun solve1() {
        println(getEnergized(Pair(Pair(0, 0), 'R')))
    }

    override fun solve2() {
        val initPoints =
            // top
            (0.rangeUntil(splitInput[0].length)).map { Pair(Pair(it, 0), 'D') } +
                    //bot
                    (0.rangeUntil(splitInput[0].length)).map { Pair(Pair(it, splitInput.size - 1), 'U') } +
                    //left
                    splitInput.indices.map { Pair(Pair(0, it), 'R') } +
                    //right
                    splitInput.indices.map { Pair(Pair(splitInput[0].length - 1, it), 'L') }

        initPoints.map { getEnergized(it) }.max().let {
            println(it)
        }
    }


    private fun getEnergized(init: Pair<Pair<Int, Int>, Char>): Int {
        val q = ArrayDeque<Pair<Pair<Int, Int>, Char>>()
        q.add(init)
        val seen = mutableSetOf<Pair<Pair<Int, Int>, Char>>()

        while (q.isNotEmpty()) {
            val p = q.removeFirst()
            if (p.first.first >= splitInput[0].length || p.first.second >= splitInput.size || p.first.first < 0 || p.first.second < 0) continue
            if (p in seen) continue

            seen.add(p)
            val (x, y) = p.first
            val c = splitInput[y][x]
            when (c) {
                '.' -> {
                    val (dx, dy) = nxt[p.second]!!
                    val np = Pair(x + dx, y + dy)
                    q.add(Pair(np, p.second))
                }

                '/' -> {
                    val ndir = when (p.second) {
                        'R' -> 'U'
                        'L' -> 'D'
                        'U' -> 'R'
                        'D' -> 'L'
                        else -> throw Exception("Invalid direction")
                    }
                    val (dx, dy) = nxt[ndir]!!
                    val np = Pair(x + dx, y + dy)
                    q.add(Pair(np, ndir))
                }

                '\\' -> {
                    val ndir = when (p.second) {
                        'R' -> 'D'
                        'L' -> 'U'
                        'U' -> 'L'
                        'D' -> 'R'
                        else -> throw Exception("Invalid direction")
                    }
                    val (dx, dy) = nxt[ndir]!!
                    val np = Pair(x + dx, y + dy)
                    q.add(Pair(np, ndir))
                }

                '-' -> {
                    if (p.second == 'U' || p.second == 'D') {
                        val np1 = Pair(x + 1, y)
                        val np2 = Pair(x - 1, y)
                        q.add(Pair(np1, 'R'))
                        q.add(Pair(np2, 'L'))
                    } else {
                        val (dx, dy) = nxt[p.second]!!
                        val np = Pair(x + dx, y + dy)
                        q.add(Pair(np, p.second))
                    }
                }

                '|' -> {
                    if (p.second == 'R' || p.second == 'L') {
                        val np1 = Pair(x, y + 1)
                        val np2 = Pair(x, y - 1)
                        q.add(Pair(np1, 'D'))
                        q.add(Pair(np2, 'U'))
                    } else {
                        val (dx, dy) = nxt[p.second]!!
                        val np = Pair(x + dx, y + dy)
                        q.add(Pair(np, p.second))
                    }
                }
            }
        }
        return seen.map { it.first }.toSet().size
    }
}