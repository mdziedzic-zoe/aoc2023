import sols.Day


class Day09 : Day("day09.txt") {
    override fun solve1() {
        splitInput.map { it.split("\\s+".toRegex()).map { it.toLong() } }
            .map { extrapolateForwards(it) }
            .reduce(Long::plus).let { println(it) }
    }

    override fun solve2() {
        splitInput.map { it.split("\\s+".toRegex()).map { it.toLong() } }
            .map { extrapolateBackwards(it) }
            .reduce(Long::plus).let { println(it) }
    }

    fun extrapolateForwards(line: List<Long>): Long {
        val acc = iterate(line)
        var curr = acc.last().last()
        for (l in acc.reversed().drop(1)) {
            curr += l.last()
        }
        return curr
    }

    fun extrapolateBackwards(line: List<Long>): Long {
        val acc = iterate(line)
        var curr = acc.last().first()

        for (l in acc.reversed().drop(1)) {
            val new = l.first() - curr
            curr = new
        }
        return curr
    }

    private fun iterate(line: List<Long>): ArrayList<List<Long>> {
        val acc = ArrayList<List<Long>>()
        var iter = line.windowed(2).map { it[1] - it[0] }
        acc.add(line)
        acc.add(iter)
        while (!iter.all { it == 0L }) {
            val nxt = iter.windowed(2).map { it[1] - it[0] }
            // TODO only end is relevant for further processing
            acc.add(nxt)
            iter = nxt
        }
        return acc
    }


}
