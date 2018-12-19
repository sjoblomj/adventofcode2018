package org.sjoblomj.adventofcode.day5

import org.junit.Test
import kotlin.test.assertEquals

class Day5Test {

  @Test fun `No unit pair`() {
    assertEquals("", reduce(""))
    assertEquals("A", reduce("A"))
  }

  @Test fun `Single unit pair - no reduction can be made`() {
    assertEquals("AA", reduce("AA"))
    assertEquals("aa", reduce("aa"))
    assertEquals("Ab", reduce("Ab"))
  }

  @Test fun `Single unit pair - can reduce`() {
    assertEquals("", reduce("Aa"))
    assertEquals("", reduce("aA"))
    assertEquals("", reduce("Bb"))
    assertEquals("", reduce("bB"))
  }

  @Test fun `Will trim whitespace`() {
    assertEquals("", reduce(" Aa "))
    assertEquals("", reduce("\naA\n"))
    assertEquals("", reduce("\tBb\t"))
    assertEquals("", reduce(" \t\nbB\n \t"))
  }

  @Test fun `Multiple unit pairs - no reduction can be made`() {
    assertEquals("abAB", reduce("abAB"))
    assertEquals("aabAAB", reduce("aabAAB"))
  }

  @Test fun `Multiple units pairs - can reduce`() {
    assertEquals("", reduce("abBA"))
    assertEquals("", reduce("aAbBAa"))
    assertEquals("dabCBAcaDA", reduce("dabAcCaCBAcCcaDA"))
  }

  @Test fun `Will remove most problematic unit`() {
    assertEquals("daDA", removeMostProblematicUnitAndReduce("dabAcCaCBAcCcaDA"))
  }
}
