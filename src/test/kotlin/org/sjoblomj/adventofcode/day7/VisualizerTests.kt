package org.sjoblomj.adventofcode.day7

import org.junit.Test
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class VisualizerTests {

  @Test
  fun `Two nodes, One level deep`() {
    val inputNodes = createInputText(listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A"))
    ))

    val result = orderNodesInGrid(inputNodes)

    assertEquals(2, result.size)
    assertEquals(Coord(0, 0), result["A"])
    assertEquals(Coord(1, 0), result["B"])
  }

  @Test
  fun `Three nodes, One level deep`() {
    val inputNodes = createInputText(listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A")),
      NodeRep("C", dependsOn = listOf("A"))
    ))

    val result = orderNodesInGrid(inputNodes)

    assertEquals(3, result.size)
    assertEquals(Coord(0, 0), result["A"])
    assertEquals(Coord(1, 0), result["B"])
    assertEquals(Coord(1, 1), result["C"])
  }

  @Test
  fun `Four nodes, One level deep`() {
    val inputNodes = createInputText(listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A")),
      NodeRep("C", dependsOn = listOf("A")),
      NodeRep("D", dependsOn = listOf("A"))
    ))

    val result = orderNodesInGrid(inputNodes)

    assertEquals(4, result.size)
    assertEquals(Coord(0, 0), result["A"])
    assertEquals(Coord(1, 0), result["B"])
    assertEquals(Coord(1, 1), result["C"])
    assertEquals(Coord(1, 2), result["D"])
  }

  @Test
  fun `Five nodes, One level deep, Two branches`() {
    val inputNodes = createInputText(listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A")),
      NodeRep("C", dependsOn = listOf("A")),
      NodeRep("D", dependsOn = nothing()),
      NodeRep("E", dependsOn = listOf("D"))
    ))

    val result = orderNodesInGrid(inputNodes)

    assertEquals(5, result.size)
    assertEquals(Coord(0, 0), result["A"])
    assertEquals(Coord(1, 0), result["B"])
    assertEquals(Coord(1, 1), result["C"])
    assertEquals(Coord(0, 1), result["D"])
    assertEquals(Coord(1, 2), result["E"])
  }

  @Test
  fun `Four nodes, Two levels deep`() {
    val inputNodes = createInputText(listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A")),
      NodeRep("C", dependsOn = listOf("A")),
      NodeRep("D", dependsOn = listOf("B", "C"))
    ))

    val result = orderNodesInGrid(inputNodes)

    assertEquals(4, result.size)
    assertEquals(Coord(0, 0), result["A"])
    assertEquals(Coord(1, 0), result["B"])
    assertEquals(Coord(1, 1), result["C"])
    assertEquals(Coord(2, 0), result["D"])
  }

  @Test
  fun `Seven nodes, Four levels deep`() {
    val inputNodes = createInputText(listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A")),
      NodeRep("C", dependsOn = listOf("A")),
      NodeRep("D", dependsOn = listOf("B", "C")),
      NodeRep("E", dependsOn = listOf("D")),
      NodeRep("F", dependsOn = listOf("D")),
      NodeRep("G", dependsOn = listOf("E", "F"))
    ))

    val result = orderNodesInGrid(inputNodes)

    assertEquals(7, result.size)
    assertEquals(Coord(0, 0), result["A"])
    assertEquals(Coord(1, 0), result["B"])
    assertEquals(Coord(1, 1), result["C"])
    assertEquals(Coord(2, 0), result["D"])
    assertEquals(Coord(3, 0), result["E"])
    assertEquals(Coord(3, 1), result["F"])
    assertEquals(Coord(4, 0), result["G"])
  }


  @Test
  fun `Eight nodes, Four levels deep`() {
    val inputNodes = createInputText(listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("Z", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A")),
      NodeRep("C", dependsOn = listOf("A")),
      NodeRep("D", dependsOn = listOf("B", "C", "Z")),
      NodeRep("E", dependsOn = listOf("D")),
      NodeRep("F", dependsOn = listOf("D")),
      NodeRep("G", dependsOn = listOf("E", "F"))
    ))

    val result = orderNodesInGrid(inputNodes)

    assertEquals(8, result.size)
    assertEquals(Coord(0, 0), result["A"])
    assertEquals(Coord(1, 0), result["B"])
    assertEquals(Coord(1, 1), result["C"])
    assertEquals(Coord(2, 0), result["D"])
    assertEquals(Coord(3, 0), result["E"])
    assertEquals(Coord(3, 1), result["F"])
    assertEquals(Coord(4, 0), result["G"])
  }


  @Test
  fun `Test Visualizer -- Fitness function`() {
    val penalty = 50
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes, penalty)

    val result = geneticVisualizer.calculateFitnessFunction(listOf(nodeGrid))[0].second

    val expectedEuclideanDistances =
      1*4 + // 4 straight neighbouring distances of length 1
        sqrt(2.0)*4 + // 4 diagonal distances
        2*1 + // 1 distance of length 2, from Z to D
        sqrt(4.0.pow(2) + 2.0.pow(2))*1 + // A to G
        sqrt(2.0.pow(2) + 2.0.pow(2))*1 // B to F
    val expectedPenalties = 3 * penalty

    val expected = expectedEuclideanDistances + expectedPenalties
    assertTrue(abs(expected - result) < 0.0000000000001)
  }

  @Test
  fun `Test Visualizer -- Initialize Population`() {
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes)

    val result = geneticVisualizer.initialisePopulation()

    assertEquals(2, result.size)
    assertIndividualsDiffer(nodeGrid, result[0])
    assertIndividualsDiffer(nodeGrid, result[1])
  }

  @Test
  fun `Test Visualizer -- mutation`() {
    val chanceOfMutation = 1.0 // Always mutate
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes, 1, chanceOfMutation)

    val result = geneticVisualizer.performMutation(listOf(nodeGrid))

    assertEquals(1, result.size)
    assertIndividualsDiffer(nodeGrid, result[0])
  }

  @Test
  fun `Test Visualizer -- crossover`() {
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes)
    val mutatedNodeGrid = geneticVisualizer.performMutation(listOf(nodeGrid))[0]

    val result = geneticVisualizer.performCrossover(listOf(nodeGrid, mutatedNodeGrid))

    assertEquals(2, result.size)
    val individual0 = result[0]
    val individual1 = result[1]
    assertEquals(individual0.size, individual1.size)
    individual0.forEach { (id, coord) -> assertEquals(coord.col, individual1[id]?.col) }
  }

  @Test
  fun `Test Visualizer -- breed`() {
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes)
    val mutatedNodeGrid = geneticVisualizer.performMutation(listOf(nodeGrid))[0]

    val result = geneticVisualizer.breed(nodeGrid, mutatedNodeGrid)

    assertEquals(nodeGrid.size, result.size)
    nodeGrid.forEach { (id, coord) -> assertEquals(coord.col, result[id]?.col) }
    nodeGrid
      .filterValues { it.col % 2 == 0 }
      .forEach { (id, coord) -> assertEquals(coord.row, result[id]?.row) }
    mutatedNodeGrid
      .filterValues { it.col % 2 == 1 }
      .forEach { (id, coord) -> assertEquals(coord.row, result[id]?.row) }
  }

  @Test
  fun `Test Visualizer -- populationHasConverged -- too few iterations`() {
    val iterationsWithoutChangesBeforeConverging = 5
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes, 1, 1.0, iterationsWithoutChangesBeforeConverging)

    assertEquals(0, geneticVisualizer.bestIndividualInEachIteration.size)
    assertFalse(geneticVisualizer.populationHasConverged(geneticVisualizer.calculateFitnessFunction(listOf(nodeGrid))), "There should have been too few iterations")
  }

  @Test
  fun `Test Visualizer -- populationHasConverged -- has not converged`() {
    val iterationsWithoutChangesBeforeConverging = 5
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes, 1, 1.0, iterationsWithoutChangesBeforeConverging)

    val nodeGridWithFitnessScore = geneticVisualizer.calculateFitnessFunction(listOf(nodeGrid))[0]
    val iterations = List(iterationsWithoutChangesBeforeConverging + 1) { nodeGridWithFitnessScore }
    geneticVisualizer.bestIndividualInEachIteration.addAll(iterations)
    val mutatedNodeGridWithFitnessScore = geneticVisualizer.calculateFitnessFunction(geneticVisualizer.performMutation(listOf(nodeGrid)))[0]
    geneticVisualizer.bestIndividualInEachIteration.addAll(listOf(mutatedNodeGridWithFitnessScore))

    assertEquals(9, geneticVisualizer.bestIndividualInEachIteration.size)
    assertFalse(geneticVisualizer.populationHasConverged(geneticVisualizer.calculateFitnessFunction(listOf(nodeGrid))), "The last few iterations are not the same")
  }

  @Test
  fun `Test Visualizer -- populationHasConverged -- has converged`() {
    val iterationsWithoutChangesBeforeConverging = 5
    val (nodes, nodeGrid) = createNodeGrid()
    val geneticVisualizer = createVisualizer(nodeGrid, nodes, 1, 1.0, iterationsWithoutChangesBeforeConverging)

    val nodeGridWithFitnessScore = geneticVisualizer.calculateFitnessFunction(listOf(nodeGrid))[0]
    val iterations = List(iterationsWithoutChangesBeforeConverging + 1) { nodeGridWithFitnessScore }
    geneticVisualizer.bestIndividualInEachIteration.addAll(iterations)

    assertEquals(7, geneticVisualizer.bestIndividualInEachIteration.size)
    assertTrue(geneticVisualizer.populationHasConverged(geneticVisualizer.calculateFitnessFunction(listOf(nodeGrid))))
  }


  @Test
  fun `Can factorize`() {
    val geneticVisualizer = createVisualizer()

    assertEquals(emptyList(), geneticVisualizer.factorize(0).sorted())
    assertEquals(emptyList(), geneticVisualizer.factorize(1).sorted())
    assertEquals(listOf(2), geneticVisualizer.factorize(2).sorted())
    assertEquals(listOf(3), geneticVisualizer.factorize(3).sorted())
    assertEquals(listOf(2, 2), geneticVisualizer.factorize(4).sorted())
    assertEquals(listOf(5), geneticVisualizer.factorize(5).sorted())
    assertEquals(listOf(2, 3), geneticVisualizer.factorize(6).sorted())
    assertEquals(listOf(7), geneticVisualizer.factorize(7).sorted())
    assertEquals(listOf(2, 2, 5), geneticVisualizer.factorize(5*2*2).sorted())
    assertEquals(listOf(3, 7), geneticVisualizer.factorize(21).sorted())
  }

  @Test
  fun `Can reduce fraction`() {
    val geneticVisualizer = createVisualizer()

    assertEquals(2 to 0, geneticVisualizer.reduceFraction(2, 0))
    assertEquals(0 to 2, geneticVisualizer.reduceFraction(0, 2))

    assertEquals(2 to 3, geneticVisualizer.reduceFraction(2, 3))
    assertEquals(5 to 2, geneticVisualizer.reduceFraction(5, 2))
    assertEquals(2 to 1, geneticVisualizer.reduceFraction(2, 1))

    assertEquals(2 to 1, geneticVisualizer.reduceFraction(4, 2))
    assertEquals(1 to 2, geneticVisualizer.reduceFraction(2, 4))
    assertEquals(1 to 1, geneticVisualizer.reduceFraction(2, 2))
    assertEquals(1 to 2, geneticVisualizer.reduceFraction(5, 10))
    assertEquals(5 to 1, geneticVisualizer.reduceFraction(15, 3))
    assertEquals(5 to 7, geneticVisualizer.reduceFraction(15, 21))
    assertEquals(5 to -7, geneticVisualizer.reduceFraction(15, -21))
    assertEquals(-5 to 7, geneticVisualizer.reduceFraction(-15, 21))
    assertEquals(5 to 7, geneticVisualizer.reduceFraction(-15, -21))
  }

  private fun createVisualizer(): Day7GeneticVisualizer {
    val properties = Day7GeneticVisualizerProperties(
      2, 1, 1, 1, HashMap(), emptyList(),
      1, 1, 1, 0.0
    )
    return Day7GeneticVisualizer(properties)
  }

  private fun createVisualizer(nodeGrid: HashMap<String, Coord>, nodes: List<Node>, penalty: Int = 1,
                               chanceOfMutation: Double = 0.0, iterationsWithoutChangesBeforeConverging: Int = 1): Day7GeneticVisualizer {

    val properties = Day7GeneticVisualizerProperties(
      2, 1, 1, iterationsWithoutChangesBeforeConverging, nodeGrid, nodes,
      getMaxRowInGrid(nodeGrid), getMaxColInGrid(nodeGrid), penalty, chanceOfMutation
    )
    return Day7GeneticVisualizer(properties)
  }
}

