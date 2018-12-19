package org.sjoblomj.adventofcode.day3

import org.junit.Test
import org.sjoblomj.adventofcode.printJson
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class VisualizerTest {
  @Test fun `Empty areas gives exceptions`() {
    assertFailsWith(IllegalArgumentException::class) {
      createGrid(emptyArray(), emptyList())
    }

    assertFailsWith(IllegalArgumentException::class) {
      createGrid(emptyArray(), listOf(Claim(1, 1, 1, 1, 1)))
    }

    assertFailsWith(IllegalArgumentException::class) {
      val area = matrix2d(1, 1) { mutableListOf<Claim>() }
      createGrid(area, emptyList())
    }
  }

  @Test fun `Can create area with one element`() {
    val claims = listOf(Claim(71, 0, 0, 1, 1))
    val area = createArea(claims)
    val expectedVisualization = "" +
      "G.\n" +
      "..\n"


    val grid = createGrid(area, claims)


    val expectedJson = "{\"cells\" : [" +
      "[[71], " + "[]], " +
      "[[], "   + "[]]" +
      "], \"claims\" : [" +
      "{\"id\" : 71, \"x0\" : 0, \"x1\" : 1, \"y0\" : 0, \"y1\" : 1}" +
      "]}"
    assertEquals(1, grid.claims.size)
    assertEquals(2, grid.cells.size)
    assertEquals(2, grid.cells[0].size)
    assertEquals(expectedVisualization, visualizeArea(area))
    assertEquals(expectedJson, printJson(grid))
  }

  @Test fun `Can create area with two elements without overlaps`() {
    val claims = listOf(Claim(71, 0, 0, 1, 1), Claim(68, 0, 1, 1, 2))
    val area = createArea(claims)
    val expectedVisualization = "" +
      "G.\n" +
      "D.\n" +
      "..\n"


    val jsonResult = printJson(area, claims)


    val expectedJson = "{\"cells\" : [" +
      "[[71], " + "[]], " +
      "[[68], " + "[]], " +
      "[[], "   + "[]]"   +
      "], \"claims\" : [" +
      "{\"id\" : 71, \"x0\" : 0, \"x1\" : 1, \"y0\" : 0, \"y1\" : 1}, " +
      "{\"id\" : 68, \"x0\" : 0, \"x1\" : 1, \"y0\" : 1, \"y1\" : 2}" +
      "]}"
    assertEquals(expectedVisualization, visualizeArea(area))
    assertEquals(expectedJson, jsonResult)
  }

  @Test fun `Can create area with two elements with overlaps`() {
    val claims = listOf(Claim(71, 0, 0, 1, 2), Claim(68, 0, 1, 1, 3))
    val area = createArea(claims)
    val expectedVisualization = "" +
      "G.\n" +
      "#.\n" +
      "D.\n" +
      "..\n"


    val resultingJson = printJson(area, claims)


    val expectedJson = "{\"cells\" : [" +
      "[[71], "     + "[]], " +
      "[[71, 68], " + "[]], " +
      "[[68], "     + "[]], " +
      "[[], "       + "[]]"   +
      "], \"claims\" : [" +
      "{\"id\" : 71, \"x0\" : 0, \"x1\" : 1, \"y0\" : 0, \"y1\" : 2}, " +
      "{\"id\" : 68, \"x0\" : 0, \"x1\" : 1, \"y0\" : 1, \"y1\" : 3}" +
      "]}"
    assertEquals(expectedVisualization, visualizeArea(area))
    assertEquals(expectedJson, resultingJson)
  }

  @Test fun `Can create area with and without overlaps`() {
    val claims = listOf(parseClaim("#71 @ 1,3: 4x4"), parseClaim("#68 @ 3,1: 4x4"), parseClaim("#78 @ 5,5: 2x2"))
    val area = createArea(claims)
    val expectedVisualization = "" +
      "........\n" +
      "...DDDD.\n" +
      "...DDDD.\n" +
      ".GG##DD.\n" +
      ".GG##DD.\n" +
      ".GGGGNN.\n" +
      ".GGGGNN.\n" +
      "........\n"


    val resultingJson = printJson(area, claims)


    val expectedJson = "{\"cells\" : [" +
      "[[], [], [], [], [], [], [], []], " +
      "[[], [], [], [68], [68], [68], [68], []], " +
      "[[], [], [], [68], [68], [68], [68], []], " +
      "[[], [71], [71], [71, 68], [71, 68], [68], [68], []], " +
      "[[], [71], [71], [71, 68], [71, 68], [68], [68], []], " +
      "[[], [71], [71], [71], [71], [78], [78], []], " +
      "[[], [71], [71], [71], [71], [78], [78], []], " +
      "[[], [], [], [], [], [], [], []]" +
      "], \"claims\" : [" +
      "{\"id\" : 71, \"x0\" : 1, \"x1\" : 5, \"y0\" : 3, \"y1\" : 7}, " +
      "{\"id\" : 68, \"x0\" : 3, \"x1\" : 7, \"y0\" : 1, \"y1\" : 5}, " +
      "{\"id\" : 78, \"x0\" : 5, \"x1\" : 7, \"y0\" : 5, \"y1\" : 7}" +
      "]}"
    assertEquals(expectedVisualization, visualizeArea(area))
    assertEquals(expectedJson, resultingJson)
  }
}
