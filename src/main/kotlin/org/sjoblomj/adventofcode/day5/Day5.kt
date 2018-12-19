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
  println("Units left after reactions: ${reduce(content).length}")
  println("Units left after reactions, once the most problematic unit has been removed: ${removeMostProblematicUnitAndReduce(content).length}")
}


internal fun removeMostProblematicUnitAndReduce(polymer: String): String {

  val allUnitsInPolymer = polymer
    .toLowerCase()
    .chars()
    .distinct()
    .mapToObj { unit -> unit.toChar() }
    .toList()

  return allUnitsInPolymer
    .map { removeSingleUnitFromPolymer(polymer, it) }
    .map { reduce(it) }
    .minBy { it.length }
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
