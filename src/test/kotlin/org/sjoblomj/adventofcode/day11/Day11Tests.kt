package org.sjoblomj.adventofcode.day11

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class Day11Tests {

  @Test fun `Can calculate Fuel Cell Power Level`() {
    val fuelCell0 = FuelCell(3, 5, 8)
    assertEquals(4, fuelCell0.powerLevel)

    val fuelCell1 = FuelCell(122, 79, 57)
    assertEquals(-5, fuelCell1.powerLevel)

    val fuelCell2 = FuelCell(217, 196, 39)
    assertEquals(0, fuelCell2.powerLevel)

    val fuelCell3 = FuelCell(101, 153, 71)
    assertEquals(4, fuelCell3.powerLevel)
  }

  @Test fun `Can calculate best square of size 3`() {
    val square0 = getBestSquareOfSize3(18)
    assertNotNull(square0)
    assertEquals(29, square0.totalPower)
    assertEquals(33, square0.topLeftX)
    assertEquals(45, square0.topLeftY)

    val square1 = getBestSquareOfSize3(42)
    assertNotNull(square1)
    assertEquals(30, square1.totalPower)
    assertEquals(21, square1.topLeftX)
    assertEquals(61, square1.topLeftY)
  }

  @Test fun `Can calculate best square of any size`() {
    val square0 = getBestSquareOfAnySize(18)
    assertNotNull(square0)
    assertEquals(113, square0.totalPower)
    assertEquals(16, square0.squareSize)
    assertEquals(90, square0.topLeftX)
    assertEquals(269, square0.topLeftY)

    val square1 = getBestSquareOfAnySize(42)
    assertNotNull(square1)
    assertEquals(119, square1.totalPower)
    assertEquals(12, square1.squareSize)
    assertEquals(232, square1.topLeftX)
    assertEquals(251, square1.topLeftY)
  }
}
