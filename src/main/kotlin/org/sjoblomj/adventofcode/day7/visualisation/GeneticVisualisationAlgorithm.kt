package org.sjoblomj.adventofcode.day7.visualisation

import org.sjoblomj.adventofcode.AlgorithmProperties
import org.sjoblomj.adventofcode.GeneticsStandardImplementation
import org.sjoblomj.adventofcode.Score
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random


/**
 * Given a mapping between nodes and their x and y positions, we wish to update the y positions.
 * Using a genetic algorithm, we can stochastically compute this. The x coordinate for each node
 * will be fixed, but the algorithm will seek to find better y coordinates.
 */
fun runGeneticAlgorithm(nodes: List<PreReq>, grid: Grid): Triple<Grid, List<Grid>, MutableList<Pair<Grid, Score>>> {

  // Number of individuals that are created in each iteration
  val populationSize = 128

  // Biggest y-position possible. Allows the graph to take up more vertical space than that of the current grid
  val maxRows = getMaxRowInGrid(grid) * 3

  // This is not really a parameter to be configured, but is rather used for convenience
  val maxCols = getMaxColInGrid(grid)

  // Number of the best individuals that will pass their genes to the next generation
  val numberOfTopPerformersToPick = (populationSize * 0.2).toInt()

  // Number of lucky individuals that will also pass their genes to the next generation, despite not being best suited
  val numberOfLuckyFewToPick = (populationSize * 0.05).toInt()

  // When this many iterations of the algorithm has been without the fittest individual changing, we consider the
  // solution to have converged and stop iterating.
  val iterationsWithoutChangesBeforeConverging = 16

  // The penalty added for placing a node k that depends on node l in a position so that the line between k and l
  // passes straight through node m (horizontally or diagonally). We add a penalty for this since this obscures the
  // resulting graph.
  val penaltyForGoingThroughNode = 150

  // Percent chance that a mutation will occur in a node
  val chanceOfMutation = 0.01


  val properties = Day7GeneticVisualiserProperties(
    populationSize,
    numberOfTopPerformersToPick,
    numberOfLuckyFewToPick,
    iterationsWithoutChangesBeforeConverging,
    grid,
    nodes,
    maxRows,
    maxCols,
    penaltyForGoingThroughNode,
    chanceOfMutation
  )
  val geneticVisualizer = Day7GeneticVisualiser(properties)

  val solutions = geneticVisualizer.generateGeneticSolution()
  val solution = removeDeadSpace(solutions.first())


  return Triple(solution, geneticVisualizer.initialPopulation, geneticVisualizer.bestIndividualInEachIteration)
}


