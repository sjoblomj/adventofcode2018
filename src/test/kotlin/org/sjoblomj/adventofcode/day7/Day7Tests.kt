package org.sjoblomj.adventofcode.day7

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Day7Tests {

  @Test fun `Will give exception if no node is without dependencies`() {
    assertFailsWith(RuntimeException::class) {
      val input = listOf(
        "Step C must be finished before step A can begin.",
        "Step A must be finished before step C can begin."
      )
      val nodes = createNodes(input)

      orderNodes(nodes)
    }
  }

  @Test fun `Can order nodes`() {
    val input = listOf(
      "Step C must be finished before step A can begin.",
      "Step C must be finished before step F can begin.",
      "Step A must be finished before step B can begin.",
      "Step A must be finished before step D can begin.",
      "Step B must be finished before step E can begin.",
      "Step F must be finished before step E can begin.",
      "Step D must be finished before step E can begin."
    )
    val nodes = createNodes(input)

    val result = orderNodes(nodes)

    assertEquals("CABDFE", result)
  }
}
