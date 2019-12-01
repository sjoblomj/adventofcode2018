package org.sjoblomj.adventofcode.day1

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Day1Tests {

  @Test fun `Can calculate frequency for empty input`() {
    assertEquals(0, calculateFrequency(emptyList()))
  }

  @Test fun `Calculating frequency for illegal input fails`() {
    assertFailsWith(IllegalArgumentException::class) {
      calculateFrequency(listOf("+1", "-apa"))
    }
  }

  @Test fun `Can calculate frequency`() {
    assertEquals(3, calculateFrequency(listOf("+1", "-2", "+3", "+1")))
    assertEquals(3, calculateFrequency(listOf("+1", "+1", "+1")))
    assertEquals(0, calculateFrequency(listOf("+1", "+1", "-2")))
    assertEquals(-6, calculateFrequency(listOf("-1", "-2", "-3")))
  }

  @Test fun `Finding repeated frequency for illegal input fails`() {
    assertFailsWith(IllegalArgumentException::class) {
      calculateFrequency(listOf("+1", "-apa"))
    }
  }

  @Test fun `Finding repeated frequency for empty input fails`() {
    assertFailsWith(IllegalArgumentException::class) {
      findRepeatedFrequency(emptyList())
    }
  }

  @Test fun `Can find repeated frequency`() {
    assertEquals(0, findRepeatedFrequency(listOf("+1", "-1")))
    assertEquals(10, findRepeatedFrequency(listOf("+3", "+3", "+4", "-2", "-4")))
    assertEquals(5, findRepeatedFrequency(listOf("-6", "+3", "+8", "+5", "-6")))
    assertEquals(14, findRepeatedFrequency(listOf("+7", "+7", "-2", "-7", "-4")))
    assertEquals(2, findRepeatedFrequency(listOf("+1", "-2", "+3", "+1", "+1", "-2")))
  }
}
