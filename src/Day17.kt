import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val parts = input[0].split("=", ", ")
        val (xStart, xEnd) = parts[1].split("..").let { it[0].toInt() to it[1].toInt() }
        val (yStart, yEnd) = parts[3].split("..").let { it[0].toInt() to it[1].toInt() }

        fun shootProbe(xVelocityInitial: Int, yVelocityInitial: Int): Pair<Boolean, Int> {
            var xPos = 0
            var yPos = 0
            var xVelocity = xVelocityInitial
            var yVelocity = yVelocityInitial
            var maxHeight = Int.MIN_VALUE

            while (xPos < xEnd && yPos > yEnd) {
                xPos += xVelocity
                yPos += yVelocity
                maxHeight = max(maxHeight, yPos)
                xVelocity = when {
                    xVelocity > 0 -> xVelocity - 1
                    xVelocity < 0 -> xVelocity + 1
                    else -> 0
                }
                yVelocity--

                if ((xStart..xEnd).contains(xPos) && (yStart..yEnd).contains(yPos)) {
                    return true to maxHeight
                }
            }
            return false to Int.MIN_VALUE
        }

        var bestHeight = Int.MIN_VALUE
        (0..1000).forEach { xVelocity ->
            (0..1000).forEach { yVelocity ->
                var height = shootProbe(xVelocity, yVelocity).second
                bestHeight = max(height, bestHeight)
            }
        }

        return bestHeight
    }

    fun part2(input: List<String>): Int {
        val parts = input[0].split("=", ", ")
        val (xStart, xEnd) = parts[1].split("..").let { it[0].toInt() to it[1].toInt() }
        val (yStart, yEnd) = parts[3].split("..").let { it[0].toInt() to it[1].toInt() }

        fun shootProbe(xVelocityInitial: Int, yVelocityInitial: Int): Boolean {
            var xPos = 0
            var yPos = 0
            var xVelocity = xVelocityInitial
            var yVelocity = yVelocityInitial
            var maxHeight = Int.MIN_VALUE

            while (xPos < xEnd && yPos > yStart) {
                xPos += xVelocity
                yPos += yVelocity
                maxHeight = max(maxHeight, yPos)
                xVelocity = when {
                    xVelocity > 0 -> xVelocity - 1
                    xVelocity < 0 -> xVelocity + 1
                    else -> 0
                }
                yVelocity--

                if ((xStart..xEnd).contains(xPos) && (yStart..yEnd).contains(yPos)) {
                    return true
                }
            }
            return false
        }

        return (0..1000).sumOf { xVelocity ->
            (-1000..1000).count { yVelocity ->
                if (shootProbe(xVelocity, yVelocity)) {
                    println("$xVelocity,$yVelocity")
                }
                return@count shootProbe(xVelocity, yVelocity)
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    println(part1(testInput))
    check(part1(testInput) == 45)
    println(part2(testInput))
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
