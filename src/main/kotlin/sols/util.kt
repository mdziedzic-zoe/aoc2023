package sols

import java.io.File

val FOUR_WAY_DIRS = listOf(
    Pair(0, 1),
    Pair(1, 0),
    Pair(0, -1),
    Pair(-1, 0),
)
val EIGHT_WAY_DIRS = listOf(
    Pair(-1, -1),
    Pair(0, -1),
    Pair(-1, 0),
    Pair(1, -1),
    Pair(-1, 1),
    Pair(0, 1),
    Pair(1, 0),
    Pair(1, 1),
)
typealias Point = Pair<Int, Int>
typealias LongPoint = Pair<Long, Long>

object FileHelper {
    fun readLines(filename: String): List<String> {
        return File(ClassLoader.getSystemResource(filename).file)
            .readLines()
            .map(String::trim)
            .filter(String::isNotEmpty)
    }

    fun readFullFile(filename: String): String {
        return File(ClassLoader.getSystemResource(filename).file)
            .readText()
            .trim()
    }
}

abstract class Day(inputPath: String) {
    val splitInput: List<String>
    val fullInput: String

    init {
        this.splitInput = FileHelper.readLines(inputPath)
        this.fullInput = FileHelper.readFullFile(inputPath)
    }

    abstract fun solve1()
    abstract fun solve2()
    fun solve() {
        println("Solving ${this.javaClass.simpleName}")
        println("Answer from part 1")
        solve1()
        println("Answer from part 2")
        solve2()
        println()
    }
}

fun gcd(x: Long, y: Long): Long {
    var a = x
    var b = y
    while (b > 0) {
        val temp = b
        b = a % b
        a = temp
    }
    return a
}

fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}

fun lcm(input: List<Long>): Long {
    var result = input[0]
    for (i in 1 until input.size){
        result = lcm(result, input[i])
    }
    return result
}