package org.sjoblomj.adventofcode.day3

import org.sjoblomj.adventofcode.readFile
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day3.txt"

typealias Claims = List<Claim>
typealias Area = Array<Array<Claims>>

inline fun <reified T> matrix2d(height: Int, width: Int, initialize: () -> T) =
  Array(height) { Array(width) { initialize() } }


fun day3() {
  println("== DAY 3 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay3() }
  println("Finished Day 3 in $timeTaken ms\n")
}

fun calculateAndPrintDay3() {
  val claims = readFile(inputFile).map { parseClaim(it) }
  val area = createArea(claims)
  println("Number of overlaps are ${calculateOverlaps(area)}")
  println("The ids of the claims that do not overlap are ${findClaimsWithoutOverlaps(area, claims).map { it.id }}")
}


fun calculateOverlaps(area: Area): Int {
  var overlaps = 0
  for (y in 0 until area[0].size) {
    for (x in 0 until area.size) {
      overlaps += if (area[x][y].size > 1) 1 else 0
    }
  }
  return overlaps
}

fun findClaimsWithoutOverlaps(area: Area, claims: List<Claim>): List<Claim> {
  val overlaps = arrayListOf<Claim>()

  for (y in 0 until area[0].size) {
    for (x in 0 until area.size) {
      if (area[x][y].size > 1) {
        val unseenOverlaps = area[x][y].filter { !overlaps.contains(it) }
        overlaps.addAll(unseenOverlaps)
      }
    }
  }
  return claims.minus(overlaps)
}


fun createArea(claims: List<Claim>): Area {
  val width = claims.map { it.x1 }.max()?.plus(1)?: 1
  val height = claims.map { it.y1 }.max()?.plus(1)?: 1

  /*
  val x0sort = claims.sortedBy { it.x0 }
  val x1sort = claims.sortedBy { it.x1 }
  val y0sort = claims.sortedBy { it.y0 }
  val y1sort = claims.sortedBy { it.y1 }
  */

  val area = matrix2d(width, height) { emptyList<Claim>() }
  for (y in 0 until height) {
    for (x in 0 until width) {
      /*
      val x0s = x0sort.filter { x >= it.x0 }
      val x1s = x1sort.filter { x <  it.x1 }
      val y0s = y0sort.filter { y >= it.y0 }
      val y1s = y1sort.filter { y <  it.y1 }

      area[x][y] = x0s.filter { x1s.contains(it) && y0s.contains(it) && y1s.contains(it) }
      */
      area[x][y] = claims.filter { claimIsOnCoord(x, y, it) }
    }
    System.out.printf("\rCreating area - %d%%", (100 * y / height))
  }
  System.out.printf("\r")
  return area
}


fun visualizeArea(area: Area): String {
  var output = ""
  for (y in 0 until area[0].size) {
    for (x in 0 until area.size) {

      val claims = area[x][y]
      output += when {
        claims.isEmpty() -> "."
        claims.size == 1 -> claims[0].id.toChar()
        else             -> "#"
      }
    }
    output += "\n"
  }
  return output
}

private fun claimIsOnCoord(x: Int, y: Int, c: Claim): Boolean {
  return x >= c.x0 && x < c.x1 &&
         y >= c.y0 && y < c.y1
}
