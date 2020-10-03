package org.sjoblomj.adventofcode.day8

import org.junit.Test
import kotlin.test.assertEquals

class Day8Tests {

  @Test fun `Can parse license file`() {
    val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"

    val tree = parseLicense(input)

    val result = visualise(input, tree)
    val expected = "" +
      "  2  3  0  3 10 11 12  1  1  0  1 99  2  1  1  2\n" +
      " #----------------------------------------------\n" +
      "       #------------- #----------------\n" +
      "                            #-------"
    assertEquals(expected, result)
  }

  @Test fun `Can calculate metadata sum for part 1`() {
    val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"
    val tree = parseLicense(input)

    val metadataSum = calculateMetadataSumPart1(tree)

    assertEquals(138, metadataSum)
  }

  @Test fun `Can calculate childless metadata sum for part 2`() {
    val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"
    val tree = parseLicense(input)

    val childFreeNodes = getTreeList(tree)
      .filter { it.children.isEmpty() }
    assertEquals(2, childFreeNodes.size)
    val nodeB = childFreeNodes.first { it.metadata.size == 3 }
    val nodeD = childFreeNodes.first { it.metadata.size == 1 }

    assertEquals(33, calculateMetadataForTree(nodeB))
    assertEquals(99, calculateMetadataForTree(nodeD))
  }

  @Test fun `Can calculate metadata sum for invalid pointers for part 2`() {
    val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"
    val tree = parseLicense(input)

    val nodesWithChildren = getTreeList(tree)
      .filter { it.children.isNotEmpty() }
    assertEquals(2, nodesWithChildren.size)
    val nodeC = nodesWithChildren.first { it.metadata.size == 1 }

    assertEquals(0, calculateMetadataForTree(nodeC))
  }

  @Test fun `Can calculate metadata sum for valid pointers for part 2`() {
    val input = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"
    val tree = parseLicense(input)

    val nodesWithChildren = getTreeList(tree)
      .filter { it.children.isNotEmpty() }
    assertEquals(2, nodesWithChildren.size)
    val nodeA = nodesWithChildren.first { it.metadata.size == 3 }

    assertEquals(66, calculateMetadataForTree(nodeA))
  }


  private fun getTreeList(tree: Tree): List<Tree> {
    return tree.children.flatMap { getTreeList(it) }.plus(tree)
  }
}
