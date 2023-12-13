import sols.Day


class Day13 : Day("day13.txt") {
    override fun solve1() {
        val pats = fullInput.split("\n\n")
        pats.map {
            val (hor, vert) = remap(it)
            findMirror(hor).sumOf { it * 100L } + findMirror(vert).sum()
        }
            .sum()
            .also { println(it) }
    }


    override fun solve2() {
        val pats = fullInput.split("\n\n")
        pats
            .map {
                val (hor, vert) = remap(it)
                findMirror2(hor) * 100 +findMirror2(vert)
            }
            .sum()
            .also { println(it) }
    }

    fun remap(pat: String): Pair<List<CharArray>, List<CharArray>> {
        val hor = pat.split("\n").map { it.toCharArray() }
        val vert = (0..<hor[0].size).map { x ->
            (0..<hor.size).map { y ->
                hor[y][x]
            }.joinToString("").toCharArray()
        }
        return Pair(hor, vert)
    }

    fun findMirror(pat: List<CharArray>): List<Long> {
        val rows = mutableListOf<Long>()

        (1..<pat.size).forEach { y ->
            if (pat[y].contentEquals(pat[y - 1])) {
                var j = 2
                var same = true
                while (y - j >= 0 && y + j - 1 < pat.size) {
                    if (pat[y - j].contentEquals(pat[y + j - 1])) {
                        j++
                    } else {
                        same = false
                        break
                    }
                }
                if (same) {
                    rows.add(y.toLong())
                }
            }
        }
        return rows
    }

    fun charDiffCnt(s1: CharArray, s2: CharArray): Int {
        var cnt = 0
        for (i in s1.indices) {
            if (s1[i] != s2[i]) {
                cnt++
            }
        }
        return cnt
    }

    fun findMirror2(pat: List<CharArray>): Long {
        (1..<pat.size).forEach { y ->
            var acc = 0
            val dff = charDiffCnt(pat[y], pat[y - 1])
            acc += dff
            var j = 2
            while (y - j >= 0 && y + j - 1 < pat.size) {
                acc += charDiffCnt(pat[y - j], pat[y + j - 1])
                j++
            }
            if (acc == 1) {
                return y.toLong()
            }
        }
        return 0L
    }

}