import kotlin.math.max

data class State(
    val hallway: List<String>,
    val roomA: List<String>,
    val roomB: List<String>,
    val roomC: List<String>,
    val roomD: List<String>
)

fun getRange(valueA: Int, valueB: Int): IntProgression {
    return if (valueA < valueB) {
        valueA..valueB
    } else {
        valueA downTo valueB
    }
}

fun <T> List<T>.update(index: Int, item: T): List<T> = toMutableList().apply { this[index] = item }

fun main() {
    fun part1(input: List<String>): Int {
        val rooms = input.slice(2..3)
            .map {
                it.split(" ", "#")
                    .filter { it.isNotBlank() }
            }
            .let { it[0].zip(it[1]) }
            .map { it.toList() }

        val initialState = State(
            hallway = List(11) { "." },
            roomA = rooms[0],
            roomB = rooms[1],
            roomC = rooms[2],
            roomD = rooms[3]
        )

        val cache = mutableMapOf<String, Int>()
        fun recurse(state: State, energy: Int): Int {
            val (hallway, roomA, roomB, roomC, roomD) = state

            val cacheKey = (hallway + roomA + roomB + roomC + roomD).joinToString()
            if (cache.containsKey(cacheKey)) return max(energy + cache.getValue(cacheKey), cache.getValue(cacheKey))

            if (roomA.all { it == "A" } && roomB.all { it == "B" } && roomC.all { it == "C" } && roomD.all { it == "D" } && hallway.all { it == "." }) {
                return energy
            }

            val energyPerStep = mapOf(
                "A" to 1,
                "B" to 10,
                "C" to 100,
                "D" to 1000
            )

            fun checkHallway(room: List<String>, expectedChar: String, hallwayPassageIndex: Int): Int {
                val roomHasSpace = room.all { it == "." } || room == listOf(".", expectedChar)
                if (!roomHasSpace) return Int.MAX_VALUE

                val result = hallway.withIndex().minOf {
                    if (it.value != expectedChar) return@minOf Int.MAX_VALUE

                    val path = getRange(hallwayPassageIndex, it.index).filter { pos -> pos != it.index }
                    val roomIndex = room.lastIndexOf(".")
                    return@minOf if (hallway.slice(path).all { it == "." }) {
                        recurse(
                            state.copy(
                                hallway = hallway.update(it.index, "."),
                                roomA = if (roomA === room) room.update(roomIndex, expectedChar) else roomA,
                                roomB = if (roomB === room) room.update(roomIndex, expectedChar) else roomB,
                                roomC = if (roomC === room) room.update(roomIndex, expectedChar) else roomC,
                                roomD = if (roomD === room) room.update(roomIndex, expectedChar) else roomD,
                            ),
                            (path.toList().size + roomIndex + 1) * energyPerStep.getValue(expectedChar)
                        )
                    } else {
                        Int.MAX_VALUE
                    }
                }
                cache[cacheKey] = result
                return max(result, energy + result)
            }

            fun checkRoom(room: List<String>, roomIndex: Int, hallwayPassageIndex: Int): Int {
                val potentialMoves = listOf(0, 1, 3, 5, 7, 9, 10)
                val result = potentialMoves.minOf {
                    val path = getRange(hallwayPassageIndex, it)
                    return@minOf if (hallway.slice(path).all { it == "." }) {
                        recurse(
                            state.copy(
                                hallway = hallway.update(it, room[roomIndex]),
                                roomA = if (roomA === room) room.update(roomIndex, ".") else roomA,
                                roomB = if (roomB === room) room.update(roomIndex, ".") else roomB,
                                roomC = if (roomC === room) room.update(roomIndex, ".") else roomC,
                                roomD = if (roomD === room) room.update(roomIndex, ".") else roomD,
                            ),
                            (path.toList().size + roomIndex) * energyPerStep.getValue(room[roomIndex])
                        )
                    } else {
                        Int.MAX_VALUE
                    }
                }
                cache[cacheKey] = result
                return max(result, energy + result)
            }

            val roomOptions = listOf(
                Triple(roomA, "A", 2),
                Triple(roomB, "B", 4),
                Triple(roomC, "C", 6),
                Triple(roomD, "D", 8)
            )

            val hallwayCheck = roomOptions.minOf { (room, checkChar, hallwayPassageIndex) ->
                checkHallway(room, checkChar, hallwayPassageIndex)
            }
            if (hallwayCheck < Int.MAX_VALUE) return hallwayCheck

            return roomOptions.minOf { (room, checkChar, hallwayPassageIndex) ->
                if (room.any { it != checkChar } && room[0] != ".") {
                    checkRoom(room, 0, hallwayPassageIndex)
                } else if (room[0] == "." && room[1] != checkChar && room[1] != ".") {
                    checkRoom(room, 1, hallwayPassageIndex)
                } else {
                    Int.MAX_VALUE
                }
            }
        }

        return recurse(initialState, 0)
    }

    fun part2(input: List<String>): Int {
        val rooms = input.slice(2..5)
            .map {
                it.split(" ", "#")
                    .filter { it.isNotBlank() }
            }
            .let {
                it[0].indices.map { index ->
                    it.map { row -> row[index] }
                }
            }

        val initialState = State(
            hallway = List(11) { "." },
            roomA = rooms[0],
            roomB = rooms[1],
            roomC = rooms[2],
            roomD = rooms[3]
        )

        val cache = mutableMapOf<String, Int>()
        fun recurse(state: State, energy: Int): Int {
            val (hallway, roomA, roomB, roomC, roomD) = state

            val cacheKey = (hallway + roomA + roomB + roomC + roomD).joinToString()
            if (cache.containsKey(cacheKey)) return max(energy + cache.getValue(cacheKey), cache.getValue(cacheKey))

            if (roomA.all { it == "A" } && roomB.all { it == "B" } && roomC.all { it == "C" } && roomD.all { it == "D" } && hallway.all { it == "." }) {
                return energy
            }

            val energyPerStep = mapOf(
                "A" to 1,
                "B" to 10,
                "C" to 100,
                "D" to 1000
            )

            fun checkHallway(room: List<String>, expectedChar: String, hallwayPassageIndex: Int): Int {
                val roomHasSpace =
                    room.joinToString("").matches(Regex("^\\.+${expectedChar}*$"))
                if (!roomHasSpace) return Int.MAX_VALUE

                val result = hallway.withIndex().minOf {
                    if (it.value != expectedChar) return@minOf Int.MAX_VALUE

                    val path = getRange(hallwayPassageIndex, it.index).filter { pos -> pos != it.index }
                    val roomIndex = room.lastIndexOf(".")
                    return@minOf if (hallway.slice(path).all { it == "." }) {
                        recurse(
                            state.copy(
                                hallway = hallway.update(it.index, "."),
                                roomA = if (roomA === room) room.update(roomIndex, expectedChar) else roomA,
                                roomB = if (roomB === room) room.update(roomIndex, expectedChar) else roomB,
                                roomC = if (roomC === room) room.update(roomIndex, expectedChar) else roomC,
                                roomD = if (roomD === room) room.update(roomIndex, expectedChar) else roomD,
                            ),
                            (path.toList().size + roomIndex + 1) * energyPerStep.getValue(expectedChar)
                        )
                    } else {
                        Int.MAX_VALUE
                    }
                }
                cache[cacheKey] = result
                return max(result, energy + result)
            }

            fun checkRoom(room: List<String>, roomIndex: Int, hallwayPassageIndex: Int): Int {
                val potentialMoves = listOf(0, 1, 3, 5, 7, 9, 10)
                val result = potentialMoves.minOf {
                    val path = getRange(hallwayPassageIndex, it)
                    return@minOf if (hallway.slice(path).all { it == "." }) {
                        recurse(
                            state.copy(
                                hallway = hallway.update(it, room[roomIndex]),
                                roomA = if (roomA === room) room.update(roomIndex, ".") else roomA,
                                roomB = if (roomB === room) room.update(roomIndex, ".") else roomB,
                                roomC = if (roomC === room) room.update(roomIndex, ".") else roomC,
                                roomD = if (roomD === room) room.update(roomIndex, ".") else roomD,
                            ),
                            (path.toList().size + roomIndex) * energyPerStep.getValue(room[roomIndex])
                        )
                    } else {
                        Int.MAX_VALUE
                    }
                }
                cache[cacheKey] = result
                return max(result, energy + result)
            }

            val roomOptions = listOf(
                Triple(roomA, "A", 2),
                Triple(roomB, "B", 4),
                Triple(roomC, "C", 6),
                Triple(roomD, "D", 8)
            )

            val hallwayCheck = roomOptions.minOf { (room, checkChar, hallwayPassageIndex) ->
                checkHallway(room, checkChar, hallwayPassageIndex)
            }
            if (hallwayCheck < Int.MAX_VALUE) return hallwayCheck

            return roomOptions.minOf { (room, checkChar, hallwayPassageIndex) ->
                val changeChar =
                    Regex("^\\.*([^${checkChar}.]|[${checkChar}](?=[^${checkChar}]))").find(room.joinToString(""))
                
                if (changeChar != null) {
                    checkRoom(room, room.indexOfFirst { it != "." }, hallwayPassageIndex)
                } else {
                    Int.MAX_VALUE
                }
            }
        }

        return recurse(initialState, 0)
    }

    // test if implementation meets criteria from the description, like:
    val part2Input = readInput("Day23_part2")
    val testInput = readInput("Day23_test")

    val part1Result = part1(testInput)
    println(part1Result)
    check(part1Result == 12521)

//    val part2Result = part2(part2Input)
//    println(part2Result)
//    check(part2Result == -1)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(part2Input))
}
