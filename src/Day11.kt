fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map { it.toString().toInt() }.toMutableList() }

        fun recurse(pointsToIncrease: List<Pair<Int, Int>>) {
            pointsToIncrease.forEach { (x, y) ->
                if (grid.getOrNull(y)?.getOrNull(x) == null) {
                    return@forEach
                }
                grid[y][x] += 1
                if (grid[y][x] == 10) {
                    val nextPointsToIncrease = listOf(
                        x to y + 1,
                        x to y - 1,
                        x + 1 to y,
                        x - 1 to y,
                        x + 1 to y + 1,
                        x + 1 to y - 1,
                        x - 1 to y + 1,
                        x - 1 to y - 1
                    )
                    recurse(nextPointsToIncrease)
                }
            }
        }

        val stepsToRun = 100
        var totalFlashes = 0
        (1..stepsToRun).forEach {
            grid.indices.forEach { y ->
                grid[0].indices.forEach { x ->
                    recurse(listOf(x to y))
                }
            }

            grid.forEach {
                totalFlashes += it.count { it > 9 }
                it.replaceAll { if (it > 9) 0 else it }
            }
        }

        return totalFlashes
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray().map { it.toString().toInt() }.toMutableList() }

        fun recurse(pointsToIncrease: List<Pair<Int, Int>>) {
            pointsToIncrease.forEach { (x, y) ->
                if (grid.getOrNull(y)?.getOrNull(x) == null) {
                    return@forEach
                }
                grid[y][x] += 1
                if (grid[y][x] == 10) {
                    val nextPointsToIncrease = listOf(
                        x to y + 1,
                        x to y - 1,
                        x + 1 to y,
                        x - 1 to y,
                        x + 1 to y + 1,
                        x + 1 to y - 1,
                        x - 1 to y + 1,
                        x - 1 to y - 1
                    )
                    recurse(nextPointsToIncrease)
                }
            }
        }

        val stepsToRun = 10000
        return (1..stepsToRun).first {
            grid.indices.forEach { y ->
                grid[0].indices.forEach { x ->
                    recurse(listOf(x to y))
                }
            }

            var flashCount = 0
            grid.forEach {
                flashCount += it.count { it > 9 }
                it.replaceAll { if (it > 9) 0 else it }
            }
            return@first flashCount == 100
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)
    check(part2(testInput) == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
