import sols.Day
import kotlin.math.floor


//NB: There is a way to solve with a plain quadratic equation, but the brute force works and doesn't fry my machine, so happy with this
class Day06 : Day("day06.txt") {
    override fun solve1() {
        val times = splitInput[0].split(": ")[1].trim().split("\\s+".toRegex()).map { it.toLong() }
        val distances = splitInput[1].split(": ")[1].trim().split("\\s+".toRegex()).map { it.toLong() }

        times.indices.fold(1L) { acc, i ->
            acc * getRes(times[i], distances[i])
        }.let { println(it) }

    }

    override fun solve2() {
        val time = splitInput[0].split(": ")[1].trim().split("\\s+".toRegex()).joinToString("").toLong()
        val distance = splitInput[1].split(": ")[1].trim().split("\\s+".toRegex()).joinToString("").toLong()
        getRes(time, distance).let { println(it) }
    }

    fun getRes(time: Long, distance: Long): Long {
        var speed: Long = if (time % 2 == 1L) {
            floor((time / 2L).toDouble()).toLong()
        } else {
            (time / 2)
        }

        var acc = 0

        while (speed * (time - speed) > distance) {
            speed--
            acc++
        }

        return if (time % 2 == 1L) acc * 2L else acc * 2L - 1L
    }
}