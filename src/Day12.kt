fun main() {
    fun part1(input: List<String>): Int {
        val parsedInput =
            input.map { it.split('-') }.flatMap { listOf(it, it.reversed()) }.groupBy({ it.first() }, { it[1] })
                .toMap()

        fun recurse(options: List<String>, visitedSmallCaves: List<String>, path: List<String>): Int {
            return options.sumOf {
                if (it.first().isUpperCase() || !visitedSmallCaves.contains(it)) {
                    val nextOptions = parsedInput.getValue(it)
                    return@sumOf recurse(nextOptions, visitedSmallCaves + it, path + it)
                } else if (it == "end") {
                    return@sumOf 1
                } else {
                    return@sumOf 0
                }
            }
        }

        return recurse(parsedInput.getValue("start"), listOf("start", "end"), listOf("start"))
    }

    fun part2(input: List<String>): Int {
        val parsedInput =
            input.map { it.split('-') }.flatMap { listOf(it, it.reversed()) }.groupBy({ it.first() }, { it[1] })
                .toMap()

        fun recurse(options: List<String>, visitedSmallCaves: List<String>, path: List<String>): Int {
            return options.sumOf {
                val canVisitSecondSmallCave =
                    (visitedSmallCaves.filter { it.first().isLowerCase() }.groupingBy { it }.eachCount()
                        .maxOf { it.value } < 2)
                            && it != "start" && it != "end"
                if (it.first().isUpperCase() || (!visitedSmallCaves.contains(it) || canVisitSecondSmallCave)) {
                    val nextOptions = parsedInput.getValue(it)
                    return@sumOf recurse(nextOptions, visitedSmallCaves + it, path + it)
                } else if (it == "end") {
                    return@sumOf 1
                } else {
                    return@sumOf 0
                }
            }
        }

        return recurse(parsedInput.getValue("start"), listOf("start", "end"), listOf("start"))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    println(part2(testInput))
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
