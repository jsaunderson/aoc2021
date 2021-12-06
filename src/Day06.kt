fun main() {
    fun part1(input: List<String>): Int {
        val initialFish = input[0].split(',').map { it.toInt() }

        return (1..80).fold(initialFish) { fish, _ ->
            fish.flatMap {
                if (it == 0) {
                    listOf(6, 8)
                } else {
                    listOf(it - 1)
                }
            }
        }.size
    }

    fun part2(input: List<String>): Long {
        val initialFish = input[0].split(',')
            .map { it.toInt() }
            .groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        return (1..256).fold(initialFish) { fish, _ ->
            fish.flatMap {
                if (it.key == 0) {
                    listOf(6 to it.value, 8 to it.value)
                } else {
                    listOf(it.key - 1 to it.value)
                }
            }
                .groupBy { it.first }
                .mapValues { it.value.sumOf { (_, value) -> value } }
        }.values.sumOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934)
    check(part2(testInput) == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
