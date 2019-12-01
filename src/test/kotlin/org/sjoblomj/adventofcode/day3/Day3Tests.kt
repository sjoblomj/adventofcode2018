package org.sjoblomj.adventofcode.day3

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day3Tests {
  @Test fun `Can create empty area`() {
    assertEquals(".\n", visualizeArea(createArea(mutableListOf())))
  }

  @Test fun `Can draw one claim on area`() {
    val claims = mutableListOf(parseClaim("#71 @ 3,2: 5x4"))
    val expected = "" +
      ".........\n" +
      ".........\n" +
      "...GGGGG.\n" +
      "...GGGGG.\n" +
      "...GGGGG.\n" +
      "...GGGGG.\n" +
      ".........\n"

    assertEquals(expected, visualizeArea(createArea(claims)))
  }

  @Test fun `Can draw two non-overlapping claims on area`() {
    val claims = mutableListOf(parseClaim("#71 @ 3,2: 5x4"), parseClaim("#68 @ 6,0: 2x2"))
    val expected = "" +
      "......DD.\n" +
      "......DD.\n" +
      "...GGGGG.\n" +
      "...GGGGG.\n" +
      "...GGGGG.\n" +
      "...GGGGG.\n" +
      ".........\n"

    assertEquals(expected, visualizeArea(createArea(claims)))
  }

  @Test fun `Can draw overlapping claims on area`() {
    val claims = mutableListOf(parseClaim("#71 @ 1,3: 4x4"), parseClaim("#68 @ 3,1: 4x4"), parseClaim("#78 @ 5,5: 2x2"))
    val expected = "" +
      "........\n" +
      "...DDDD.\n" +
      "...DDDD.\n" +
      ".GG##DD.\n" +
      ".GG##DD.\n" +
      ".GGGGNN.\n" +
      ".GGGGNN.\n" +
      "........\n"

    assertEquals(expected, visualizeArea(createArea(claims)))
  }

  @Test fun `Can calculate the correct amount of overlaps for no input`() {
    val claims = mutableListOf<Claim>()

    assertEquals(0, calculateOverlaps(createArea(claims)))
  }

  @Test fun `Can calculate the correct amount of overlaps for no overlaps`() {
    val claims = mutableListOf(parseClaim("#71 @ 3,2: 5x4"), parseClaim("#68 @ 6,0: 2x2"))

    assertEquals(0, calculateOverlaps(createArea(claims)))
  }

  @Test fun `Can calculate the correct amount of overlaps`() {
    val claims = mutableListOf(parseClaim("#71 @ 1,3: 4x4"), parseClaim("#68 @ 3,1: 4x4"), parseClaim("#78 @ 5,5: 2x2"))

    assertEquals(4, calculateOverlaps(createArea(claims)))
  }

  @Test fun `Returns empty list when there are no claims that could overlap`() {
    val claims = mutableListOf<Claim>()

    val result = findClaimsWithoutOverlaps(createArea(claims), claims)

    assertEquals(0, result.size)
  }

  @Test fun `Returns empty list when no claims overlap`() {
    val claims = mutableListOf(parseClaim("#71 @ 3,2: 5x4"), parseClaim("#68 @ 6,0: 2x3"))

    val result = findClaimsWithoutOverlaps(createArea(claims), claims)

    assertEquals(0, result.size)
  }

  @Test fun `Can find the id of the one claim that has no overlaps`() {
    val claims = mutableListOf(parseClaim("#71 @ 1,3: 4x4"), parseClaim("#68 @ 3,1: 4x4"), parseClaim("#78 @ 5,5: 2x2"))

    val result = findClaimsWithoutOverlaps(createArea(claims), claims)

    assertEquals(1, result.size)
    assertEquals(78, result[0].id)
  }

  @Test fun `Can find the ids of the claims that has no overlaps`() {
    val claims = mutableListOf(parseClaim("#71 @ 1,3: 4x4"), parseClaim("#68 @ 3,1: 4x4"), parseClaim("#78 @ 5,5: 2x2"), parseClaim("#79 @ 0,0: 1x1"))

    val result = findClaimsWithoutOverlaps(createArea(claims), claims)

    assertEquals(2, result.size)
    assertTrue(result.map { it.id }.contains(78))
    assertTrue(result.map { it.id }.contains(79))
  }
}
