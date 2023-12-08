import sols.Day


class Day08 : Day("day08.txt") {
    override fun solve1() {
        val instr = splitInput[0]
        val map = mutableMapOf<String, Pair<String, String>>()
        splitInput.drop(1).forEach {
            val key = it.substring(0, 3)
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)
            map[key] = Pair(left, right)
        }

        var curr = "AAA"
        var steps = 0

        while (curr != "ZZZ") {
            val idx = steps % instr.length
            val move = instr[idx]
            val (left, right) = map[curr]!!
            curr = if (move == 'L') {
                left
            } else {
                right
            }
            steps++
        }
        println(steps)
    }


    override fun solve2() {
        val instr = splitInput[0]
        val map = mutableMapOf<String, Pair<String, String>>()
        splitInput.drop(1).forEach {
            val key = it.substring(0, 3)
            val left = it.substring(7, 10)
            val right = it.substring(12, 15)
            map[key] = Pair(left, right)
        }

        var curr = map.keys.filter { it.last() == 'A' }.toSet()
        var steps = 0

        val cycles = mutableMapOf<String, Long>()
        while (cycles.size < curr.size) {
            val next = mutableSetOf<String>()
            for (c in curr) {
                val idx = steps % instr.length
                val move = instr[idx]
                val (left, right) = map[c]!!
                if (c.last() == 'Z') {
                    cycles[c] = steps.toLong()
                }
                if (move == 'L') {
                    next.add(left)
                } else {
                    next.add(right)
                }
            }
            curr = next
            steps++
        }
        lcm(cycles.values.toList()).let { println(it) }
    }

    private fun gcd(x: Long, y: Long): Long {
        var a = x
        var b = y
        while (b > 0) {
            val temp = b
            b = a % b
            a = temp
        }
        return a
    }

    private fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    private fun lcm(input: List<Long>): Long {
        var result = input[0]
        for (i in 1 until input.size){
            result = lcm(result, input[i])
        }
        return result
    }
}