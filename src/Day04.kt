data class BingoInput(val bingoNumbers: List<String>, val bingoBoards: List<List<List<String>>>)

fun main() {
    fun parseInput(input: List<String>): BingoInput {
        val bingoNumbers = input[0].split(',')
        val bingoBoards = input.drop(2).chunked(6).map { board ->
            board.slice(0..4).map { it.trim().split("\\s+".toRegex()) }
        }
        return BingoInput(bingoNumbers, bingoBoards)
    }

    fun transpose(matrix: List<List<String>>): List<List<String>> {
        val newMatrix = MutableList(5) { MutableList(5) { "" } }
        for (i in matrix.indices) {
            for (j in matrix.indices) {
                newMatrix[j][i] = matrix[i][j]
            }
        }

        return newMatrix
    }

    fun calcWin(board: List<List<String>>, nums: List<String>): Int {
        val uncheckedBoardSum = (board.flatten() - nums.toSet()).sumOf { it.toInt() }
        return uncheckedBoardSum * nums.last().toInt()
    }

    fun isBoardWin(bingoNums: List<String>, board: List<List<String>>): Boolean {
        val winRows = board + transpose(board)
        return winRows.any { bingoNums.containsAll(it) }
    }

    fun part1(input: List<String>): Int {
        val (bingoNumbers, bingoBoards) = parseInput(input)
        bingoNumbers.indices.forEach { bingoNumIndex ->
            val nums = bingoNumbers.slice(0..bingoNumIndex)
            bingoBoards.forEach { board ->
                if (isBoardWin(nums, board)) {
                    return calcWin(board, nums)
                }
            }
        }

        return -1
    }

    fun part2(input: List<String>): Int {
        val (bingoNumbers, bingoBoards) = parseInput(input)

        fun recurse(bingoNumIndex: Int, bingoBoards: List<List<List<String>>>): Int {
            val nums = bingoNumbers.slice(0..bingoNumIndex)

            val remainingBoards = bingoBoards.filter {
                !isBoardWin(nums, it)
            }

            return if (remainingBoards.isEmpty()) {
                calcWin(bingoBoards.last(), nums)
            } else {
                recurse(bingoNumIndex + 1, remainingBoards)
            }
        }

        return recurse(0, bingoBoards)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
