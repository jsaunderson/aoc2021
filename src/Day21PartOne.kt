package partone

import readInput

private fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }

class Game(playerPositions: List<Int>) {
    companion object {
        const val numberOfRollsPerTurn = 3
        const val trackSize = 10
    }

    private val players: List<Player>
    private val dice = Dice()
    private val currentPlayer: Sequence<Player>

    init {
        players = playerPositions.map { Player(it) }
        currentPlayer = players.asSequence().repeat()
    }

    fun playGame() {
        currentPlayer.any {
            it.playTurn(dice)
            return@any it.score >= 1000
        }
    }

    fun calculatePartOneAnswer(): Int {
        return players.minOf { it.score } * players.sumOf { it.rollCount }
    }
}

class Player(var trackPosition: Int) {
    var score: Int = 0
    var rollCount: Int = 0

    fun playTurn(dice: Dice) {
        val totalRoll = List(Game.numberOfRollsPerTurn) { dice.roll() }.sum()
        trackPosition = (trackPosition + totalRoll) % Game.trackSize
        rollCount += Game.numberOfRollsPerTurn
        score += if (trackPosition == 0) Game.trackSize else trackPosition
    }
}

class Dice {
    private var diceRoll = (1..100).asSequence().repeat().iterator()

    fun roll(): Int {
        return diceRoll.next()
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val startingPositions = input.map { it.split(": ").last().toInt() }
        val game = Game(startingPositions)
        game.playGame()
        return game.calculatePartOneAnswer()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    println(part1(testInput))
    check(part1(testInput) == 739785)

    val input = readInput("Day21")
    println(part1(input))
}
