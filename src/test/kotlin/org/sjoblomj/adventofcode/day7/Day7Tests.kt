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


  @Test fun `Can parallelize work`() {
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

    val numberOfWorkers = 2
    val baseTimeTakenForEachStep = 0

    val expected = "" +
      "Second 0 1    Done\n" +
      "0      C .    \n" +
      "1      C .    \n" +
      "2      C .    \n" +
      "3      A F    C\n" +
      "4      B F    CA\n" +
      "5      B F    CA\n" +
      "6      D F    CAB\n" +
      "7      D F    CAB\n" +
      "8      D F    CAB\n" +
      "9      D .    CABF\n" +
      "10     E .    CABFD\n" +
      "11     E .    CABFD\n" +
      "12     E .    CABFD\n" +
      "13     E .    CABFD\n" +
      "14     E .    CABFD\n" +
      "15     . .    CABFDE"

    val result = parallelize(nodes, numberOfWorkers, baseTimeTakenForEachStep)
    assertEquals(expected, visualiseParallelizedWork(result))
  }
}
