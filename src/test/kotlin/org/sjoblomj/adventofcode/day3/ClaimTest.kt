package org.sjoblomj.adventofcode.day3

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ClaimTest {
  @Test fun `Parsing claim throws exception for empty input`() {
    assertFailsWith(IllegalArgumentException::class) {
      parseClaim("")
    }
  }

  @Test fun `Parsing claim throws exception for unparseable input`() {
    assertFailsWith(IllegalArgumentException::class) {
      parseClaim("apa bepa")
    }
  }

  @Test fun `Parsing proper claim works`() {
    val claim = parseClaim("#71 @ 240,664: 13x29")

    assertEquals(71, claim.id)
    assertEquals(240, claim.x0)
    assertEquals(664, claim.y0)
    assertEquals(240 + 13, claim.x1)
    assertEquals(664 + 29, claim.y1)
  }
}
