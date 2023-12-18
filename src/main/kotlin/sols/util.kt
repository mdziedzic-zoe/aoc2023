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