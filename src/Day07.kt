import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val positions = input[0].split(',').map { it.toInt() }

        return (positions.minOf { it }..positions.maxOf { it }).fold((-1 to Int.MAX_VALUE)) { prevBest, currPos ->
            val requiredFuel = positions.sumOf {
                (currPos - it).absoluteValue
            }

            if (requiredFuel < prevBest.second) {
                currPos to requiredFuel
            } else {
                prevBest
            }
        }.second
    }

    fun part2(input: List<String>): Int {
        val costCache = (0..2000).map { it * (it + 1) / 2 }
        val positions = input[0].split(',').map { it.toInt() }

        return (positions.minOf { it }..positions.maxOf { it }).fold((-1 to Int.MAX_VALUE)) { prevBest, currPos ->
            val requiredFuel = positions.sumOf {
                costCache[(currPos - it).absoluteValue]
            }

            if (requiredFuel < prevBest.second) {
                currPos to requiredFuel
            } else {
                prevBest
            }
        }.second
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
