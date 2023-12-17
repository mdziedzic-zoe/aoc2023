import sols.Day
import java.util.*
import kotlin.math.max
import kotlin.math.min

typealias Point = Pair<Int, Int>

data class StepDirection(val dir: Char, val steps: Int)

class Day17 : Day("day17.txt") {
    val MAX_STEPS_IN_LINE = 3
    val posDirs = mapOf(
        'R' to listOf('R', 'D', 'U'),
        'L' to listOf('D', 'L', 'U'),
        'U' to listOf('R', 'U', 'L'),
        'D' to listOf('R', 'D', 'L'),
        'S' to listOf('R', 'D'),
    )

    val posDirs2 = mapOf(
        'R' to listOf('D', 'U'),
        'L' to listOf('D', 'U'),
        'U' to listOf('R', 'L'),
        'D' to listOf('R', 'L'),
        'S' to listOf('R', 'D'),
    )

    override fun solve1() {

        val q = PriorityQueue<Triple<Point, StepDirection, Long>>(compareBy { it.third })

        val target = splitInput[0].length - 1 to splitInput.size - 1
        val start = 0 to 0

        q.add(Triple(start, StepDirection('S', 0), 0L))
        val visited = mutableSetOf<Pair<Point, StepDirection>>()
        while (q.isNotEmpty()) {
            val el = q.remove()

            val currpos = el.first
            val currStepDirs = el.second
            val stepsTakenInLine = currStepDirs.steps
            val currDir = currStepDirs.dir
            val dist = el.third

            if (Pair(currpos, currStepDirs) in visited) {
                continue
            }
            visited.add(currpos to currStepDirs)

            if (currpos == target) {
                println(dist)
                return
            }

            for (nxtDir in posDirs[currDir]!!) {
                val nxtp = when (nxtDir) {
                    'R' -> currpos.first + 1 to currpos.second
                    'L' -> currpos.first - 1 to currpos.second
                    'U' -> currpos.first to currpos.second - 1
                    'D' -> currpos.first to currpos.second + 1
                    else -> throw Exception("Invalid direction")
                }
                if (nxtp.first < 0 || nxtp.second < 0 || nxtp.first >= splitInput[0].length || nxtp.second >= splitInput.size) {
                    continue
                }
                val nxtSteps = if (nxtDir == currDir) stepsTakenInLine + 1 else 1

                if (nxtSteps > MAX_STEPS_IN_LINE) {
                    continue
                }
                q.add(
                    Triple(
                        nxtp,
                        StepDirection(nxtDir, nxtSteps),
                        dist + splitInput[nxtp.second][nxtp.first].digitToInt().toLong()
                    )
                )
            }
        }
    }

    override fun solve2() {

        val q = PriorityQueue<Triple<Point, Char, Long>>(compareBy { it.third })
        val target = splitInput[0].length - 1 to splitInput.size - 1
        val start = 0 to 0

        val w = splitInput[0].length - 1
        val h = splitInput.size - 1

        q.add(Triple(start, 'S', 0L))
        val visited = mutableSetOf<Pair<Point, Char>>()
        while (q.isNotEmpty()) {
            val el = q.remove()

            val currpos = el.first
            val currDir = el.second
            val dist = el.third

            if (Pair(currpos, currDir) in visited) {
                continue
            }
            visited.add(currpos to currDir)

            if (currpos == target) {
                println(dist)
                return
            }

            for (nxtDir in posDirs2[currDir]!!) {
                for (stepSize in 4..10) {
                    var nxtPoint: Pair<Int, Int>? = null
                    var nxtDist: Long? = null

                    if (nxtDir == 'R') {
                        if (currpos.first + stepSize > w) {
                            continue
                        }
                        nxtPoint = currpos.first + stepSize to currpos.second
                        nxtDist = splitInput[currpos.second].slice(currpos.first + 1..nxtPoint.first)
                            .chunked(1)
                            .map { it.toLong() }.sum()
                    } else if (nxtDir == 'L') {
                        if (currpos.first - stepSize < 0) {
                            continue
                        }
                        nxtPoint = currpos.first - stepSize to currpos.second
                        nxtDist = splitInput[currpos.second].slice(nxtPoint.first..currpos.first - 1)
                            .chunked(1)
                            .map { it.toLong() }.sum()
                    } else if (nxtDir == 'U') {
                        if (currpos.second - stepSize < 0) {
                            continue
                        }
                        nxtPoint = currpos.first to max(currpos.second - stepSize, 0)
                        nxtDist =
                            (currpos.second - 1 downTo nxtPoint.second).map {
                                splitInput[it][currpos.first].digitToInt().toLong()
                            }
                                .sum()
                    } else if (nxtDir == 'D') {
                        if (currpos.second + stepSize > h) {
                            continue
                        }
                        nxtPoint = currpos.first to min(currpos.second + stepSize, h)
                        nxtDist =
                            (currpos.second + 1..nxtPoint.second).map {
                                splitInput[it][currpos.first].digitToInt().toLong()
                            }
                                .sum()
                    }

                    if (nxtPoint!!.first < 0 || nxtPoint.second < 0 || nxtPoint.first >= splitInput[0].length || nxtPoint.second >= splitInput.size) {
                        continue
                    }

                    q.add(
                        Triple(
                            nxtPoint,
                            nxtDir,
                            dist + nxtDist!!
                        )
                    )
                }
            }
        }
    }
}
