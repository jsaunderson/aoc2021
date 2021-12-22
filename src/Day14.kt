fun main() {
    fun part1(input: List<String>): Int {
        val start = input[0]
        val rules = input.slice(2 until input.size).map { it.split(" -> ").let { it[0] to it[1] } }.toMap()

        val chars = (0 until 10).fold(start) { chain, _ ->
            chain.windowed(2).map {
                val charToInsert = rules.get(it)
                if (charToInsert == null) {
                    it.get(0).toString()
                } else {
                    (it.get(0) + charToInsert)
                }
            }.joinToString("") + chain.last()
        }.toCharArray().groupBy { it }

        return chars.maxOf { it.value.size } - chars.minOf { it.value.size }
    }

    fun part2(input: List<String>): Long {
        val start = input[0]
        val rules = input.slice(2 until input.size).map { it.split(" -> ").let { it[0] to it[1] } }.toMap()

        val cache = mutableMapOf<String, Map<Char, Long>>()

        fun recurse(chain: String, depth: Int): Map<Char, Long> {
            if (depth == 40) {
                return chain.toCharArray().groupBy { it }
                    .mapValues { it.value.size.toLong() }
            }

            val newChain = chain.windowed(2).map {
                val charToInsert = rules.get(it)
                if (charToInsert == null) {
                    it.get(0).toString()
                } else {
                    (it.get(0) + charToInsert)
                }
            }.joinToString("") + chain.last()

            val cacheKey = "$chain,$depth"
            if (cache.containsKey(cacheKey)) {
                return cache.getValue(cacheKey)
            }

            val chainLength = 100
            val charCounts = if (newChain.length > chainLength) {
                //try fix last chunk being partial
                val chunks = newChain.chunked(chainLength).let {
                    it.dropLast(2) + it.slice(it.size - 2 until it.size)
                }

                chunks.windowed(2, partialWindows = true).flatMapIndexed { index, it ->
                    listOf(
                        recurse(it[0], depth + 1),
                        if (it.size == 1) emptyMap() else recurse(
                            it[0].last().toString() + it[1].first(),
                            depth + 1
                        ),
                        if (it.size == 1) emptyMap() else mapOf(it[0].last() to -1L),
                        if (it.size == 1) emptyMap() else mapOf(it[1].first() to -1L)
                    )
                }
            } else {
                listOf(recurse(newChain, depth + 1))
            }

            val sumCounts =
                charCounts.flatMap { it.entries }.groupBy({ it.key }, { it.value }).mapValues { it.value.sum() }
            cache.set(cacheKey, sumCounts)
            return sumCounts
        }

        val chars = recurse(start, 0)

//        cache.entries.forEach { println(it) }
        return chars.maxOf { it.value } - chars.minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    println(part1(testInput))
    check(part1(testInput) == 1588)
    println(part2(testInput))
    check(part2(testInput) == 2188189693529)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}
