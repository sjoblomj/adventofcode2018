package org.sjoblomj.adventofcode.day5

import java.io.File
import kotlin.streams.toList
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day5.txt"


fun day5() {
  println("== DAY 5 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay5() }
  println("Finished Day 5 in $timeTaken ms\n")
}

private fun calculateAndPrintDay5() {
  val content = File(inputFile).readText()
  val reducedPolymer = reduce(content)
  println("Units left after reactions: ${reducedPolymer.length}")
  println("Units left after reactions, once the most problematic unit has been removed: ${removeMostProblematicUnitAndReduce(reducedPolymer).length}")
}


internal fun removeMostProblematicUnitAndReduce(originalPolymer: String): String {

  val allUnitsInPolymer = originalPolymer
    .toLowerCase()
    .chars()
    .distinct()
    .mapToObj { unit -> unit.toChar() }
    .toList()

  return allUnitsInPolymer
    .map { unit -> removeSingleUnitFromPolymer(originalPolymer, unit) }
    .map { polymer -> reduce(polymer) }
    .minBy { reducedPolymer -> reducedPolymer.length }
  ?: throw RuntimeException("Unable to find shortest polymer")
}

internal fun reduce(s: String): String {
  var polymer = s.trim()
  var i = 0

  while (i < polymer.length - 1) {
    val unitPair = nextUnitPair(polymer, i)

    if (unitsReact(unitPair)) {
      polymer = polymer.replace(unitPair, "")
      i = if (i > 0) i - 1 else 0
    } else {
      i++
    }
  }

  return polymer
}


private fun nextUnitPair(polymer: String, i: Int) = "${polymer[i]}${polymer[i + 1]}"

private fun unitsReact(unitPair: String) =
  unitPair[0].toLowerCase() == unitPair[1].toLowerCase() &&
    ((unitPair[0].isUpperCase() && unitPair[1].isLowerCase()) ||
     (unitPair[0].isLowerCase() && unitPair[1].isUpperCase()))

private fun removeSingleUnitFromPolymer(polymer: String, unit: Char) = polymer.replace(unit.toString(), "", true)
