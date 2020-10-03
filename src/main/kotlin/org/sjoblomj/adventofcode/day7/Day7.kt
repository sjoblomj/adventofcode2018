package org.sjoblomj.adventofcode.day7

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.sjoblomj.adventofcode.day7.visualisation.visualise
import org.sjoblomj.adventofcode.readFile
import kotlin.system.measureTimeMillis

private const val inputFile = "src/main/resources/inputs/day7.txt"
private const val numberOfWorkers = 5
private const val baseTimeTakenForEachStep = 60


fun day7() {
  println("== DAY 7 ==")
  val timeTaken = measureTimeMillis { calculateAndPrintDay7() }
  println("Finished Day 7 in $timeTaken ms\n")
}

private fun calculateAndPrintDay7() {
  val nodes = createNodes(readFile(inputFile))
  println("Resulting order is ${orderNodes(nodes)}")

  val parallelizedWork = parallelize(nodes, numberOfWorkers, baseTimeTakenForEachStep)
  println("With parallelization, it would take ${parallelizedWork.size - 1} seconds to finish")
}

internal fun createNodes(input: List<String>): List<Node> {
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

private fun allDependenciesFulfilled(node: Node, finishedNodes: List<Node>) =
  finishedNodes.containsAll(node.prerequisites)

private fun getFirstNodes(nodes: List<Node>): List<Node> {
  val firstNodes = nodes.filter { it.prerequisites.isEmpty() }.sortedBy { it.id }

  if (firstNodes.isEmpty())
    throw RuntimeException("Should find node that has no dependencies")
  return firstNodes
}


internal fun parallelize(nodes: List<Node>, numberOfWorkers: Int, baseTimeTakenForEachStep: Int): List<WorkOrder> {
  val workers = generateAvailableWorkers(numberOfWorkers, baseTimeTakenForEachStep)

  return parallelize(nodes.sortedBy { it.id }, 0, workers, emptyList()).reversed()
}

private fun parallelize(nodes: List<Node>, iteration: Int, workers: List<Worker>, result: List<Node>): List<WorkOrder> {

  val nodesThatWereJustFinished = workers.filter { it.isFinished(iteration) }.mapNotNull { it.node }
  val finishedNodes = result.plus(nodesThatWereJustFinished)

  if (nodes.isEmpty() && workers.all { it.isFinished(iteration) })
    return listOf(WorkOrder(generateAvailableWorkers(workers.size, workers.first().baseTimeTakenForEachStep), finishedNodes))

  val newWorkerList = createNewWorkerList(nodes, finishedNodes, workers, iteration)

  return parallelize(getNodesNotBeingWorkedOn(newWorkerList, nodes), iteration + 1, newWorkerList, finishedNodes).plus(WorkOrder(newWorkerList, finishedNodes))
}

private fun createNewWorkerList(nodes: List<Node>, finishedNodes: List<Node>, workers: List<Worker>, iteration: Int): List<Worker> {

  val nodesReadyForProcessing = nodes.filter { allDependenciesFulfilled(it, finishedNodes) }
  var nodesReadyForProcessingIndex = 0

  return workers.map {
    if (it.isFinished(iteration)) {

      val node = if (nodesReadyForProcessingIndex < nodesReadyForProcessing.size) nodesReadyForProcessing[nodesReadyForProcessingIndex] else null
      nodesReadyForProcessingIndex++
      Worker(node, iteration, it.baseTimeTakenForEachStep)
    } else
      it
  }
}

private fun getNodesNotBeingWorkedOn(newWorkerList: List<Worker>, nodes: List<Node>): List<Node> {
  val nodesBeingWorkedOn = newWorkerList.mapNotNull { it.node }
  return nodes.filter { !nodesBeingWorkedOn.contains(it) }
}

private fun generateAvailableWorkers(numberOfWorkers: Int, baseTimeTakenForEachStep: Int) =
  generateSequence { Worker(null, null, baseTimeTakenForEachStep) }.take(numberOfWorkers).toList()


internal data class WorkOrder(val workers: List<Worker>, val result: List<Node>)

internal data class Worker(val node: Node?, val startedAt: Int?, val baseTimeTakenForEachStep: Int) {
  private fun delayForJob() = if (node == null) -1 else node.id[0].toInt() - 'A'.toInt() + 1
  fun isFinished(currentIteration: Int) = if (startedAt == null || node == null) true else startedAt + baseTimeTakenForEachStep + delayForJob() <= currentIteration
}


internal fun visualiseParallelizedWork(workOrders: List<WorkOrder>): String {
  val numberOfWorkers = workOrders.map { it.workers.size }.max()
  val workerLabels = (0 until (numberOfWorkers ?: 0)).joinToString(" ")
  val secondLabel = "Second"
  val label = "$secondLabel $workerLabels    Done"

  val lines = workOrders.mapIndexed { index, workOrder ->
    val formattedIndex = index.toString().padEnd(secondLabel.length)
    val underWork = workOrder.workers.map { it.node?.id ?: "." }

    "$formattedIndex ${underWork.joinToString(" ")}    ${workOrder.result.joinToString("") { it.id }}"
  }
  return label + "\n" + lines.joinToString("\n")
}

private fun output(nodes: List<Node>, resultingOrder: String) {
  GlobalScope.launch { visualise(nodes, resultingOrder) }
}
