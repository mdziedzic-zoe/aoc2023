import sols.Day
import sols.EIGHT_WAY_DIRS


class Day03 : Day("day03.txt") {
    override fun solve1() {
        val rows = splitInput.size
        val cols = splitInput[0].length
        var sum = 0

        val symbols = splitInput.flatMap { it.toList() }.filter { it != '.' && !it.isDigit() }.toSet()

        for (i in 0..<rows) {
            var j = 0
            while (j in 0..<cols) {
                if (splitInput[i][j].isDigit()) {
                    val acc = StringBuilder()
                    var adjacent = false
                    while (j < cols && splitInput[i][j].isDigit()) {
                        for (dir in EIGHT_WAY_DIRS) {
                            val x = i + dir.first
                            val y = j + dir.second
                            if (x in 0..<rows && y in 0..<cols) {
                                if (splitInput[x][y] in symbols) {
                                    adjacent = true
                                }
                            }
                        }
                        acc.append(splitInput[i][j])
                        j++
                    }
                    if (adjacent) {
                        sum += acc.toString().toInt()
                    }
                }
                j++
            }
        }
        println(sum)
    }

    override fun solve2() {
        val rows = splitInput.size
        val cols = splitInput[0].length
        var sum = 0

        for (i in 0..<rows) {
            for (j in 0..<cols) {
                if (splitInput[i][j] == '*') {
                    val adjacents = mutableListOf<Int>()

                    val visited = mutableSetOf<Pair<Int, Int>>()

                    for (dir in EIGHT_WAY_DIRS) {
                        val x = i + dir.first
                        val y = j + dir.second
                        if (x in 0..<rows && y in 0..<cols && !visited.contains(Pair(x, y))) {
                            if (splitInput[x][y].isDigit()) {
                                val acc = StringBuilder()
                                acc.append(splitInput[x][y])
                                visited.add(Pair(x, y))

                                var iter = y
                                while (iter - 1 >= 0 && splitInput[x][iter - 1].isDigit()) {
                                    acc.insert(0, splitInput[x][iter - 1])
                                    visited.add(Pair(x, iter - 1))
                                    iter--
                                }
                                iter = y
                                while (iter + 1 < cols && splitInput[x][iter + 1].isDigit()) {
                                    acc.append(splitInput[x][iter + 1])
                                    visited.add(Pair(x, iter + 1))
                                    iter++
                                }

                                adjacents.add(acc.toString().toInt())
                            }
                        }
                    }
                    if (adjacents.size == 2) {
                        sum += adjacents[0] * adjacents[1]
                    }
                }
            }
        }
        println(sum)
    }
}