import java.util.Collections.swap

// From internet
fun <V> List<V>.permutations(): List<List<V>> {
    val retVal: MutableList<List<V>> = mutableListOf()

    fun generate(k: Int, list: List<V>) {
        // If only 1 element, just output the array
        if (k == 1) {
            retVal.add(list.toList())
        } else {
            for (i in 0 until k) {
                generate(k - 1, list)
                if (k % 2 == 0) {
                    swap(list, i, k - 1)
                } else {
                    swap(list, 0, k - 1)
                }
            }
        }
    }

    generate(this.count(), this.toList())
    return retVal
}

fun main() {
    fun parseInput(input: List<String>): List<Pair<List<String>, List<String>>> {
        return input.map { it.split('|', ' ').filter { it.trim().isNotEmpty() } }
            .map { Pair(it.slice(0..9), it.slice(10..13)) }
    }

    val segmentLocations = mapOf(
        "abcefg" to 0,
        "cf" to 1,
        "acdeg" to 2,
        "acdfg" to 3,
        "bcdf" to 4,
        "abdfg" to 5,
        "abdefg" to 6,
        "acf" to 7,
        "abcdefg" to 8,
        "abcdfg" to 9
    )
    val aToG = listOf("a", "b", "c", "d", "e", "f", "g")

    fun part1(input: List<String>): Int {
        val digitsOrOutput = parseInput(input)



        return digitsOrOutput.sumOf { (digits, output) ->
            val mappingSeq = aToG.permutations().asSequence().mapNotNull { permutation ->
                val mapping = permutation.zip(aToG).toMap()
                return@mapNotNull digits.map { digitStr ->
                    val lookupKey = digitStr.map { mapping[it.toString()] }.sortedBy { it }.joinToString("")
                    val actualNum = segmentLocations[lookupKey] ?: return@mapNotNull null
                    digitStr.toCharArray().sorted().joinToString("") to actualNum
                }
            }.firstOrNull()?.toMap()

            output.map { it.split("").sorted().joinToString("").let { mappingSeq?.get(it) } }
                .filter { listOf(1, 4, 7, 8).contains(it) }
                .size
        }
    }

    fun part2(input: List<String>): Int {
        val digitsOrOutput = parseInput(input)

        return digitsOrOutput.sumOf { (digits, output) ->
            val mappingSeq = aToG.permutations().asSequence().mapNotNull { permutation ->
                val mapping = permutation.zip(aToG).toMap()
                return@mapNotNull digits.map { digitStr ->
                    val lookupKey = digitStr.map { mapping[it.toString()] }.sortedBy { it }.joinToString("")
                    val actualNum = segmentLocations[lookupKey] ?: return@mapNotNull null
                    digitStr.toCharArray().sorted().joinToString("") to actualNum
                }
            }.firstOrNull()?.toMap()

            output.map { it.split("").sorted().joinToString("").let { mappingSeq?.get(it) } }
                .joinToString("") { it.toString() }.toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
