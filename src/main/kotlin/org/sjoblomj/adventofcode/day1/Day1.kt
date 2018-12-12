package org.sjoblomj.adventofcode.day1

import org.sjoblomj.adventofcode.readFile
import java.lang.IllegalArgumentException
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day1.txt"


fun day1() {
  println("== DAY 1 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay1() }
  println("Finished Day 1 in $timeTaken ms\n")
}

private fun calculateAndPrintDay1() {
  val content = readFile(inputFile)
  println("Resulting frequency is ${calculateFrequency(content)}")
  println("First repeating frequency is ${findRepeatedFrequency(content)}")
}

fun calculateFrequency(input: List<String>): Int = fileContentToInts(input).sum()


fun findRepeatedFrequency(input: List<String>): Int {
  if (input.isEmpty())
    throw IllegalArgumentException("Expected input")

  var frequency = 0
  val frequencies = mutableListOf(frequency)
  val fileContent = fileContentToInts(input)

  var i = 0
  while (true) {
    for (num: Int in fileContent) {
      frequency += num
      if (frequencies.contains(frequency)) {
        System.out.printf("\r")
        return frequency
      }
      frequencies.add(frequency)
    }
    System.out.printf("\rFinding repeated frequency. Iteration %d", i++)
  }
}

private fun fileContentToInts(input: List<String>) = input.map { Integer.parseInt(it) }
