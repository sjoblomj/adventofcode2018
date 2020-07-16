package org.sjoblomj.adventofcode.day7.visualisation

import org.sjoblomj.adventofcode.Score
import org.sjoblomj.adventofcode.day7.Node
import org.sjoblomj.adventofcode.writeFile
import java.io.File

data class Coord(val col: Int, val row: Int)


fun visualise(nodes: List<Node>, resultingOrder: String) {
  val preReqs = nodes.convertToPreReq()

  val grid = orderNodesInGrid(preReqs)
  // grid has mapped every node id to an x and y coordinate.
  // It is, however, a dense graph that is not visually very pleasing.
  // When drawing lines between the nodes, it will be difficult to
  // make out the graph since the nodes are so close together,
  // and many lines will go through other nodes.
  //
  // Although each node id has a unique x and y coordinate, we wish
  // to update the y-coordinates so that the graph will look better.
  // Using a genetic algorithm, we can stochastically compute this.
  // The x coordinate for each node will be fixed, but the algorithm
  // will seek to find better y coordinates.


  val (solution, initialPopulation, bestIndividualInEachIteration) = runGeneticAlgorithm(preReqs, grid)

  createVisualisationFileOnHarddrive(nodes, resultingOrder, grid, solution, initialPopulation,
    bestIndividualInEachIteration)
}


/**
 * Given a list of nodes, this function will return the node ids mapped to x and y positions for that node.
 * The positions are chosen so that every node p that is a prerequisite for node q, the x-position of p will
 * always be before that of q. In other words, the nodes are placed in the order that they must be completed in.
 *
 * The positions can be seen as a grid of x and y coordinates, although it should be noted that the majority
 * of the positions in the grid will be empty. In the result, each id will deterministically have the same
 * x-position assigned to it each time the function is executed, but the y-position is subject to change.
 * A stochastic genetic algorithm is used to calculate y-positions, and the result is thus subject to randomness.
 */
internal fun orderNodesInGrid(nodes: List<PreReq>): Map<String, Coord> {
  val grid = HashMap<String, Coord>()
  var column = 0

  while (!nodes.all { grid.containsKey(it.id) }) {

    val nodesToBeAdded = nodes
      .filter { !grid.containsKey(it.id) }
      .filter { mapContainsEveryPrerequisite(it, grid) }

    for (node in nodesToBeAdded) {
      var row = 0
      while (grid.containsValue(Coord(column, row))) {
        row++
      }

      grid[node.id] = Coord(column, row)
    }
    column++
  }
  return grid
}

private fun mapContainsEveryPrerequisite(node: PreReq, grid: Map<String, Coord>) =
  getAllPrerequisites(node).all { grid.containsKey(it.id) }

private fun getAllPrerequisites(node: PreReq) = getAllPrerequisitesIncludingSelf(node).minus(node)

private fun getAllPrerequisitesIncludingSelf(node: PreReq): List<PreReq> {
  return node.prerequisites
    .flatMap { getAllPrerequisitesIncludingSelf(it) }
    .toMutableList()
    .plus(node)
}



fun createVisualisationFileOnHarddrive(nodes: List<Node>, resultingOrder: String, originalGrid: Map<String, Coord>,
                                       solution: Map<String, Coord>, initialPopulation: List<Map<String, Coord>>,
                                       bestIndividualInEachIteration: List<Pair<Map<String, Coord>, Score>>) {

  val content = Day7GeneticVisualiser::class.java.getResource("/visualisations/day7.html").readText()

  fun replaceLine(content: String, searchString: String, newValue: String): String {
    val stringIndex = content.indexOf(searchString)
    val indexOfPreviousNewline = content.substring(0, stringIndex).lastIndexOf("\n") + 1
    val indexOfNextNewline = content.indexOf("\n", stringIndex)
    return content.substring(0, indexOfPreviousNewline) + newValue + content.substring(indexOfNextNewline)
  }
  val linkData = nodes.flatMap { nodeRep -> nodeRep.dependers.map { dep -> "{from: \"${nodeRep.id}\", to: \"${dep.id}\"}" } }.toString()
  val nodeData = solution.map { "{id: \"${it.key}\", x: ${it.value.col + 1}, y: ${it.value.row + 1}}" }.toString()
  val originalNodeOrdering = originalGrid.map { "{id: \"${it.key}\", x: ${it.value.col + 1}, y: ${it.value.row + 1}}" }.toString()
  val initialPopulationData = initialPopulation.map { individual -> individual.map { "{id: \"${it.key}\", x: ${it.value.col + 1}, y: ${it.value.row + 1}}" }.toString() }
  val bestIndividual = bestIndividualInEachIteration.map { (individual, score) ->
    "{score: $score, individual: " + individual.map { "{id: \"${it.key}\", x: ${it.value.col + 1}, y: ${it.value.row + 1}}" }.toString() + "}"}

  var updatedContent = content
  updatedContent = replaceLine(updatedContent, "##VISUALIZER_RESULTING_ORDER##", "    var resultingOrder = \"$resultingOrder\";")
  updatedContent = replaceLine(updatedContent, "##VISUALIZER_LINK_DATA##", "    var linkData = $linkData;")
  updatedContent = replaceLine(updatedContent, "##VISUALIZER_SOLUTION##", "    var solution = $nodeData;")
  updatedContent = replaceLine(updatedContent, "##VISUALIZER_ORIGINAL_NODE_ORDERING##", "    var originalNodeOrdering = $originalNodeOrdering;")
  updatedContent = replaceLine(updatedContent, "##VISUALIZER_ORIGINAL_POPULATION##", "    var initialPopulation = $initialPopulationData;")
  updatedContent = replaceLine(updatedContent, "##VISUALIZER_BEST_INDIVIDUAL_IN_EACH_ITERATION##", "    var bestIndividualInEachIteration = $bestIndividual;")

  val dir = "./build/visualisations/"
  val filename = "day7.html"
  if (!File(dir).exists()) {
    assert(File(dir).mkdir())
  }
  writeFile(dir + filename, updatedContent)
}



data class PreReq(val id: String, val prerequisites: List<PreReq>)

internal fun Collection<Node>.convertToPreReq(): List<PreReq> {
  fun createPreReq(node: Node): PreReq = PreReq(node.id, node.prerequisites.map { createPreReq(it) } )
  return this.map { createPreReq(it) }
}
