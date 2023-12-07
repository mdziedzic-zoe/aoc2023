import sols.Day


class Day07 : Day("day07.txt") {
    override fun solve1() {
        splitInput.map { line ->
            val (hand, bid) = line.split("\\s+".toRegex())
            hand to bid.toLong()
        }
            .sortedWith(HandComp())
            .foldIndexed(0L) { idx, acc, (_, bid) ->
                acc + (bid * (idx + 1))
            }.let { println(it) }
    }

    override fun solve2() {
        splitInput.map { line ->
            val (hand, bid) = line.split("\\s+".toRegex())
            hand to bid.toLong()
        }
            .sortedWith(HandComp2())
            .foldIndexed(0L) { idx, acc, (_, bid) ->
                acc + (bid * (idx + 1))
            }.let { println(it) }
    }
}

class HandComp : Comparator<Pair<String, Long>> {
    val ordering = "23456789TJQKA"
    override fun compare(h1: Pair<String, Long>, h2: Pair<String, Long>): Int {
        val hand1 = h1.first
        val hand2 = h2.first

        val hc1 = hand1.toCharArray().groupBy { it }.values.map { it.size }.sortedByDescending { it }
        val hc2 = hand2.toCharArray().groupBy { it }.values.map { it.size }.sortedByDescending { it }

        if (hc1[0] != hc2[0]) {
            return hc1[0].compareTo(hc2[0])
        } else {
            //check for pairs and full house
            if (hc1.take(2) != hc2.take(2)) {
                return hc1[1].compareTo(hc2[1])
            }

            for (i in hand1.indices) {
                if (hand1[i] != hand2[i]) {
                    return ordering.indexOf(hand1[i]).compareTo(ordering.indexOf(hand2[i]))
                }
            }
        }
        return 0
    }
}

class HandComp2 : Comparator<Pair<String, Long>> {
    val ordering = "J23456789TQKA"
    override fun compare(h1: Pair<String, Long>, h2: Pair<String, Long>): Int {
        val hand1 = h1.first
        val hand2 = h2.first

        var hc1: List<Int> = mutableListOf()
        var hc2: List<Int> = mutableListOf()

        if (!hand1.contains("J") && !hand2.contains("J")) {
            hc1 = hand1.toCharArray().groupBy { it }.values.map { it.size }.sortedByDescending { it }
            hc2 = hand2.toCharArray().groupBy { it }.values.map { it.size }.sortedByDescending { it }
        } else {
            hc1 =
                hand1.toCharArray().filter { it != 'J' }.groupBy { it }.values.map { it.size }.sortedByDescending { it }
                    .toMutableList()
            hc2 =
                hand2.toCharArray().filter { it != 'J' }.groupBy { it }.values.map { it.size }.sortedByDescending { it }
                    .toMutableList()

            val jc1 = hand1.toCharArray().filter { it == 'J' }.size
            val jc2 = hand2.toCharArray().filter { it == 'J' }.size
            if (hc1.isEmpty()) {
                hc1 = mutableListOf(5)
            } else {
                hc1[0] = hc1[0] + jc1
            }
            if (hc2.isEmpty()) {
                hc2 = mutableListOf(5)
            } else {
                hc2[0] = hc2[0] + jc2
            }
        }
        if (hc1[0] != hc2[0]) {
            return hc1[0].compareTo(hc2[0])
        } else {
            //check for pairs and full house
            if (hc1.take(2) != hc2.take(2)) {
                return hc1[1].compareTo(hc2[1])
            }

            for (i in hand1.indices) {
                if (hand1[i] != hand2[i]) {
                    return ordering.indexOf(hand1[i]).compareTo(ordering.indexOf(hand2[i]))
                }
            }
        }
        return 0
    }
}