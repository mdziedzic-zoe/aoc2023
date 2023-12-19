import sols.Day

data class Rule(
    val cat: Int,
    val lessThan: Boolean,
    val num: Long,
    val dest: String,
) {
    fun eval(q: LongArray): String? {
        if (lessThan) {
            if (q[cat] < num) {
                return dest
            }
        } else {
            if (q[cat] > num) {
                return dest
            }
        }
        return null
    }
}


data class Workflow(
    val name: String,
    val rules: List<Rule>,
    val def: String,
)

class Day19 : Day("day19.txt") {
    override fun solve1() {
        val (workflows, parts) = parse()
        val accepted = mutableListOf<LongArray>()
        for (q in parts) {
            var curr = "in"
            while (true) {
                if (curr == "A") {
                    accepted.add(q)
                    break
                }
                if (curr == "R") {
                    break
                }
                val w = workflows[curr]!!
                curr = getNextDest(w.rules, q) ?: w.def
            }
        }
        accepted.map { it.sum() }.sum().also { println(it) }
    }


    override fun solve2() {
        val workflows = parse().first
        val init = mutableListOf(
            1L to 4001L,
            1L to 4001L,
            1L to 4001L,
            1L to 4001L,
        )

        println(dfs(workflows, init, "in"))
    }

    private fun parse(): Pair<Map<String, Workflow>, List<LongArray>> {
        val s = fullInput.split("\n\n")
        val workflows = s[0].split("\n")
            .map {
                val split = it.split("{")
                val name = split[0]

                val rawRules = split[1]
                    .dropLast(1)
                    .split(",")
                val def = rawRules.last()

                val rules = rawRules.dropLast(1)
                    .map {
                        val split2 = it.split(":")
                        val expr = split2[0]
                        val dest = split2[1]

                        val cat = "xmas".indexOf(expr[0])
                        val lessThan = expr[1] == '<'
                        val num = expr.substring(2).toLong()
                        Rule(cat, lessThan, num, dest)
                    }
                Workflow(name, rules, def)
            }.associateBy { it.name }

        val parts = s[1]
            .split("\n")
            .map {
                it.drop(1)
                    .dropLast(1)
                    .split(",")
                    .map { it.substring(2).toLong() }
                    .toLongArray()
            }
        return Pair(workflows, parts)
    }

    private fun getNextDest(rules: List<Rule>, q: LongArray): String? {
        for (r in rules) {
            val res = r.eval(q)
            if (res != null) {
                return res
            }
        }
        return null
    }


    fun dfs(workflows: Map<String, Workflow>, q: MutableList<Pair<Long, Long>>, curr: String): Long {
        if (curr == "A") {
            return q.map { it.second - it.first }.reduce { acc, l -> acc * l }
        }
        if (curr == "R") {
            return 0L
        }
        var ans = 0L
        val w = workflows[curr]!!

        for (rule in w.rules) {
            if (rule.lessThan) {
                ans += dfs(workflows,
                    q.map { it.copy() }.toMutableList().apply {
                        val np = this[rule.cat].first to rule.num
                        this[rule.cat] = np
                    }.toMutableList(),
                    rule.dest
                )
                val np = rule.num to q[rule.cat].second
                q[rule.cat] = np
            } else {
                ans += dfs(
                    workflows,
                    q.map { it.copy() }.toMutableList().apply {
                        val np = rule.num + 1 to this[rule.cat].second
                        this[rule.cat] = np
                    }.toMutableList(),
                    rule.dest
                )
                val np = q[rule.cat].first to rule.num + 1
                q[rule.cat] = np
            }
        }
        ans += dfs(workflows, q, w.def)

        return ans
    }

}
