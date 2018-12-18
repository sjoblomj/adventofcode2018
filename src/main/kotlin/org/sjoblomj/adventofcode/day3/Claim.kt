package org.sjoblomj.adventofcode.day3

data class Claim(val id: Int, val x0: Int, val y0: Int, val x1: Int, val y1: Int)

internal fun parseClaim(claim: String): Claim {
  val destructuredRegex = "#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)".toRegex()

  return destructuredRegex.matchEntire(claim)
    ?.destructured
    ?.let { (id, x0, y0, width, height) ->
      Claim(id.toInt(), x0.toInt(), y0.toInt(), x0.toInt() + width.toInt(), y0.toInt() + height.toInt())
    }
    ?: throw IllegalArgumentException("Bad input '$claim'")
}
