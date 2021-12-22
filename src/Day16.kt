fun main() {
    val hextToBinary = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111"
    )

    fun part1(input: List<String>): Int {
        val binary = input[0].map { hextToBinary.getValue(it) }.joinToString("")

        fun parsePacket(packet: String): Triple<List<Long>, Int, Int> {
            val version = packet.slice(0..2).toInt(2)
            val typeId = packet.slice(3..5).toInt(2)
            val lengthTypeId = packet[6]

            fun getLiteral(start: Int): Pair<Long, Int> {
                return packet.slice(start until packet.length).chunked(5).foldIndexed("") { index, acc, chunk ->
                    val newStr = acc + chunk.drop(1)
                    if (chunk[0] == '0') {
                        return newStr.toLong(2) to start + ((index + 1) * 5)
                    }

                    return@foldIndexed newStr
                }.toLong(2) to Int.MAX_VALUE
            }

            if (typeId == 4) {
                val (literal, usedBits) = getLiteral(6)
                return Triple(listOf(literal), usedBits, version)
            }

            val lengthTypeIdEnd = 7
            val bitLengthEnd = lengthTypeIdEnd + 15
            val subPacketEnd = lengthTypeIdEnd + 11
            val totalBitLengths = packet.slice(lengthTypeIdEnd until bitLengthEnd).toInt(2)
            val numberOfSubpackets = packet.slice(lengthTypeIdEnd until subPacketEnd).toInt(2)

            if (lengthTypeId == '0') {
                var usedLength = 0
                val literalList = mutableListOf<Long>()
                var versionSum = version
                while (usedLength < totalBitLengths) {
                    val start = usedLength + bitLengthEnd
                    val (num, used, packetVersion) = parsePacket(packet.slice(start until bitLengthEnd + totalBitLengths))
                    usedLength += used
                    literalList += num
                    versionSum += packetVersion
                }

                return Triple(literalList, usedLength + bitLengthEnd, versionSum)
            } else {
                var usedLength = 0
                var numberSubpackets = 0
                val literalList = mutableListOf<Long>()
                var versionSum = version
                while (numberSubpackets++ < numberOfSubpackets) {
                    val start = usedLength + subPacketEnd
                    val (num, used, packetVersion) = parsePacket(packet.slice(start until packet.length))
                    usedLength += used
                    literalList += num

                    versionSum += packetVersion
                }

                return Triple(literalList, usedLength + subPacketEnd, versionSum)
            }
        }

        return parsePacket(binary).third
    }

    fun part2(input: List<String>): Long {
        val binary = input[0].map { hextToBinary.getValue(it) }.joinToString("")

        fun parsePacket(packet: String): Triple<Long, Int, Int> {
            val version = packet.slice(0..2).toInt(2)
            val typeId = packet.slice(3..5).toInt(2)
            val lengthTypeId = packet[6]

            fun getLiteral(start: Int): Pair<Long, Int> {
                return packet.slice(start until packet.length).chunked(5).foldIndexed("") { index, acc, chunk ->
                    val newStr = acc + chunk.drop(1)
                    if (chunk[0] == '0') {
                        return newStr.toLong(2) to start + ((index + 1) * 5)
                    }

                    return@foldIndexed newStr
                }.toLong(2) to Int.MAX_VALUE
            }

            if (typeId == 4) {
                val (literal, usedBits) = getLiteral(6)
                return Triple(literal, usedBits, version)
            }

            val lengthTypeIdEnd = 7
            val bitLengthEnd = lengthTypeIdEnd + 15
            val subPacketEnd = lengthTypeIdEnd + 11
            val totalBitLengths = packet.slice(lengthTypeIdEnd until bitLengthEnd).toInt(2)
            val numberOfSubpackets = packet.slice(lengthTypeIdEnd until subPacketEnd).toInt(2)

            fun getResult(packetValues: List<Long>): Long {
                return when (typeId) {
                    0 -> packetValues.sum()
                    1 -> packetValues.fold(1L) { acc, l -> acc * l }
                    2 -> packetValues.minOf { it }
                    3 -> packetValues.maxOf { it }
                    5 -> if (packetValues[0] > packetValues[1]) 1 else 0
                    6 -> if (packetValues[0] < packetValues[1]) 1 else 0
                    7 -> if (packetValues[0] == packetValues[1]) 1 else 0
                    else -> 0
                }
            }

            if (lengthTypeId == '0') {
                var usedLength = 0
                val literalList = mutableListOf<Long>()
                var versionSum = version
                while (usedLength < totalBitLengths) {
                    val start = usedLength + bitLengthEnd
                    val (num, used, packetVersion) = parsePacket(packet.slice(start until bitLengthEnd + totalBitLengths))
                    usedLength += used
                    literalList += num
                    versionSum += packetVersion
                }

                return Triple(getResult(literalList), usedLength + bitLengthEnd, versionSum)
            } else {
                var usedLength = 0
                var numberSubpackets = 0
                val literalList = mutableListOf<Long>()
                var versionSum = version
                while (numberSubpackets++ < numberOfSubpackets) {
                    val start = usedLength + subPacketEnd
                    val (num, used, packetVersion) = parsePacket(packet.slice(start until packet.length))
                    usedLength += used
                    literalList += num

                    versionSum += packetVersion
                }

                return Triple(getResult(literalList), usedLength + subPacketEnd, versionSum)
            }
        }

        return parsePacket(binary).first
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
//    println(part1(testInput))
//    check(part1(testInput) == 31)
    println(part2(testInput))
    check(part2(testInput) == 0L)

    val input = readInput("Day16")
//    println(part1(input))
    println(part2(input))
}