class Day7GeneticVisualiser(properties: Day7GeneticVisualiserProperties) :
  GeneticsStandardImplementation<Grid, Day7GeneticVisualiserProperties>(properties) {

  internal lateinit var initialPopulation: List<Grid>
  private val primeNumbers = getAllPrimesLessThan(max(properties.maxCols, properties.maxRows)) // Precalculate for optimization reasons


  /**
   * Generate a population where the ids and columns are the same for each Grid, but the rows are randomised.
   */
  override fun initialisePopulation(): List<Grid> {
    initialPopulation = super.initialisePopulation()
    return initialPopulation
  }

  /**
   * The Grids returned from this function will always have the same ids and columns, but the rows are randomised.
   */
  override fun createIndividual(): Grid {
    val randomizedGrid = newGrid()

    val randomizedNewRowIndexes = (0 .. properties.maxCols).map { (0 .. properties.maxRows).shuffled() }
    for ((nodeId, coord) in properties.originalNodeGrid) {
      val col = coord.col
      val row = randomizedNewRowIndexes[col][coord.row]
      randomizedGrid[nodeId] = Coord(col, row)
    }
    return randomizedGrid
  }


  /**
   * This function will assign a score to a Grid, denoting how good that Grid is. The Score will be the sum of the
   * euclidean distance between every connected node. Each time a line between nodes a and b pass straight through
   * another node c (horizontally or diagonally), a penalty is added to the number. The lower the number the better
   * the solution.
   */
  override fun calculateFitnessScoreOfIndividual(individual: Grid): Score {
    var sum = 0.0
    for ((nodeId, coord) in individual) {
      val node = properties.nodeList.first { it.id == nodeId }
      for (prerequisite in node.prerequisites) {
        val prereqCoord = individual[prerequisite.id] ?: error("Failed to find Prerequisite node $prerequisite")

        sum += calculateEuclideanDistance(coord, prereqCoord)

        if (lineGoesThroughOtherNode(individual, coord, prereqCoord)) {
          sum += properties.penaltyForGoingThroughNode
        }
      }
    }
    return sum
  }

  private fun calculateEuclideanDistance(q: Coord, p: Coord): Double {
    fun pow(a: Int, b: Int) = (a - b).toDouble().pow(2.0)
    return sqrt(pow(q.row, p.row) + pow(q.col, p.col))
  }

  private fun lineGoesThroughOtherNode(grid: Grid, coord: Coord, prereqCoord: Coord): Boolean {

    return getAllNodesBetween(coord, prereqCoord)
      .mapNotNull { getEntryAtCoord(grid, it) }
      .isNotEmpty()
  }

  private fun getAllNodesBetween(coord: Coord, prereqCoord: Coord): List<Coord> {

    return if (coordsArePlacedInLine(coord, prereqCoord)) {

      val colsInBetween = (prereqCoord.col + 1) until coord.col
      colsInBetween.map { Coord(it, coord.row) }

    } else {

      val slopeDeltaX = coord.col - prereqCoord.col
      val slopeDeltaY = coord.row - prereqCoord.row
      val (dy, dx) = reduceFraction(slopeDeltaY, slopeDeltaX)

      generateSequence(1, { it + 1 })
        .map { Coord(prereqCoord.col + (it * dx), prereqCoord.row + (it * dy)) }
        .takeWhile { it != coord }
        .toList()
    }
  }

  private fun coordsArePlacedInLine(c0: Coord, c1: Coord) = c0.row == c1.row

  internal fun reduceFraction(m: Int, n: Int): Pair<Int, Int> {
    if (m == 0 || n == 0) {
      return m to n
    }

    val mSign = if (m < 0 && n >= 0) -1 else 1
    val nSign = if (n < 0 && m >= 0) -1 else 1
    val mFactors = factorize(abs(m))
    val nFactors = factorize(abs(n))

    val mReduced = mFactors.subtractUnique(nFactors).product()
    val nReduced = nFactors.subtractUnique(mFactors).product()
    return mSign*mReduced to nSign*nReduced
  }

  internal fun factorize(n: Int): List<Int> {
    if (n < 2) {
      return emptyList()
    }
    val primeFactors = mutableListOf<Int>()
    val primes = if (primeNumbers.last() < n) getAllPrimesLessThan(n) else primeNumbers

    var m = n
    while (primeFactors.product() != n) {
      for (prime in primes) {
        if (m % prime == 0) {
          primeFactors.add(prime)
          m /= prime
        }
      }
    }

    return primeFactors
  }

  private fun getAllPrimesLessThan(max: Int): List<Int> {
    val primes = mutableListOf<Int>()
    for (n in listOf(2).plus(3 .. max step 2)) {
      if (primes.none { prime -> n % prime == 0 }) {
        primes.add(n)
      }
    }
    return primes
  }

  private fun <T> List<T>.subtractUnique(other: List<T>): List<T> {
    val elements = mutableListOf<T>()
    for (elem in this) {
      if (!elements.contains(elem)) {
        val countThis = this.count { it == elem }
        val countOther = other.count { it == elem }
        if (countThis > countOther)
          elements.addAll(List(countThis - countOther) { elem })
      }
    }
    return elements
  }

  private fun Collection<Int>.product(): Int {
    return this.fold(1) { product, element -> product * element }
  }


  /**
   * Create a new grid by combining two others. The ids and columns are always fixed and will be the same on every
   * individual, only the row positions change. From the mother, the child inherits the row positions of her first
   * column. The child then inherits the row positions of the second column of its father. This is repeated, so that
   * the row positions of every even column comes from the mother and the row positions of every odd column
   * comes from the father.
   */
  override fun breed(mother: Grid, father: Grid): Grid {
    val child = newGrid()

    // Go over every column. Take one column from the mother and the next from the father.
    for (col in (0 .. properties.maxCols)) {
      val parent = listOf(mother, father)[col % 2] // Alternate between parents
      parent
        .filter { it.value.col == col }
        .forEach { child[it.key] = it.value }
    }
    return child
  }


  /**
   * To prevent the algorithm from getting stuck in a suboptimal local minimum, we inflict random mutations to
   * some individuals in the population. The mutation consists of randomly selecting a node. The node will get a
   * new row position that is randomly chosen. If there already is another node at that place, the nodes swap places.
   */
  override fun performMutation(population: List<Grid>): List<Grid> {

    return population
      .map { it.toMutableMap() }
      .map { grid ->
        for (entry in grid.entries) {
          if (Random.nextDouble() < properties.chanceOfMutation) {
            swapCoordinates(entry.key, entry.value, grid)
          }
        }
        grid
      }
  }

  private fun swapCoordinates(id: String, coord: Coord, grid: MutableMap<String, Coord>) {
    val col = coord.col
    val newRow = Random.nextInt(properties.maxRows)

    val entryToSwap = getEntryAtCoord(grid, Coord(col, newRow))
    if (entryToSwap != null) {
      grid[entryToSwap.first] = Coord(col, coord.row)
    }
    grid[id] = Coord(col, newRow)
  }


  private fun getEntryAtCoord(grid: Grid, coord: Coord) =
    grid
      .filter { gridVal -> gridVal.value == coord }
      .toList()
      .firstOrNull()
}


private fun getMinRowInGrid(grid: Grid) =
  grid.map { it.value.row }.min() ?: error("Failed to find smallest row")

internal fun getMaxRowInGrid(grid: Grid) =
  grid.map { it.value.row }.max() ?: error("Failed to find biggest row")

internal fun getMaxColInGrid(grid: Grid) =
  grid.map { it.value.col }.max() ?: error("Failed to find biggest col")


/**
 * If the node with the lowest row position x is > 0, we wish to subtract x from every single node,
 * so that the graph starts at row 0.
 */
private fun removeDeadSpace(grid: Grid): Grid {
  val minRowInGrid = getMinRowInGrid(grid)
  return grid
    .map { (id, coord) -> id to Coord(coord.col, coord.row - minRowInGrid) }
    .toMap()
}


data class Day7GeneticVisualiserProperties(
  override val populationSize: Int,
  override val numberOfTopPerformersToPick: Int,
  override val numberOfLuckyFewToPick: Int,
  override val iterationsWithoutChangesBeforeConverging: Int,
  val originalNodeGrid: Grid,
  val nodeList: List<PreReq>,
  val maxRows: Int,
  val maxCols: Int,
  val penaltyForGoingThroughNode: Int,
  val chanceOfMutation: Double
) : AlgorithmProperties

private typealias Grid = Map<String, Coord>
private fun newGrid() = HashMap<String, Coord>()
