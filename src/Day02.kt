enum class Command {
    FORWARD,
    DOWN,
    UP
}

fun main() {
    fun part1(input: List<String>): Int {
        val commands = input.map {
            val parts = it.split(' ')
            val command = Command.valueOf(parts[0].uppercase())
            return@map Pair(command, parts[1].toInt())
        }

        val (depthCommands, horizontalCommands) = commands.partition {
            it.first == Command.FORWARD
        }
        val depth = depthCommands.sumOf { it.second }
        val horizontal = horizontalCommands.sumOf { if (it.first == Command.DOWN) it.second else -it.second }
        return depth * horizontal
    }

    fun part2(input: List<String>): Int {
        val commands = input.map {
            val parts = it.split(' ')
            val command = Command.valueOf(parts[0].uppercase())
            return@map Pair(command, parts[1].toInt())
        }

        val (_, horizontalPos, depth) = commands.fold(Triple(0, 0, 0)) { (aim, horizontalPos, depth), next ->
            if (next.first == Command.FORWARD) {
                return@fold Triple(aim, horizontalPos + next.second, depth + (aim * next.second))
            } else {
                val aimChange = if (next.first == Command.DOWN) next.second else -next.second
                return@fold Triple(aim + aimChange, horizontalPos, depth)
            }
        }

        return horizontalPos * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
