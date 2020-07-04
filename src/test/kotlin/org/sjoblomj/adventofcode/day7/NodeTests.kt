package org.sjoblomj.adventofcode.day7

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class NodeTests {
  @Test fun `parseLine -- illegal input`() {
    assertFailsWith(IllegalArgumentException::class) {
      parseLine("Apa bepa cepa")
    }
    assertFailsWith(IllegalArgumentException::class) {
      parseLine("Step € must be finished before step W can begin.")
    }
    assertFailsWith(IllegalArgumentException::class) {
      parseLine("Step L must be finished before step € can begin.")
    }
    assertFailsWith(IllegalArgumentException::class) {
      parseLine("Step  must be finished before step W can begin.")
    }
    assertFailsWith(IllegalArgumentException::class) {
      parseLine("Step L must be finished before step  can begin.")
    }
  }

  @Test fun `Can parseLine`() {
    val data = parseLine("Step L must be finished before step W can begin.")

    assertEquals("W", data.first)
    assertEquals("L", data.second)
  }

  @Test fun `Can create Nodes`() {
    val input = listOf(
      "Step C must be finished before step A can begin.",
      "Step C must be finished before step F can begin.",
      "Step A must be finished before step B can begin.",
      "Step A must be finished before step D can begin.",
      "Step B must be finished before step E can begin.",
      "Step F must be finished before step E can begin.",
      "Step D must be finished before step E can begin."
    )

    val result = createNodesFromInput(input)

    assertTrue(containsNode(result, "A"))
    assertTrue(containsNode(result, "B"))
    assertTrue(containsNode(result, "C"))
    assertTrue(containsNode(result, "D"))
    assertTrue(containsNode(result, "E"))
    assertTrue(containsNode(result, "F"))

    val nodeA = getNode(result, "A")
    val nodeB = getNode(result, "B")
    val nodeC = getNode(result, "C")
    val nodeD = getNode(result, "D")
    val nodeE = getNode(result, "E")
    val nodeF = getNode(result, "F")

    assertEquals(0, nodeC.prerequisites.size)

    assertEquals(1, nodeA.prerequisites.size)
    assertTrue(nodeA.prerequisites.contains(nodeC))

    assertEquals(1, nodeF.prerequisites.size)
    assertTrue(nodeF.prerequisites.contains(nodeC))

    assertEquals(1, nodeB.prerequisites.size)
    assertTrue(nodeB.prerequisites.contains(nodeA))

    assertEquals(1, nodeD.prerequisites.size)
    assertTrue(nodeD.prerequisites.contains(nodeA))

    assertEquals(3, nodeE.prerequisites.size)
    assertEquals(listOf(nodeB, nodeD, nodeF), nodeE.prerequisites)
  }

  private fun containsNode(nodes: List<Node>, id: String): Boolean {
    return try {
      getNode(nodes, id)
      true
    } catch (e: Exception) {
      false
    }
  }

  private fun getNode(nodes: List<Node>, id: String): Node {
    return nodes.filter { it.id == id }[0]
  }
}
