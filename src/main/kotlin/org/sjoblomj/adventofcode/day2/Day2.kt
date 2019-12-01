package org.sjoblomj.adventofcode.day2

import org.sjoblomj.adventofcode.readFile
import java.util.stream.IntStream
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day2.txt"


fun day2() {
  println("== DAY 2 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay2() }
  println("Finished Day 2 in $timeTaken ms\n")
}

private fun calculateAndPrintDay2() {
  val content = readFile(inputFile)
  println("Resulting checksum is ${calculateChecksum(content)}")
  println("Common part of the strings where exactly one character differs between them: ${findCommonPartOfStringsWithOneCharDifferences(content)}")
}

internal fun containsExactlyTwoIdenticalLetters(line: String) = containsExactlyNIdenticalLetters(line, 2)

internal fun containsExactlyThreeIdenticalLetters(line: String) = containsExactlyNIdenticalLetters(line, 3)

private fun containsExactlyNIdenticalLetters(line: String, n: Int): Boolean {
  val charMap = mutableMapOf<Char, Int>()
  for (char: Char in line.toCharArray())
    charMap[char] = charMap[char]?.plus(1) ?: 1
  return charMap.containsValue(n)
}

internal fun calculateChecksum(input: List<String>): Int {
  val twos = input.filter { containsExactlyTwoIdenticalLetters(it) }.size
  val threes = input.filter { containsExactlyThreeIdenticalLetters(it) }.size
  return twos * threes
}

internal fun exactlyOneCharDiffers(s0: String, s1: String): Boolean {

  var numberOfDifferences = 0

  for (i in s0.indices) {
    if (s0[i] != s1[i])
      numberOfDifferences++
    if (numberOfDifferences > 1)
      return false
  }
  return numberOfDifferences == 1
}


internal fun findCommonPartOfStringsWithOneCharDifferences(input: List<String>): String? {

  if (input.map { it.length }.distinct().size != 1)
    throw IllegalArgumentException("Expected all lines to have the same length")

  for (i in input.indices)
    for (j in i + 1 until input.size)
      if (exactlyOneCharDiffers(input[i], input[j]))
        return findCommonPart(input[i], input[j])
  return null
}

private fun findCommonPart(s0: String, s1: String): String {

  return IntStream.range(0, s0.length)
    .filter { s0[it] == s1[it] }
    .mapToObj { s0[it] }
    .toArray()
    .joinToString("")
}
