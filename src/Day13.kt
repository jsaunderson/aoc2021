fun main() {
    fun part1(input: List<String>): Int {
        val splitIndex = input.indexOfFirst { it == "" }
        val coordinates = input.slice(0 until splitIndex).map { it.split(',').map { it.toInt() } }
        val fold = input[splitIndex + 1].split('=').let { Pair(it[0].split(' ').last(), it[1].toInt()) }

        return fold.let { (direction, pos) ->
            if (direction == "x") {
                coordinates.map {
                    if (it[0] > pos) {
                        listOf(pos - (it[0] - pos), it[1])
                    } else {
                        it
                    }
                }
            } else {
                coordinates.map {
                    if (it[1] > pos) {
                        listOf(it[0], pos - (it[1] - pos))
                    } else {
                        it
                    }
                }
            }
        }.distinctBy { "${it[0]},${it[1]}" }.size
    }

    fun part2(input: List<String>): Unit {
        val splitIndex = input.indexOfFirst { it == "" }
        val coordinates = input.slice(0 until splitIndex).map { it.split(',').map { it.toInt() } }
        val fold = input.slice(splitIndex + 1 until input.size)
            .map { it.split('=').let { Pair(it[0].split(' ').last(), it[1].toInt()) } }

        val coords = fold.fold(coordinates) { coordinates, (direction, pos) ->
            if (direction == "x") {
                coordinates.map {
                    if (it[0] > pos) {
                        listOf(pos - (it[0] - pos), it[1])
                    } else {
                        it
                    }
                }
            } else {
                coordinates.map {
                    if (it[1] > pos) {
                        listOf(it[0], pos - (it[1] - pos))
                    } else {
                        it
                    }
                }
            }
        }
        val coordsSet = coords.map { "${it[0]},${it[1]}" }.toSet()

        val maxWidth = coords.maxOf { it[0] }
        val maxHeight = coords.maxOf { it[1] }
        println()
        (0..maxHeight).forEach { y ->
            (0..maxWidth).forEach { x ->
                val charToPrint = if (coordsSet.contains("$x,$y")) "#" else "."
                print(charToPrint)
            }
            println()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    println(part1(testInput))
    check(part1(testInput) == 17)
//    println(part2(testInput))
//    check(part2(testInput) == -1)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
