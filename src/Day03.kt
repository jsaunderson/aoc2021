fun main() {
    fun part1(input: List<String>): Int {
        val bits =
            input.flatMap { it.toCharArray().withIndex() }
                .groupBy({ it.index }, { it.value }).values
                .map { str ->
                    str.groupingBy { it }
                        .eachCount()
                        .maxByOrNull { it.value }?.key
                }
        val gamma = bits.joinToString("").toInt(2)
        val epsilon = bits.joinToString("") { if (it == '1') "0" else "1" }.toInt(2)
        return gamma * epsilon
    }

    fun part2(input: List<String>): Int {
        fun recurse(candidates: List<String>, index: Int, findMin: Boolean): Int {
            return if (candidates.size <= 1) {
                candidates[0].toInt(2)
            } else {
                val multiplier = if (findMin) -1 else 1
                val nextCandidates =
                    candidates.groupBy { it[index] }
                        .toSortedMap(if (findMin) compareBy { it } else compareByDescending { it })
                        .maxByOrNull { it.value.size * multiplier }?.value ?: emptyList()
                recurse(nextCandidates, index + 1, findMin)
            }
        }

        val oxygen = recurse(input, 0, false)
        val co2 = recurse(input, 0, true)

        return oxygen * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
