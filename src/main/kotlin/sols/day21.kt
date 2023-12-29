import sols.Day
import sols.FOUR_WAY_DIRS
import sols.Point
import space.kscience.kmath.functions.ListPolynomial
import space.kscience.kmath.functions.asFunctionOver
import space.kscience.kmath.operations.LongRing


class Day21 : Day("day21.txt") {
    override fun solve1() {
        var start: Point? = null
        (0..splitInput.size - 1).forEach { y ->
            (0..splitInput[0].length - 1).forEach { x ->
                if (splitInput[y][x] == 'S') {
                    start = Pair(x, y)
                }
            }
        }

        val visited = mutableSetOf<Point>()
        val total = mutableSetOf<Point>()
        val q = ArrayDeque<Pair<Point, Int>>()
        val steps = 64;
        q.add(start!! to steps)

        while (q.isNotEmpty()) {
            val (curr, stepsLeft) = q.removeFirst()
            if (stepsLeft % 2 == 0) {
                total.add(curr)
            }
            if (stepsLeft == 0) {
                continue
            }
            if (curr in visited) {
                continue
            }
            visited.add(curr)
            for (dir in FOUR_WAY_DIRS) {
                val dx = curr.first + dir.first
                val dy = curr.second + dir.second
                val next = Pair(dx, dy)
                if (
                    next in visited
                    ||
                    dx < 0 || dx >= splitInput[0].length
                    ||
                    dy < 0 || dy >= splitInput.size
                    ||
                    splitInput[dy][dx] == '#'
                ) {
                    continue
                }
                q.add(next to stepsLeft - 1)
            }
        }
        println(total.size)
    }

    override fun solve2() {

        var start: Point? = null
        val h = splitInput.size
        val w = splitInput[0].length
        (0..h - 1).forEach { y ->
            (0..w - 1).forEach { x ->
                if (splitInput[y][x] == 'S') {
                    start = Pair(x, y)
                }
            }
        }

        val steps = 26501365L
        val rem = steps % w
        var points = setOf(start!!)
        repeat(rem.toInt()) {
            points = nextIter(points)
        }

        // solvable through quadratic formula
        // f(x) = ax^2 + bx + c

        val f0 = points.size
        repeat(w) {
            points = nextIter(points)
        }
        val f1 = points.size
        repeat(w) {
            points = nextIter(points)
        }
        val f2 = points.size

        val c = f0.toLong()
        val b = (2 * f1 - f2 / 2.0 - 3 * f0 / 2.0).toLong()
        val a = ((f2 + f0) / 2 - f1).toLong()

        val n: Long = steps / w
        val res = ListPolynomial(c, b, a).asFunctionOver(LongRing).invoke(n)
        println(res)

    }

    fun nextIter(set: Set<Point>): Set<Point> =
        set.flatMap {
            FOUR_WAY_DIRS.map { dir ->
                val dx = it.first + dir.first
                val dy = it.second + dir.second
                Pair(dx, dy)
            }
        }.filter {
            splitInput[it.second.mod(splitInput.size)][it.first.mod(splitInput[0].length)] != '#'
        }.toSet()
}

fun main(){
    Day21().solve()
}