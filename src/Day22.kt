import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

data class Instruction(
    val xRange: Pair<Int, Int>,
    val yRange: Pair<Int, Int>,
    val zRange: Pair<Int, Int>,
    val turnOn: Boolean
) {
    fun toCube(): Cube {
        return Cube(xRange, yRange, zRange, turnOn)
    }
}


data class Cube(
    val xRange: Pair<Int, Int>,
    val yRange: Pair<Int, Int>,
    val zRange: Pair<Int, Int>,
    val isOn: Boolean
) {
    fun overlaps(cube: Cube): Boolean {
        return xRange.second >= cube.xRange.first && xRange.first <= cube.xRange.second &&
                yRange.second >= cube.yRange.first && yRange.first <= cube.yRange.second &&
                zRange.second >= cube.zRange.first && zRange.first <= cube.zRange.second
    }

    fun contains(cube: Cube): Boolean {
        return xRange.first <= cube.xRange.first && xRange.second >= cube.xRange.second &&
                yRange.first <= cube.yRange.first && yRange.second >= cube.yRange.second &&
                zRange.first <= cube.zRange.first && zRange.second >= cube.zRange.second
    }

    fun explode(cube: Cube): List<Cube> {
        fun createWindows(range: Pair<Int, Int>, forRange: Pair<Int, Int>): List<Pair<Int, Int>> {
            return if (range.first == range.second || (forRange.first <= range.first && forRange.second >= range.second)) {
                listOf(range)
            } else {
                val mid = range.first + ceil((range.second - range.first) / 2.0).toInt()
                listOf(range.first to mid - 1, mid to range.second)
            }
        }

        val xWindows = createWindows(xRange, cube.xRange)
        val yWindows = createWindows(yRange, cube.yRange)
        val zWindows = createWindows(zRange, cube.zRange)

        return xWindows.flatMap { xWindow ->
            yWindows.flatMap { yWindow ->
                zWindows.map { zWindow ->
                    Cube(xWindow, yWindow, zWindow, isOn)
                }
            }
        }
    }
}

fun main() {
    fun parseInput(input: List<String>): List<Instruction> {
        return input.map {
            val parts = it.split("=", ",")
            val xRange = parts[1].split("..").map { it.toInt() }.let { it[0] to it[1] }
            val yRange = parts[3].split("..").map { it.toInt() }.let { it[0] to it[1] }
            val zRange = parts[5].split("..").map { it.toInt() }.let { it[0] to it[1] }
            val turnOn = it.startsWith("on")
            return@map Instruction(xRange, yRange, zRange, turnOn)
        }
    }

    fun part1(input: List<String>): Int {
        val instructions = parseInput(input)
        val state = mutableMapOf<String, Boolean>()
        instructions
            .map { instruction ->
                fun limitRange(range: Pair<Int, Int>): Pair<Int, Int> {
                    return max(-50, range.first) to min(50, range.second)
                }
                return@map instruction.copy(
                    xRange = limitRange(instruction.xRange),
                    yRange = limitRange(instruction.yRange),
                    zRange = limitRange(instruction.zRange)
                )
            }
            .forEach { instruction ->
                IntRange(instruction.xRange.first, instruction.xRange.second).forEach { x ->
                    IntRange(instruction.yRange.first, instruction.yRange.second).forEach { y ->
                        IntRange(instruction.zRange.first, instruction.zRange.second).forEach { z ->
                            state["$x,$y,$z"] = instruction.turnOn
                        }
                    }
                }
            }

        return state.count { it.value }
    }

    fun part2(input: List<String>): Long {
        val cubes = parseInput(input).map { it.toCube() }

        val worldXRange =
            cubes.map { it.xRange }.minOf { it.first } to cubes.map { it.xRange }.maxOf { it.second }
        val worldYRange =
            cubes.map { it.yRange }.minOf { it.first } to cubes.map { it.yRange }.maxOf { it.second }
        val worldZRange =
            cubes.map { it.zRange }.minOf { it.first } to cubes.map { it.zRange }.maxOf { it.second }
        val world = Cube(worldXRange, worldYRange, worldZRange, false)


        return cubes.foldIndexed(listOf(world)) { index, allCubes, cube ->
            val newCubes = mutableListOf<Cube>()
            var cubesToCheck = allCubes

            println("processing $index/${cubes.size} $cube")

            while (cubesToCheck.isNotEmpty()) {
                cubesToCheck = cubesToCheck.flatMap {
                    if (cube.contains(it)) {
                        newCubes.add(it.copy(isOn = cube.isOn))
                        emptyList()
                    } else if (cube.overlaps(it)) {
                        it.explode(cube)
                    } else {
                        newCubes.add(it)
                        emptyList()
                    }
                }
            }
            return@foldIndexed newCubes
        }.filter { it.isOn }.sumOf {
            return@sumOf 1L *
                    (it.xRange.second - it.xRange.first + 1) *
                    (it.yRange.second - it.yRange.first + 1) *
                    (it.zRange.second - it.zRange.first + 1)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day22_test")

    val part1Result = part1(testInput)
    println(part1Result)
    check(part1Result == 474140)
    val part2Result = part2(testInput)
    println(part2Result)
    check(part2Result == 2758514936282235L)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
