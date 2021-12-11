import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val lineStack = Stack<Char>()
            val charMap = mapOf(
                '[' to ']',
                '(' to ')',
                '<' to '>',
                '{' to '}',
            )

            val num = line.toCharArray().toList().firstNotNullOfOrNull {
                when (it) {
                    '[', '(', '{', '<' -> {
                        lineStack.add(charMap[it])
                        null
                    }
                    ']', ')', '}', '>' -> {
                        val value = lineStack.pop()
                        if (value == it) null else it
                    }
                    else -> false
                }
            }.let {
                when (it) {
                    ']' -> 57
                    ')' -> 3
                    '}' -> 1197
                    '>' -> 25137
                    else -> 0
                }
            }
            return@sumOf num
        }
    }

    fun part2(input: List<String>): Long {
        return input.mapNotNull { line ->
            val lineStack = Stack<Char>()
            val charMap = mapOf(
                '[' to ']',
                '(' to ')',
                '<' to '>',
                '{' to '}',
            )

            val isValid = line.toCharArray().toList().all {
                when (it) {
                    '[', '(', '{', '<' -> {
                        lineStack.add(charMap[it])
                        true
                    }
                    ']', ')', '}', '>' -> {
                        val value = lineStack.pop()
                        value == it
                    }
                    else -> false
                }
            }
            return@mapNotNull if (isValid) lineStack else null
        }.map {
            val charValues = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
            return@map it.reversed().fold(0L) { total, char ->
                total * 5 + charValues.getValue(char)
            }
        }.sorted().let { it[it.size / 2] }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
