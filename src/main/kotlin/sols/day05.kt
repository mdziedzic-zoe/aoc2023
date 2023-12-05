import sols.Day

data class Range(val start: Long, val end: Long, val offset: Long)
data class Interval(val start: Long, val end: Long)

class Day05 : Day("day05.txt") {
    override fun solve1() {
        val arr = fullInput.split("\n\n")
        val seeds = arr[0].substring(7).split(" ").map { it.toLong() }
        val stages = arr.drop(1).map {
            it.split("\n").drop(1).map { it.split(" ").map { it.toLong() } }
                .map { Range(start = it[1], offset = it[0] - it[1], end = it[1] + it[2]) }
        }

        seeds.map {
            var seed = it
            for (stage in stages) {
                for (rg in stage) {
                    if (seed in rg.start..rg.end) {
                        seed += rg.offset
                        break
                    }
                }
            }
            seed
        }.min().let { println(it) }

    }


    override fun solve2() {
        val arr = fullInput.split("\n\n")
        val seedRanges = arr[0].substring(7).split(" ")
            .map { it.toLong() }
            .chunked(2)
            .map { it[0] to it[0] + it[1] }

        val stages = arr.drop(1)
            .map {
                it.split("\n")
                    .drop(1)
                    .map { line -> line.split(" ").map { it.toLong() } }
                    .map { Range(start = it[1], offset = it[0] - it[1], end = it[1] + it[2]) }
            }

        seedRanges.map { sr ->
            var intervals = listOf(Interval(sr.first, sr.second))
            for (j in 0..<stages.size) {
                val stage = stages[j]
                val nextIntervals = mutableListOf<Interval>()
                for (k in 0..<intervals.size) {
                    val interval = intervals[k]
                    nextIntervals.addAll(getIntervals(stage, interval))
                }
                intervals = nextIntervals
            }
            intervals.minOf { it.start }
        }.min().let { println(it) }
    }

    private fun getIntervals(
        ranges: List<Range>,
        interval: Interval,
    ): MutableList<Interval> {
        val x1 = interval.start
        val x2 = interval.end

        val leftOut = arrayListOf<Interval>()
        val mapped = arrayListOf<Interval>()

        for (r in ranges) {
            /*
            1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
               a----------------------------------------------------b
                     c--d        e--f                            g-----h
             */
            val y1 = maxOf(x1, r.start)
            val y2 = minOf(x2, r.end - 1)
            if (y1 <= y2) {
                leftOut.add(Interval(y1, y2))
                mapped.add(Interval(y1 + r.offset, y2 + r.offset))
            }
        }
        leftOut.sortBy { it.start }
        var curr = x1
        for ((y1, y2) in leftOut) {
            if (y1 > curr) mapped.add(Interval(curr, y1 - 1))
            curr = y2 + 1
        }
        if (curr <= x2) mapped.add(Interval(curr, x2))
        return mapped
    }
}