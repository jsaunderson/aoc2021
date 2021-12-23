package parttwo

import readInput

fun <T> cartesianProduct(a: List<T>, b: List<T>, vararg lists: List<T>): List<List<T>> =
    (listOf(a, b).plus(lists))
        .fold(listOf(listOf())) { acc, set ->
            acc.flatMap { list -> set.map { element -> list + element } }
        }

val rolls = (1..3).toList()
val rollCombinations = cartesianProduct(rolls, rolls, rolls).map { it.sum() }

val gameCache = mutableMapOf<String, Map<Boolean, Long>>()

data class Game(val players: List<Player>, val isPlayerOne: Boolean = true) {
    companion object {
        const val trackSize = 10
    }

    fun playGame(): Map<Boolean, Long> {
        val cacheKey =
            "$isPlayerOne,${players[0].hashCode()},${players[1].hashCode()}"
        if (gameCache.containsKey(cacheKey)) {
            return gameCache.getValue(cacheKey)
        }

        val playerIndex = if (isPlayerOne) 0 else 1
        val result = rollCombinations.map {
            val subgame = this.copy(isPlayerOne = !isPlayerOne, players = players.map { it.copy() })
            val player = subgame.players[playerIndex]
            player.playTurn(it)
            return@map if (player.score >= 21) {
                mapOf(isPlayerOne to 1L)
            } else {
                subgame.playGame()
            }
        }.flatMap { it.entries }
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.sum() }

        gameCache[cacheKey] = result
        return result
    }
}

data class Player(var trackPosition: Int, var score: Int = 0) {
    fun playTurn(roll: Int) {
        trackPosition = (trackPosition + roll) % Game.trackSize
        score += if (trackPosition == 0) Game.trackSize else trackPosition
    }
}


fun main() {
    fun part2(input: List<String>): Long {
        val startingPositions = input.map { it.split(": ").last().toInt() }
        val players = startingPositions.map { Player(it) }
        val game = Game(players)
        return game.playGame().maxOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    println(part2(testInput))
    check(part2(testInput) == 444356092776315L)

    val input = readInput("Day21")
    println(part2(input))
}
