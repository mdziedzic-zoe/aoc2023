import sols.Day
import kotlin.math.pow

data class ScratchCard(val id: Int, val wins: Int)
class Day04 : Day("day04.txt") {
    override fun solve1() {

        val res = splitInput.map { line ->
            val allnums = line.split(":")[1].split("|")
            val lnums = allnums[0].split(" ").filter { it.isNotEmpty() }.map(String::toInt).toSet()
            val rnums = allnums[1].split(" ").filter { it.isNotEmpty() }.map(String::toInt).toSet()

            val inter = lnums.intersect(rnums)
            if (inter.isEmpty()) 0.0 else 2.0.pow((inter.size - 1).toDouble())
        }

        val res2 = res.sum()
        println(res2.toInt())
    }

    override fun solve2() {
        val cards = splitInput.map { line ->
            val id = line.split(":")[0].split(" ").filter { it.isNotEmpty() }[1].toInt()

            val allnums = line.split(":")[1].split("|")
            val lnums = allnums[0].split(" ").filter { it.isNotEmpty() }.map(String::toInt).toSet()
            val rnums = allnums[1].split(" ").filter { it.isNotEmpty() }.map(String::toInt).toSet()

            val inter = lnums.intersect(rnums)
            ScratchCard(id, inter.size)
        }
            .associateBy { it.id }

        // first id, second count
        val pool = sortedMapOf<Int, Int>()
        for (card in cards) {
            pool[card.key] = 1
        }
        var acc = cards.size

        while (pool.isNotEmpty()) {
            val fk = pool.firstKey()
            val count = pool[fk]!!
            val c = cards[fk]!!
            acc += c.wins * count
            if (c.wins > 0) {
                for (id in 1..c.wins) {
                    pool[fk + id] = pool[fk + id]!! + count
                }
            }
            pool.remove(fk)
        }
        print(acc)
    }
}
