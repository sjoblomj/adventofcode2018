package org.sjoblomj.adventofcode.day3

import org.sjoblomj.adventofcode.readFile
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day3.txt"

typealias Claims = MutableList<Claim>
typealias Area = Array<Array<Claims>>

inline fun <reified T> matrix2d(height: Int, width: Int, initialize: () -> T) =
  Array(height) { Array(width) { initialize() } }


fun day3() {
  println("== DAY 3 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay3() }
  println("Finished Day 3 in $timeTaken ms\n")
}

private fun calculateAndPrintDay3() {
  val claims = readFile(inputFile).map { parseClaim(it) }
  val area = createArea(claims)
  println("Number of overlaps are ${calculateOverlaps(area)}")
  println("The ids of the claims that do not overlap with any other are ${findClaimsWithoutOverlaps(area, claims).map { it.id }}")
}


internal fun calculateOverlaps(area: Area): Int {
  var overlaps = 0
  for (y in 0 until area.size) {
    for (x in 0 until area[0].size) {
      overlaps += if (area[y][x].size > 1) 1 else 0
    }
  }
  return overlaps
}

internal fun findClaimsWithoutOverlaps(area: Area, claims: List<Claim>): List<Claim> {
  val overlaps = arrayListOf<Claim>()

  for (y in 0 until area.size) {
    for (x in 0 until area[0].size) {
      if (area[y][x].size > 1) {
        val unseenOverlaps = area[y][x].filter { !overlaps.contains(it) }
        overlaps.addAll(unseenOverlaps)
      }
    }
  }
  return claims.minus(overlaps)
}


internal fun createArea(claims: List<Claim>): Area {
  val width = claims.map { it.x1 }.max()?.plus(1)?: 1
  val height = claims.map { it.y1 }.max()?.plus(1)?: 1

  val area = matrix2d(height, width) { mutableListOf<Claim>() }
  for (claim in claims)
    for (x in claim.x0 until claim.x1)
      for (y in claim.y0 until claim.y1)
        addClaimToArea(area, claim, x, y)
  return area
}

private fun addClaimToArea(area: Area, claim: Claim, x: Int, y: Int) {
  if (area[y][x].isEmpty()) {
    area[y][x] = mutableListOf(claim)
  } else {
    area[y][x].add(claim)
  }
}
