import sols.Day


class Day14 : Day("day14.txt") {
    override fun solve1() {
        val (ovals, cubes) = parse()
        var acc = 0
        for (x in 0..splitInput[0].length) {
            val or = ovals.filter { it.first == x }.map { "O" to it }
            val cr = cubes.filter { it.first == x }.map { "R" to it }

            val t = (or + cr).sortedBy { it.second.second }

            var p = splitInput.size
            var local = 0
            for (i in 0..<t.size) {
                if (t[i].first == "O") {
                    local += p
                    p--
                } else {
                    p = splitInput.size - t[i].second.second - 1
                }
            }
            acc += local
        }
        println(acc)
    }

    override fun solve2() {
        val h = splitInput.size
        val w = splitInput[0].length

        var (ovals, cubes) = parse()

        var cycle = 1L
        val totalCycles = 1000000000L

        val seenCycles: MutableMap<String, Long> = mutableMapOf()
        var cycleNotFound = true
        var mod = 0L

        while (cycle < totalCycles) {

            // north
            var nxtOvals = mutableListOf<Pair<Int, Int>>()

            for (x in 0..<w) {
                val or = ovals.filter { it.first == x }.map { "O" to it }
                val cr = cubes.filter { it.first == x }.map { "R" to it }

                val t = (or + cr).sortedBy { it.second.second }

                var p = 0
                for (i in 0..<t.size) {
                    if (t[i].first == "O") {
                        nxtOvals.add(Pair(x, p))
                        p++
                    } else {
                        p = t[i].second.second + 1
                    }
                }
            }

            ovals = nxtOvals
            nxtOvals = mutableListOf()

            // west
            for (y in 0..<h) {
                val or = ovals.filter { it.second == y }.map { "O" to it }
                val cr = cubes.filter { it.second == y }.map { "R" to it }

                val t = (or + cr).sortedBy { it.second.first }

                var p = 0
                for (i in 0..<t.size) {
                    if (t[i].first == "O") {
                        nxtOvals.add(Pair(p, y))
                        p++
                    } else {
                        p = t[i].second.first + 1
                    }
                }
            }
            ovals = nxtOvals
            nxtOvals = mutableListOf()

            // south
            for (x in 0..<w) {
                val or = ovals.filter { it.first == x }.map { "O" to it }
                val cr = cubes.filter { it.first == x }.map { "R" to it }

                val t = (or + cr).sortedByDescending { it.second.second }

                var p = h - 1
                for (i in 0..<t.size) {
                    if (t[i].first == "O") {
                        nxtOvals.add(Pair(x, p))
                        p--
                    } else {
                        p = t[i].second.second - 1
                    }
                }
            }

            ovals = nxtOvals

            nxtOvals = mutableListOf()

            // east
            for (y in 0..<h) {
                val or = ovals.filter { it.second == y }.map { "O" to it }
                val cr = cubes.filter { it.second == y }.map { "R" to it }

                val t = (or + cr).sortedByDescending { it.second.first }

                var p = w - 1
                for (i in 0..<t.size) {
                    if (t[i].first == "O") {
                        nxtOvals.add(Pair(p, y))
                        p--
                    } else {
                        p = t[i].second.first - 1
                    }
                }
            }
            ovals = nxtOvals

            val key = enc(ovals, cubes)
            if (seenCycles.contains(key) && cycleNotFound) {
                mod = cycle - seenCycles[key]!!
                cycleNotFound = false
            }
            if (cycleNotFound) {
                seenCycles[key] = cycle
            } else {
                if (cycle % mod == totalCycles % mod) {
                    println(weight(ovals, h))
                    return
                }
            }
            cycle++
        }
    }


    fun parse(): Pair<MutableList<Pair<Int, Int>>, MutableList<Pair<Int, Int>>> {
        val ovals = mutableListOf<Pair<Int, Int>>()
        val cubes = mutableListOf<Pair<Int, Int>>()
        (0..<splitInput.size).forEach { y ->
            (0..<splitInput[0].length).forEach { x ->
                if (splitInput[y][x] == 'O') {
                    ovals.add(Pair(x, y))
                } else if (splitInput[y][x] == '#') {
                    cubes.add(Pair(x, y))
                }
            }
        }
        return Pair(ovals, cubes)
    }

    fun weight(ovals: MutableList<Pair<Int, Int>>, height: Int): Int {
        return ovals.map { height - it.second }.sum()
    }

    fun enc(ovals: MutableList<Pair<Int, Int>>, cubes: MutableList<Pair<Int, Int>>): String {
        return ovals.toString() + cubes.toString()
    }

    // helper to visualize the board
    fun visualize(
        ovals: MutableList<Pair<Int, Int>>,
        cubes: MutableList<Pair<Int, Int>>,
        w: Int,
        h: Int,
        cycle: Long,
        weight: Int
    ) {
        println("CYCLE $cycle, W: $weight")
        for (y in 0..<h) {
            for (x in 0..<w) {
                if (ovals.contains(Pair(x, y))) {
                    print("O")
                } else if (cubes.contains(Pair(x, y))) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
        println()
    }
}