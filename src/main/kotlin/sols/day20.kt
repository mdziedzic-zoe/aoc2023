import sols.Day
import sols.lcm

data class BroadcastModule(
    val type: ModuleType,
    val name: String,
    // for flipflops
    val dests: List<String>,
    var on: Boolean? = null,
    val lastPulse: MutableMap<String, Pulse> = mutableMapOf(),
)

/*
broadcaster -> a
%a -> inv, con
&inv -> b
%b -> con
&con -> output
 */
enum class ModuleType {
    BROADCASTER, FLIPFLOP, CONJUNCTION
}

enum class Pulse {
    HIGH, LOW
}

class Day20 : Day("day20.txt") {
    override fun solve1() {
        val arr = splitInput.map {
            val broadcaster = it.startsWith("broadcaster")
            val (name, destsraw) = it.split(" -> ")
            val dests = destsraw.split(", ").map { it.trim() }
            if (broadcaster) {
                BroadcastModule(ModuleType.BROADCASTER, name, dests)
            } else {
                if (name.startsWith("%")) {
                    BroadcastModule(ModuleType.FLIPFLOP, name.drop(1), dests, false, mutableMapOf())
                } else {
                    val lastPulses = splitInput
                        .map { it.split(" -> ") }
                        .filter { it[1].contains(name.drop(1)) }
                        .map { it[0].drop(1) to Pulse.LOW }.toMap().toMutableMap()
                    BroadcastModule(ModuleType.CONJUNCTION, name.drop(1), dests, null, lastPulses)
                }
            }
        }.associateBy { it.name }

        // modules to be actioned
        val q = ArrayDeque<Triple<String, String, Pulse>>()

        var lows = 0L
        var highs = 0L

        var cnt = 0L
        val buttonPresses = 1000

        while (cnt < buttonPresses) {
            // button to broadcaster
            lows++
            arr["broadcaster"]!!.dests.forEach { dest ->
//                println("broadcaster -low-> $dest")
                q.add(Triple("broadcaster", dest, Pulse.LOW))
                lows++
            }


            while (q.isNotEmpty()) {
                val (src, name, pulse) = q.removeFirst()
                val module = arr[name] ?: continue

                if (module.type == ModuleType.FLIPFLOP) {
                    if (pulse == Pulse.HIGH) {
                        continue
                    } else {
                        val isOn = module.on!!

                        if (isOn) {
                            module.dests.forEach {
//                                println("${module.name} -low-> $it")
                                q.add(Triple(name, it, Pulse.LOW))
                                lows++
                            }
                        } else {
                            module.dests.forEach {
//                                println("${module.name} -high-> $it")
                                q.add(Triple(name, it, Pulse.HIGH))
                                highs++
                            }
                        }
                        module.on = !isOn
                    }
                } else if (module.type == ModuleType.CONJUNCTION) {
                    module.lastPulse[src] = pulse

                    if (module.lastPulse.values.all { it == Pulse.HIGH }) {
                        module.dests.forEach {
//                            println("${module.name} -low-> $it")
                            q.add(Triple(name, it, Pulse.LOW))
                            lows++
                        }
                    } else {
                        module.dests.forEach {
//                            println("${module.name} -high-> $it")
                            q.add(Triple(name, it, Pulse.HIGH))
                            highs++
                        }
                    }
                }
            }
            cnt++
        }
        println(lows * highs)
    }

    override fun solve2() {
        val arr = splitInput.map {
            val broadcaster = it.startsWith("broadcaster")
            val (name, destsraw) = it.split(" -> ")
            val dests = destsraw.split(", ").map { it.trim() }
            if (broadcaster) {
                BroadcastModule(ModuleType.BROADCASTER, name, dests)
            } else {
                if (name.startsWith("%")) {
                    BroadcastModule(ModuleType.FLIPFLOP, name.drop(1), dests, false, mutableMapOf())
                } else {
                    val lastPulses = splitInput
                        .map { it.split(" -> ") }
                        .filter { it[1].contains(name.drop(1)) }
                        .map { it[0].drop(1) to Pulse.LOW }.toMap().toMutableMap()
                    BroadcastModule(ModuleType.CONJUNCTION, name.drop(1), dests, null, lastPulses)
                }
            }
        }.associateBy { it.name }

        // modules to be actioned
        val q = ArrayDeque<Triple<String, String, Pulse>>()

        var lows = 0L
        var highs = 0L

        var cnt = 1L
        val klCycles = mutableMapOf<String, Long>()


        while (true) {
            // button to broadcaster
            lows++
            arr["broadcaster"]!!.dests.forEach { dest ->
                q.add(Triple("broadcaster", dest, Pulse.LOW))
                lows++
            }


            while (q.isNotEmpty()) {
                val (src, name, pulse) = q.removeFirst()

                val module = arr[name] ?: continue

                if (module.type == ModuleType.FLIPFLOP) {
                    if (pulse == Pulse.HIGH) {
                        continue
                    } else {
                        val isOn = module.on!!

                        if (isOn) {
                            module.dests.forEach {
                                q.add(Triple(name, it, Pulse.LOW))
                                lows++
                            }
                        } else {
                            module.dests.forEach {
                                q.add(Triple(name, it, Pulse.HIGH))
                                highs++
                            }
                        }
                        module.on = !isOn
                    }
                } else if (module.type == ModuleType.CONJUNCTION) {
                    module.lastPulse[src] = pulse

                    if (
                        // looked it up in my input
                        module.name == "kl"
                        && pulse == Pulse.HIGH && !klCycles.containsKey(src)) {
//                        println(cnt)
                        klCycles[src] = cnt
                        if (klCycles.values.size == 4) {
                            println(lcm(klCycles.values.toList()))
                            return
                        }
                    }

                    if (module.lastPulse.values.all { it == Pulse.HIGH }) {
                        module.dests.forEach {
//                            println("${module.name} -low-> $it")
                            q.add(Triple(name, it, Pulse.LOW))
                            lows++
                        }
                    } else {
                        module.dests.forEach {
                            q.add(Triple(name, it, Pulse.HIGH))
                            highs++
                        }
                    }
                }
            }
            cnt++
        }
    }
}
