fun main() {
    fun parseInput(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        return input.map { line ->
            val (x1, y1, x2, y2) = line.split("->", ",").map { it.trim().toInt() }
            return@map Pair(x1 to y1, x2 to y2)
        }
    }

    fun getRange(valueA: Int, valueB: Int): IntProgression {
        return if (valueA < valueB) {
            valueA..valueB
        } else {
            valueA downTo valueB
        }
    }

    fun part1(input: List<String>): Int {
        val points = parseInput(input)
        val validPoints = points.filter { (pointA, pointB) ->
            pointA.first == pointB.first || pointA.second == pointB.second
        }

        return validPoints.flatMap { (pointA, pointB) ->
            if (pointA.first == pointB.first) {
                getRange(pointA.second, pointB.second).map { "${pointA.first},${it}" }
            } else {
                getRange(pointA.first, pointB.first).map { "${it},${pointA.second}" }
            }
        }
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= 2 }.size
    }

    fun part2(input: List<String>): Int {
        val points = parseInput(input)

        return points.flatMap { (pointA, pointB) ->
            if (pointA.first != pointB.first && pointA.second != pointB.second) {
                getRange(pointA.first, pointB.first).zip(getRange(pointA.second, pointB.second))
                    .map { (x, y) -> "${x},${y}" }
            } else if (pointA.first == pointB.first) {
                getRange(pointA.second, pointB.second).map { "${pointA.first},${it}" }
            } else {
                getRange(pointA.first, pointB.first).map { "${it},${pointA.second}" }
            }
        }
            .groupingBy { it }
            .eachCount()
            .filter { it.value >= 2 }.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
