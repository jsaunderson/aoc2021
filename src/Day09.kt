fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.map { it.toString().toInt() } }
        return grid[0].indices.sumOf { x ->
            grid.indices.sumOf { y ->
                val current = grid[y][x]
                val lessUp = y == 0 || grid[y - 1][x] > current
                val lessDown = y == grid.size - 1 || grid[y + 1][x] > current
                val lessLeft = x == 0 || grid[y][x - 1] > current
                val lessRight = x == grid[0].size - 1 || grid[y][x + 1] > current

                if (lessUp && lessDown && lessLeft && lessRight) {
                    current + 1
                } else {
                    0
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.map { it.toString().toInt() } }

        fun recurseSearch(pos: Pair<Int, Int>, seenLocations: MutableSet<String>): Int {
            val (x, y) = pos
            val maybeCurrent = grid.getOrNull(y)?.getOrNull(x)
            if (seenLocations.contains("$x,$y") || maybeCurrent == null || maybeCurrent == 9) {
                return 0
            }

            seenLocations.add("$x,$y")

            val searchLocations = listOf(
                x to y - 1,
                x to y + 1,
                x - 1 to y,
                x + 1 to y,
            )

            return 1 + searchLocations.sumOf {
                recurseSearch(it, seenLocations)
            }
        }

        val z = mutableSetOf<String>()

        return grid[0].indices.flatMap { x ->
            grid.indices.map { y ->
                recurseSearch(x to y, z)
            }
        }.filter { it > 0 }.sorted().takeLast(3).reduce { acc, i -> acc * i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    println(part2(testInput))
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
