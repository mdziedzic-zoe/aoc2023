import sols.Day


class Day12 : Day("day12.txt") {

    override fun solve1() {
        splitInput.map {
            val arr = it.split(" ")
            arr[0] to arr[1]
        }
            .map {
                eval2(it.first + ".", it.second)
            }
            .sum()
            .also { println(it) }
    }


    override fun solve2() {
        splitInput.map {
            val arr = it.split(" ")
            arr[0] to arr[1]
        }
            .map {
                val expanded1 = listOf(it.first, it.first, it.first, it.first, it.first).joinToString("?")
                val expanded2 = listOf(it.second, it.second, it.second, it.second, it.second).joinToString(",")
                expanded1 to expanded2
            }
            .map { eval2(it.first + ".", it.second) }
            .sum()
            .also { println(it) }

    }

    private fun tooFewCharsToFill(s: String, springs: String): Boolean {

        val qc = s.count { it == '?' }
        val hc = s.count { it == '#' }
        val sprc = springs.split(",").map { it.toInt() }.sum()
        if (hc + qc < sprc) {
            return true
        }
        return false
    }

    // cute but slow, leaving it here for posterity
    fun eval1(s: String, springs: String, cnt: Long): Long {
        if (tooFewCharsToFill(s, springs)) return 0L

        if (!s.contains("?")) {
            val arrangement = s.split("\\.+".toRegex()).map { it.length }.filter { it > 0 }.joinToString(",")
            return if (arrangement == springs) {
                cnt + 1L
            } else {
                cnt
            }
        }
        val idx = s.indexOf("?")
        val res = eval1(s.substring(0, idx) + "." + s.substring(idx + 1), springs, cnt) +
                eval1(s.substring(0, idx) + "#" + s.substring(idx + 1), springs, cnt)
        return res
    }

    val cache = mutableMapOf<Pair<String, String>, Long>()
    fun eval2(s: String, springs: String): Long {
        if (cache.containsKey(s to springs)) {
            return cache[s to springs]!!
        }

        if (s.isEmpty()) {
            val res = if (springs.isEmpty()) 1L else 0L
            cache[s to springs] = res
            return res
        }
        if (springs.isEmpty()) {
            val res = if (s.filter { it == '#' }.length > 0) 0L else 1L
            cache[s to springs] = res
            return res
        }
        if (tooFewCharsToFill(s, springs)) {
            cache[s to springs] = 0L
            return 0L
        }

        var res: Long = 0L
        if (s[0] == '.') {
            res = eval2(s.drop(1), springs)
        } else if (s[0] == '#') {
            val si = springs.split(",").map { it.toInt() }.toIntArray()
            if (s.length <= si[0]) {
                res = 0L
            } else {
                val c = s.take(si[0])
                if (!c.contains(".") && s[si[0]] != '#') {
                    res = eval2(s.drop(si[0] + 1), si.drop(1).joinToString(","))
                }
            }
        } else if (s[0] == '?') {
            res = eval2(s.replaceFirst("?", "#"), springs) + eval2(s.drop(1), springs)
        }

        cache[s to springs] = res
        return res
    }

}
