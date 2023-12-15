import sols.Day


class Day15 : Day("day15.txt") {
    fun hash(str: String): Int {
        var curr = 0
        str.forEach { c ->
            val char = c.code
            curr += char
            curr *= 17
            curr %= 256
        }
        return curr
    }

    override fun solve1() {
        val arr = splitInput[0].split(",")
        var acc = 0L
        arr.forEach { str ->
            val curr = hash(str)
            acc += curr.toLong()
        }
        println(acc)
    }


    override fun solve2() {
        val boxes = mutableMapOf<Int, List<List<String>>>()
        val arr = splitInput[0].split(",")

        arr.forEach { op ->
            if (op.contains("-")) {
                val id = op.split("-")[0]
                val h = hash(id)
                val l = boxes.getOrDefault(h, mutableListOf()).toMutableList()
                l.removeIf { it[0] == id }
                if (l.size > 0) {
                    boxes[h] = l
                } else {
                    boxes.remove(h)
                }
            } else {
                val (id, fl) = op.split("=")
                val h = hash(id)
                val l = boxes.getOrDefault(h, mutableListOf()).toMutableList()
                val idx = l.indexOfFirst { it[0] == id }
                if (idx != -1) {
                    val ml = l.toMutableList()
                    ml[idx] = listOf(id, fl)
                    boxes[h] = ml
                } else {
                    l.add(listOf(id, fl))
                    boxes[h] = l
                }
            }
        }

        var acc = 0L
        boxes.forEach { entry ->
            entry.value.forEachIndexed { idx, item ->
                acc += (entry.key + 1) * (idx + 1) * item[1].toLong()
            }
        }
        println(acc)
    }
}