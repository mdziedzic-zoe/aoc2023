import sols.Day
import kotlin.math.max

data class Game(val gameId: Int, val reveals: List<Triple<Int, Int, Int>>)

const val RL = 12
const val GL = 13
const val BL = 14

class Day02 : Day("day02.txt") {
    fun parseLine(s: String): Game {
        val s1 = s.split(":")
        val gameId = s1[0].substring(5).toInt()

        val rawReveals = s1[1].split(";")

        val reveals = rawReveals.map { rev ->
            var r = 0
            var g = 0
            var b = 0

            val digitRgx = "\\d+".toRegex()

            rev.split(",").forEach {
                val num = digitRgx.find(it)!!.value.toInt()

                if (it.contains("red")) {
                    r = num
                } else if (it.contains("green")) {
                    g = num
                } else if (it.contains("blue")) {
                    b = num
                }
            }
            Triple(r, g, b)
        }
        return Game(gameId, reveals)
    }

    override fun solve1() {
        val games = splitInput.map { parseLine(it) }
        var acc = 0
        games.forEach { g ->
            if (g.reveals.all { rev -> rev.first <= RL && rev.second <= GL && rev.third <= BL })
                acc += g.gameId
        }
        println(acc)
    }

    override fun solve2() {
        val games = splitInput.map { parseLine(it) }
        var acc = 0
        games.forEach { game ->
            var r = 0
            var g = 0
            var b = 0
            game.reveals.forEach { rev ->
                r = max(r, rev.first)
                g = max(g, rev.second)
                b = max(b, rev.third)
            }
            val power = r * g * b
            acc += power
        }
        println(acc)
    }
}