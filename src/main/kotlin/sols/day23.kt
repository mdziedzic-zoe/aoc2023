import sols.Day
import sols.FOUR_WAY_DIRS
import sols.Point


class Day23 : Day("day23.txt") {
    override fun solve1() {
        val h = splitInput.size
        val w = splitInput[0].length

        val s: Point = Point(1, 0)
        val f: Point = Point(w - 2, h - 1)
        println(dfs(f, Triple(0, s, mutableListOf())))
//        println(dfs(s, f, 0, mutableSetOf()))

    }


    override fun solve2() {
        val h = splitInput.size
        val w = splitInput[0].length
        val s: Point = Point(1, 0)
        val f: Point = Point(w - 2, h - 1)

        val g = buildGraph(s, f)
        println(dfs2(s, f, g, 0, setOf(s)))


    }

    private fun dfs2(
        start: Point,
        end: Point,
        graph: MutableMap<Pair<Int, Int>, MutableList<Pair<Pair<Int, Int>, Long>>>,
        dist: Long,
        seen: Set<Point>
    ): Long {
        if (start == end) {
            return dist
        }
        var best = -1L
        for (nextNode in graph[start]!!) {
            val (next, d) = nextNode
            if (next in seen) continue
            best = maxOf(best, dfs2(next, end, graph, dist + d, seen + next))
        }
        return best
    }


    fun dfs(end: Point, t: Triple<Long, Point, MutableList<Point>>): Long {
        val (dist, curr, set) = t
        if (curr == end) {
            return dist
        }
        var best = -1L
        for (dir in FOUR_WAY_DIRS) {
            var np = curr.first + dir.first to curr.second + dir.second
            if (np in set) continue
            if (np.first < 0 || np.second < 0 || np.second >= splitInput.size || np.first >= splitInput[0].length) continue
            val cc = splitInput[curr.second][curr.first]
            val nc = splitInput[np.second][np.first]
            if (nc == '#') continue
            val ns = set.map { it.copy() }.toMutableList()
            if (cc == '.' && nc == '.') {
                ns.add(np)
                best = maxOf(best, dfs(end, Triple(dist + 1, np, ns)))
            }
            if (dir == Pair(1, 0) && nc == '>') {
                ns.add(np)
                np = Point(np.first + 1, np.second)
                ns.add(np)
                best = maxOf(best, dfs(end, Triple(dist + 2, np, ns)))
            }
            if (dir == Pair(0, 1) && nc == 'v') {
                ns.add(np)
                np = Point(np.first, np.second + 1)
                ns.add(np)
                best = maxOf(best, dfs(end, Triple(dist + 2, np, ns)))
            }
            if (dir == Pair(-1, 0) && nc == '<') {
                ns.add(np)
                np = Point(np.first - 1, np.second)
                ns.add(np)
                best = maxOf(best, dfs(end, Triple(dist + 2, np, ns)))
            }
            if (dir == Pair(1, 0) && nc == '^') {
                ns.add(np)
                np = Point(np.first, np.second - 1)
                ns.add(np)
                best = maxOf(best, dfs(end, Triple(dist + 2, np, ns)))
            }
        }
        return best
    }

    val graph = mutableMapOf<Point, MutableSet<Pair<Point, Long>>>()

    fun buildGraph(end: Point, curr: Point, prevNode: Point, dist: Long, visited: MutableList<Point>) {
        if (curr == end) {
            graph.getOrPut(prevNode) { mutableSetOf() } += curr to dist
        }
        println(prevNode)

        var iter = curr
        var acc = dist

        var around = FOUR_WAY_DIRS.map { iter.first + it.first to iter.second + it.second }
            .filter { it !in visited }
            .filter { it.first >= 0 && it.second >= 0 && it.second < splitInput.size && it.first < splitInput[0].length }
            .filter { splitInput[it.second][it.first] != '#' }

        while (true) {
            if (iter == end) {
                graph.getOrPut(prevNode) { mutableSetOf() } += iter to acc
                break
            }
            if (around.size == 1) {
                visited += iter
                iter = around[0]
                acc++
                around = FOUR_WAY_DIRS.map { iter.first + it.first to iter.second + it.second }
                    .filter { it !in visited }
                    .filter { it.first >= 0 && it.second >= 0 && it.second < splitInput.size && it.first < splitInput[0].length }
                    .filter { splitInput[it.second][it.first] != '#' }
            } else break
        }


        if (around.size >= 2) {
            graph.getOrPut(prevNode) { mutableSetOf() } += iter to acc
            around.forEach {
                buildGraph(end, it, iter, 1, (visited + iter).toMutableList())
            }
        }
    }

    fun buildGraph(start: Point, end: Point): MutableMap<Point, MutableList<Pair<Point, Long>>> {
        val crossroads = mutableMapOf<Point, MutableList<Pair<Point, Long>>>(
            start to mutableListOf(),
            end to mutableListOf()
        )

        for (y in splitInput.indices) {
            for (x in splitInput[0].indices) {
                if (splitInput[y][x] == '.') {
                    val around = FOUR_WAY_DIRS.map { x + it.first to y + it.second }
                        .filter { it.first >= 0 && it.second >= 0 && it.second < splitInput.size && it.first < splitInput[0].length }
                        .filter { splitInput[it.second][it.first] != '#' }
                    if (around.size > 2) {
                        crossroads[x to y] = mutableListOf()
                    }
                }
            }
        }

        for (c in crossroads.keys) {
            var dist = 0L
            val seen = mutableSetOf(c)
            var queue = setOf(c)
            while (queue.isNotEmpty()) {
                dist++
                queue = buildSet {
                    for (el in queue) {
                        val around = FOUR_WAY_DIRS.map { el.first + it.first to el.second + it.second }
                            .filter { it.first >= 0 && it.second >= 0 && it.second < splitInput.size && it.first < splitInput[0].length }
                            .filter { splitInput[it.second][it.first] != '#' }
                            .filter { it !in seen }
                            .forEach {
                                if (it in crossroads) {
                                    crossroads.getValue(c).add(it to dist)
                                } else {
                                    add(it)
                                    seen.add(it)
                                }
                            }
                    }
                }
            }
        }
        return crossroads
    }


    fun dfs2x(end: Point, t: Triple<Long, Point, MutableSet<Point>>): Long {
        val (dist, curr, set) = t
        if (curr == end) {
            return dist
        }
        var best = -1L
        for (nextNode in graph[curr]!!) {
            if (nextNode.first in set) continue
            best = maxOf(
                best,
                dfs2x(end, Triple(dist + nextNode.second, nextNode.first, (set + curr).toMutableSet()))
            )
        }

        return best
    }


    fun longest(start: Point, end: Point, visited: MutableList<Point>): Long {
        val drr = DeepRecursiveFunction<Triple<Long, Point, MutableList<Point>>, Long> { (dist, curr, set) ->
            if (curr == end) {
                return@DeepRecursiveFunction dist
            }
            var best = -1L
            for (dir in FOUR_WAY_DIRS) {
                val np = curr.first + dir.first to curr.second + dir.second
                if (np in set) continue
                if (np.first < 0 || np.second < 0 || np.second >= splitInput.size || np.first >= splitInput[0].length) continue
                val nc = splitInput[np.second][np.first]
                if (nc == '#') continue
                val ns = (set + np).toMutableList()
                best = maxOf(best, callRecursive(Triple(dist + 1, np, ns)))
            }
            return@DeepRecursiveFunction best
        }

        return drr(Triple(0, start, visited))
    }
}

fun main() {
    Day23().solve1()
    Day23().solve2()
}