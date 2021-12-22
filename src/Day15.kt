import java.util.*
import kotlin.math.min

@OptIn(ExperimentalStdlibApi::class)
fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.map { it.toString().toInt() } }

        val searchedLocation = mutableMapOf<String, Int>()

        var bestPath = Int.MAX_VALUE

        val recurse = DeepRecursiveFunction<Triple<Pair<Int, Int>, Set<String>, Int>, Int> { (pos, path, riskTotal) ->
            val (x, y) = pos
            val key = "$x,$y"

            if (riskTotal >= (searchedLocation[key] ?: Int.MAX_VALUE)) {
                return@DeepRecursiveFunction Int.MAX_VALUE
            }
            searchedLocation[key] = riskTotal

            if (riskTotal >= bestPath) {
                return@DeepRecursiveFunction Int.MAX_VALUE
            }

            if (x == grid[0].size - 1 && y == grid.size - 1) {
                bestPath = min(bestPath, riskTotal + grid[y][x])
                return@DeepRecursiveFunction riskTotal + grid[y][x]
            }

            if (x < 0 || x >= grid[0].size || y < 0 || y >= grid.size || path.contains(key)) {
                return@DeepRecursiveFunction Int.MAX_VALUE
            }

            val locationsToSearch = listOf(
                x + 1 to y,
                x to y + 1,
                x - 1 to y,
                x to y - 1
            )

            return@DeepRecursiveFunction locationsToSearch.minOf {
                callRecursive(
                    Triple(
                        it,
                        path + key,
                        riskTotal + grid[y][x]
                    )
                )
            }
        }

        return recurse(Triple(0 to 0, emptySet(), 0)) - grid[0][0]
    }

    fun part2(input: List<String>): Int {
        val inputGrid = input.map { it.map { it.toString().toInt() } }
        val repeatedGridRight = inputGrid.map { row -> (0..4).flatMap { row.map { cell -> cell + it } } }
        val grid = (0..4).flatMap { addNum ->
            repeatedGridRight.map { row ->
                row.map { it + addNum }.map { if (it > 9) it - 9 else it }
            }
        }

        val searchedLocation = mutableMapOf<String, Int>()
        var bestPath = Int.MAX_VALUE

        val compareByValue: Comparator<Triple<Pair<Int, Int>, Set<String>, Int>> = compareBy { it.third }
        val priorityQueue = PriorityQueue(compareByValue)


        val recurse = DeepRecursiveFunction<Triple<Pair<Int, Int>, Set<String>, Int>, Unit> { (pos, path, riskTotal) ->
            val (x, y) = pos
            val key = "$x,$y"

            if (riskTotal >= (searchedLocation[key] ?: Int.MAX_VALUE)) {
                return@DeepRecursiveFunction
            }
            searchedLocation[key] = riskTotal

            if (riskTotal >= bestPath) {
                return@DeepRecursiveFunction
            }


            if (x == grid[0].size - 1 && y == grid.size - 1) {
                bestPath = min(bestPath, riskTotal + grid[y][x])
                return@DeepRecursiveFunction
            }

            if (x < 0 || x >= grid[0].size || y < 0 || y >= grid.size || path.contains(key)) {
                return@DeepRecursiveFunction
            }

            val locationsToSearch = listOf(
                x + 1 to y,
                x to y + 1,
                x - 1 to y,
                x to y - 1
            )

            locationsToSearch.forEach {
                priorityQueue.add(
                    Triple(
                        it,
                        path + key,
                        riskTotal + grid[y][x]
                    )
                )
            }
        }

        priorityQueue.add(Triple(0 to 0, emptySet(), 0))

        while (priorityQueue.isNotEmpty()) {
            val next = priorityQueue.remove()
            recurse(next)
        }

        return bestPath - grid[0][0]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
//    println(part1(testInput))
//    check(part1(testInput) == 40)
    println(part2(testInput))
    check(part2(testInput) == 315)

    val input = readInput("Day15")
//    println(part1(input))
    println(part2(input))
}
