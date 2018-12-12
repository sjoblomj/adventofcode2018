package org.sjoblomj.adventofcode

import org.junit.Test
import kotlin.test.assertEquals

class UtilTest {
  @Test fun `readFile can handle non-existing files`() {
    assertEquals(emptyList<String>(), readFile("src/to/file/that/doesn't/exist.txt"))
  }

  @Test fun `readFile returns list`() {
    assertEquals(listOf("+1", "+1", "-2"), readFile("src/test/resources/inputs/test.txt"))
  }
}
