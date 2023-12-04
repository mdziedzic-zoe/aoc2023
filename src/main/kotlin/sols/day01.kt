import sols.Day

class Day01 : Day("day01.txt") {
    override fun solve1() {
        var acc: Long = 0
        val l = (0..10).toList()
        input.forEach { line ->
            val leftDigit = l.map { d ->
                line.indexOf(d.toString()) to d
            }
                .filter { it.first >= 0 }
                .minBy { it.first }.second

            val rightDigit = l.map { d ->
                line.lastIndexOf(d.toString()) to d
            }
                .filter { it.first >= 0 }
                .maxBy { it.first }.second

            val lineNum = leftDigit * 10 + rightDigit
            acc += lineNum
        }
        println(acc)

    }


    override fun solve2() {
        val m = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )

        var acc: Long = 0
        input.forEach { line ->

            val l1 = m.entries.flatMap {
                val left = line.indexOf(it.key)
                val right = line.lastIndexOf(it.key)
                listOf(left to it.value, right to it.value)
            }
            val l2 = m.entries.flatMap {
                val left = line.indexOf(it.value.toString())
                val right = line.lastIndexOf(it.value.toString())
                listOf(left to it.value, right to it.value)
            }
            val present = (l1 + l2).filter { it.first >= 0 }
            val left = present.minBy { it.first }.second
            val right = present.maxBy { it.first }.second

            val lineNum = left * 10 + right
            acc += lineNum

        }
        println(acc)
    }
}