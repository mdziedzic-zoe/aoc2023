import com.microsoft.z3.Context
import com.microsoft.z3.Status
import de.earley.adventofcode.eq
import de.earley.adventofcode.get
import de.earley.adventofcode.plus
import de.earley.adventofcode.times
import sols.Day
import java.math.BigInteger


data class Hailstone(
    val x: BigInteger,
    val y: BigInteger,
    val z: BigInteger,
    val dx: BigInteger,
    val dy: BigInteger,
    val dz: BigInteger
)

data class Hailstone2(
    val x: Long,
    val y: Long,
    val z: Long,
    val dx: Long,
    val dy: Long,
    val dz: Long,
)

class Day24 : Day("day24.txt") {
    val lowerBound = BigInteger("200000000000000")
    val upperBound = BigInteger("400000000000000")

    override fun solve1() {
        val hailstones = parse()
        val combinations = hailstones.flatMap { h ->
            hailstones.map { other -> h to other }
        }.filter { it.first != it.second }
        println(combinations.count { intersection(it.first, it.second) } / 2)
    }

    private fun parse(): List<Hailstone> {
        val hailstones = splitInput
            .map { line ->
                val (l, r) = line.split("@")
                val (x, y, z) = l.trim().split(",")
                val (dx, dy, dz) = r.trim().split(",")
                Hailstone(
                    x = BigInteger(x.trim()),
                    y = BigInteger(y.trim()),
                    z = BigInteger(z.trim()),
                    dx = BigInteger(dx.trim()),
                    dy = BigInteger(dy.trim()),
                    dz = BigInteger(dz.trim()),
                )
            }
        return hailstones
    }

    private fun parse2(): List<Hailstone2> {
        val hailstones = splitInput
            .map { line ->
                val (l, r) = line.split("@")
                val (x, y, z) = l.trim().split(",")
                val (dx, dy, dz) = r.trim().split(",")
                Hailstone2(
                    x = x.trim().toLong(),
                    y = y.trim().toLong(),
                    z = z.trim().toLong(),
                    dx = dx.trim().toLong(),
                    dy = dy.trim().toLong(),
                    dz = dz.trim().toLong(),
                )
            }
        return hailstones
    }


    fun intersection(h1: Hailstone, h2: Hailstone): Boolean {
        val (x, y, _, dx, dy, _) = h1
        val (u, v, _, du, dv, _) = h2

        if (dy * du == dv * dx) return false

        val denominator1 = dy * du - dx * dv
        val numerator1 = dv * (x - u) - du * (y - v)
        val t1 = (numerator1.toFloat() / denominator1.toFloat()).toDouble()

        val denominator2 = dv * dx - du * dy
        val numerator2 = dy * (u - x) - dx * (v - y)
        val t2 = (numerator2.toFloat() / denominator2.toFloat()).toDouble()

        return t1 > 0 && (lowerBound.toDouble() < x.toDouble() + (t1 * dx.toDouble()) && x.toDouble() + (t1 * dx.toDouble()) < upperBound.toDouble())
                && t2 > 0 && (lowerBound.toDouble() < y.toDouble() + (t1 * dy.toDouble()) && y.toDouble() + (t1 * dy.toDouble()) < upperBound.toDouble())
    }

    override fun solve2() {
        val hailstones = parse2()
        with(Context()) {
            val solver = mkSolver()
            val x = mkRealConst("x")
            val y = mkRealConst("y")
            val z = mkRealConst("z")
            val vx = mkRealConst("vx")
            val vy = mkRealConst("vy")
            val vz = mkRealConst("vz")

            // three stones are enough to get all variables (6 + 3 variables).
            // So if there is a solution, that will be it
            hailstones.take(3).forEachIndexed { i, hailstone ->
                val t = mkRealConst("t$i")
                solver.add(mkGe(t, mkReal(0))) // t > 0
                solver.add(x + vx * t eq mkReal(hailstone.x) + mkReal(hailstone.dx) * t)
                solver.add(y + vy * t eq mkReal(hailstone.y) + mkReal(hailstone.dy) * t)
                solver.add(z + vz * t eq mkReal(hailstone.z) + mkReal(hailstone.dz) * t)
            }

            when (solver.check()) {
                Status.UNSATISFIABLE -> error("UNSAT")
                null, Status.UNKNOWN -> error("UNKNOWN")
                Status.SATISFIABLE -> {
                    println(solver.model[x].toLong() + solver.model[y].toLong() + solver.model[z].toLong())
                }
            }

        }
    }
}

fun main() {

    Day24().solve1()
    Day24().solve2()
}

