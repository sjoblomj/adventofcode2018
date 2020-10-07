package org.sjoblomj.adventofcode.day11

import org.sjoblomj.adventofcode.readFile
import kotlin.system.measureTimeMillis


private const val inputFile = "src/main/resources/inputs/day11.txt"
private const val defaultSquareSize = 3
private const val gridWidth = 300
private const val gridHeight = 300
private const val maxSquareSize = 300

inline fun <reified T> matrix2d(height: Int, width: Int, initialize: (Int, Int) -> T) =
  Array(height) { x -> Array(width) { y -> initialize.invoke(x, y) } }


fun day11() {
  println("== DAY 11 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay11() }
  println("Finished Day 11 in $timeTaken ms\n")
}

private fun calculateAndPrintDay11() {
  val gridSerialNumber = readFile(inputFile)[0].toInt()
  val squareOfSize3 = getBestSquareOfSize3(gridSerialNumber)
  val squareOfAnySize = getBestSquareOfAnySize(gridSerialNumber)
  println("X,Y coordinate of the best square of size $defaultSquareSize is ${squareOfSize3.topLeftX},${squareOfSize3.topLeftY}")
  println("X,Y,Size of the best square of any size is ${squareOfAnySize.topLeftX},${squareOfAnySize.topLeftY},${squareOfAnySize.squareSize}")
}

internal fun getBestSquareOfSize3(gridSerialNumber: Int) =
  getBestSquare(gridSerialNumber, defaultSquareSize) { squareSize -> squareSize == defaultSquareSize }

internal fun getBestSquareOfAnySize(gridSerialNumber: Int): Square {
  val squareSizeLimit = maxSquareSize
  return getBestSquare(gridSerialNumber, squareSizeLimit) { true }
}

private fun getBestSquare(gridSerialNumber: Int, squareSizeLimit: Int, squareSizeIncluder: (Int) -> Boolean): Square {

  val area = createArea(gridSerialNumber)
  val powerLinesHori = matrix2d(gridHeight, gridWidth) { _, _ -> 0 }
  val powerLinesVert = matrix2d(gridHeight, gridWidth) { _, _ -> 0 }
  val grid = matrix2d(gridHeight, gridWidth) { _, _ -> 0 }

  var bestSquare = Square(-1, -1, -1, Int.MIN_VALUE)

  for (squareSize in 1..squareSizeLimit) {
    for (x in 1 until gridWidth - squareSize) {
      for (y in 1 until gridHeight - squareSize) {

        val xsq = x - 1 + squareSize
        val ysq = y - 1 + squareSize

        powerLinesHori[x - 1][ysq] += area[xsq][ysq]
        powerLinesVert[xsq][y - 1] += area[xsq][ysq]

        val totalPower = grid[x - 1][y - 1] - area[xsq][ysq] +
          powerLinesHori[x - 1][ysq] + powerLinesVert[xsq][y - 1]

        grid[x - 1][y - 1] = totalPower

        if (totalPower > bestSquare.totalPower && squareSizeIncluder.invoke(squareSize)) {
          val square = Square(x + 1, y + 1, squareSize, totalPower)
          bestSquare = square
        }
      }
    }
  }

  return bestSquare
}


private fun createArea(gridSerialNumber: Int) =
  matrix2d(gridHeight, gridWidth) { x, y -> FuelCell(x + 1, y + 1, gridSerialNumber).powerLevel }


internal class Square(val topLeftX: Int, val topLeftY: Int, val squareSize: Int, val totalPower: Int)


internal class FuelCell(x: Int, private val y: Int, private val gridSerialNumber: Int) {
  private val rackId = x + 10
  val powerLevel = calculatePowerLevel()

  private fun calculatePowerLevel(): Int {
    val res = (rackId * y + gridSerialNumber) * rackId
    return ((res / 100) % 10) - 5
  }
}
