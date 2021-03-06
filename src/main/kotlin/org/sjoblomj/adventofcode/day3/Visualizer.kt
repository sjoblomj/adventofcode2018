package org.sjoblomj.adventofcode.day3

import org.sjoblomj.adventofcode.printJson

class Grid(val claims: List<Claim>, val cells: List<List<List<Int>>>)


internal fun visualizeArea(area: Area): String {
  var output = ""
  for (y in area.indices) {
    for (x in area[0].indices) {
      val claims = area[y][x]
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

internal fun createGrid(area: Array<Array<Claims>>, claims: List<Claim>): Grid {

  if (area.isEmpty() || claims.isEmpty()) {
    throw IllegalArgumentException("Expected non-empty area and list of claims")
  }

  val cellsWithIds = matrix2d(area.size, area[0].size) { emptyList<Int>() }
  for (y in area[0].indices)
    for (x in area.indices)
      cellsWithIds[x][y] = area[x][y].map { it.id }

  val cells = cellsWithIds.map { it.toList() }

  return Grid(claims, cells)
}

internal fun printJson(area: Area, claims: List<Claim>) = printJson(createGrid(area, claims))
