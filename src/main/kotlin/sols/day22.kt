import sols.Day
import sols.Point

data class XYZTriple(
    val x: Int,
    val y: Int,
    var z: Int
)

data class Brick(
    val name: String,
    var start: XYZTriple,
    var end: XYZTriple
)

//fun Brick.overlaps(other: Brick): Boolean =
//    (start.x <= other.end.x && end.x >= other.start.x)
//            && (start.y <= other.end.y && end.y >= other.start.y)
//
//fun Brick.intersects(other: Brick, z: Int): Boolean =


class Day22 : Day("day22.txt") {

    override fun solve1() {
        val alph = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val bricks = splitInput.mapIndexed { i, it ->
            val (start, end) = it.split("~")
            val (x1, y1, z1) = start.split(",").map { it.toInt() }
            val (x2, y2, z2) = end.split(",").map { it.toInt() }
            Brick(
//                alph[i % alph.length].toString() + i.toString(),
                i.toString(),
                XYZTriple(x1, y1, z1), XYZTriple(x2, y2, z2)
            )
        }.sortedBy { it.start.z }


        val maxZ = mutableMapOf<Point, Pair<String, Int>>().withDefault { Pair("", 0) }

        val supports = mutableMapOf<String, MutableSet<String>>().withDefault { mutableSetOf() }
        val supportedBy = mutableMapOf<String, MutableSet<String>>().withDefault { mutableSetOf() }

        for (brick in bricks) {
            val pointsOnBase = (brick.start.x..brick.end.x).flatMap { x ->
                (brick.start.y..brick.end.y).map { y ->
                    Point(x, y)
                }
            }

            val highestZ = pointsOnBase.map { maxZ.getValue(it).second }.maxOrNull() ?: 0
            val d = brick.end.z - brick.start.z
            brick.start.z = highestZ + 1
            brick.end.z = highestZ + 1 + d

            pointsOnBase
                .filter { highestZ == maxZ.getValue(it).second }
                .forEach {
                    if (maxZ.getValue(it).first != "") {
                        supports.getOrPut(maxZ.getValue(it).first) { mutableSetOf() } += brick.name
                        supportedBy.getOrPut(brick.name) { mutableSetOf() } += maxZ.getValue(it).first
                    }
                }
            pointsOnBase.forEach { maxZ[it] = brick.name to brick.end.z }
        }

        val res = bricks.size - supportedBy.values.filter { it.size == 1 }.flatMap { it }.toSet().size
        println(res)

    }

    override fun solve2() {
        val alph = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val bricks = splitInput.mapIndexed { i, it ->
            val (start, end) = it.split("~")
            val (x1, y1, z1) = start.split(",").map { it.toInt() }
            val (x2, y2, z2) = end.split(",").map { it.toInt() }
            Brick(
//                alph[i % alph.length].toString() + i.toString(),
                i.toString(),
                XYZTriple(x1, y1, z1), XYZTriple(x2, y2, z2)
            )
        }.sortedBy { it.start.z }


        val maxZ = mutableMapOf<Point, Pair<String, Int>>().withDefault { Pair("", 0) }

        val supports = mutableMapOf<String, MutableSet<String>>().withDefault { mutableSetOf() }
        val supportedBy = mutableMapOf<String, MutableSet<String>>().withDefault { mutableSetOf() }

        for (brick in bricks) {
            val pointsOnBase = (brick.start.x..brick.end.x).flatMap { x ->
                (brick.start.y..brick.end.y).map { y ->
                    Point(x, y)
                }
            }

            val highestZ = pointsOnBase.map { maxZ.getValue(it).second }.maxOrNull() ?: 0
            val d = brick.end.z - brick.start.z
            brick.start.z = highestZ + 1
            brick.end.z = highestZ + 1 + d

            pointsOnBase
                .filter { highestZ == maxZ.getValue(it).second }
                .forEach {
                    if (maxZ.getValue(it).first != "") {
                        supports.getOrPut(maxZ.getValue(it).first) { mutableSetOf() } += brick.name
                        supportedBy.getOrPut(brick.name) { mutableSetOf() } += maxZ.getValue(it).first
                    }
                }
            pointsOnBase.forEach { maxZ[it] = brick.name to brick.end.z }
        }

        val res = bricks.map { brick ->
            var q = mutableSetOf<String>()
            q.addAll(supports.getValue(brick.name))
            val currFalling = mutableSetOf<String>(brick.name)
            while (q.isNotEmpty()) {
                val nxtQ = mutableSetOf<String>()
                for (b in q) {
                    if ((supportedBy.getValue(b) - currFalling).isEmpty()) {
                        nxtQ.addAll(supports.getValue(b))
                        currFalling.add(b)
                    }
                }
                q = nxtQ
            }
            currFalling.size - 1
        }.sum()

        println(res)
    }
}

fun main() {
    Day22().solve1()
    Day22().solve2()
}