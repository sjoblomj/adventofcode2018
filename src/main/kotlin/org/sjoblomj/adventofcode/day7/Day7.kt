package org.sjoblomj.adventofcode.day7

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sjoblomj.adventofcode.day7.visualisation.visualise
import org.sjoblomj.adventofcode.readFile
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day7.txt"


fun day7() {
  println("== DAY 7 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay7() }
  println("Finished Day 7 in $timeTaken ms\n")
}

private fun calculateAndPrintDay7() {
  val nodes = createNodes(readFile(inputFile))
  println("Resulting order is ${orderNodes(nodes)}")
}

fun createNodes(input: List<String>): List<Node> {
  return createNodesFromInput(input)
}

internal fun orderNodes(nodes: List<Node>): String {
  val orderedNodes = mutableListOf<Node>()
  for (firstNode in getFirstNodes(nodes))
    orderNodesRecursively(firstNode, orderedNodes)

  val resultingOrder = orderedNodes.joinToString("") { it.id }
  output(orderedNodes, resultingOrder)
  return resultingOrder
}

private fun orderNodesRecursively(node: Node, orderedNodes: MutableList<Node>) {
  if (allDependenciesFulfilled(node, orderedNodes) && !orderedNodes.contains(node)) {
    orderedNodes.add(node)
    for (depender in node.dependers.sortedBy { it.id })
      orderNodesRecursively(depender, orderedNodes)
  }
}

private fun allDependenciesFulfilled(node: Node, orderedNodes: List<Node>) =
  orderedNodes.containsAll(node.prerequisites)

fun getFirstNodes(nodes: List<Node>): List<Node> {
  val firstNodes = nodes.filter { it.prerequisites.isEmpty() }.sortedBy { it.id }

  if (firstNodes.isEmpty())
    throw RuntimeException("Should find node that has no dependencies")
  return firstNodes
}


fun output(nodes: List<Node>, resultingOrder: String) {
  GlobalScope.launch { visualise(nodes, resultingOrder) }
}