private fun createNodeGrid(): Pair<List<Node>, HashMap<String, Coord>> {
  val nodes = createInputText(
    listOf(
      NodeRep("A", dependsOn = nothing()),
      NodeRep("Z", dependsOn = nothing()),
      NodeRep("B", dependsOn = listOf("A")),
      NodeRep("C", dependsOn = listOf("A")),
      NodeRep("D", dependsOn = listOf("B", "C", "Z")),
      NodeRep("E", dependsOn = listOf("D")),
      NodeRep("F", dependsOn = listOf("B", "D")),
      NodeRep("G", dependsOn = listOf("A", "E", "F"))
    )
  )
  val nodeGrid = HashMap<String, Coord>().also {
    it["A"] = Coord(0, 0)
    it["B"] = Coord(1, 0)
    it["Z"] = Coord(0, 1)
    it["C"] = Coord(1, 1)
    it["D"] = Coord(2, 1)
    it["E"] = Coord(3, 1)
    it["F"] = Coord(3, 2)
    it["G"] = Coord(4, 2)
  }
  return Pair(nodes, nodeGrid)
}

private fun assertIndividualsDiffer(individual0: Map<String, Coord>, individual1: Map<String, Coord>) {
  assertEquals(individual0.size, individual1.size)
  individual0.forEach { (id, coord) -> assertEquals(coord.col, individual1[id]?.col) }
  val originalRows = individual0.map { (_, coord) -> coord.row }
  val resultingRows = individual1.map { (_, coord) -> coord.row }
  assertNotEquals(originalRows, resultingRows, "There is a tiny minuscule risk that these might be equal, but that is neglectable")
}

fun createInputText(nodeReps: List<NodeRep>): List<Node> {
  val stringList = mutableListOf<String>()
  for (nodeRep in nodeReps)
    for (dep in nodeRep.dependsOn)
      stringList.add("Step $dep must be finished before step ${nodeRep.id} can begin.")
  return createNodesFromInput(stringList)
}

private fun nothing() = emptyList<String>()

data class NodeRep(val id: String, val dependsOn: List<String>)
