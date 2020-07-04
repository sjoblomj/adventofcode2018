package org.sjoblomj.adventofcode

/**
 * Sensible default implementations to the Genetic Algorithm.
 */
abstract class GeneticsStandardImplementation<Individual, Properties : AlgorithmProperties>(val properties: Properties) :
  GeneticAlgorithm<Individual> {

  val bestIndividualInEachIteration = mutableListOf<Pair<Individual, Score>>()


  /**
   * Given a list of Individuals and their Scores, this will pick a subset of the population. The returned list will
   * consist of all the best scoring Individuals as well as a number of randomly chosen Individuals who are not among
   * the best scoring Individuals, but are chosen nonetheless.
   */
  override fun performSelection(fitnessScore: List<Pair<Individual, Score>>): List<Individual> {

    val numberOfTopPerformersToPick = properties.numberOfTopPerformersToPick
    val numberOfLuckyFewToPick = properties.numberOfLuckyFewToPick

    val sortedList = fitnessScore.sortedBy { it.second }

    val topPerformers = sortedList
      .take(numberOfTopPerformersToPick)
      .map { it.first }

    val luckyFew = sortedList
      .subList(numberOfTopPerformersToPick + 1, fitnessScore.size)
      .shuffled()
      .take(numberOfLuckyFewToPick)
      .map { it.first }

    return topPerformers + luckyFew
  }


  /**
   * Generate and return a population consisting of a defined number of Individuals.
   */
  override fun initialisePopulation(): List<Individual> {
    return (0 until properties.populationSize)
      .map { createIndividual() }
  }

  /**
   * Generate and return an Individual.
   */
  abstract fun createIndividual(): Individual


  /**
   * This function will assign a score to each individual in the population, denoting how good that individual is.
   * The lower the score, the better the solution.
   */
  override fun calculateFitnessFunction(population: List<Individual>): List<Pair<Individual, Score>> {
    val fitnessFunction = population.map { Pair(it, calculateFitnessScoreOfIndividual(it)) }

    // Save the best individual in the population, so that we know when the algorithm converges
    val bestIndividual = fitnessFunction.minBy { it.second }!!
    bestIndividualInEachIteration.add(bestIndividual)

    return fitnessFunction
  }

  /**
   * Calculate the Fitness Score of a single Individual.
   */
  abstract fun calculateFitnessScoreOfIndividual(individual: Individual): Score


  /**
   * Return true when the algorithm should stop. This will happen when the last n iterations have not produced any
   * change to the fitness score.
   */
  override fun populationHasConverged(population: List<Pair<Individual, Score>>): Boolean {
    return bestIndividualInEachIteration.size > properties.iterationsWithoutChangesBeforeConverging &&
      bestIndividualInEachIteration
        .takeLast(properties.iterationsWithoutChangesBeforeConverging)
        .map { it.second }
        .distinct()
        .size == 1
  }


  /**
   * Generate a new solution from the individuals that were selected to pass their genes on to the next generation.
   * Two individuals will be selected at random and produce one child, until we have reached a population of the
   * size we want.
   * Since the parents are chosen randomly, the same parent can spawn multiple children (with different partners),
   * and the same individual can simultaneously be BOTH the mother and father to a child.
   */
  override fun performCrossover(selectedPopulation: List<Individual>): List<Individual> {
    return (0 until properties.populationSize)
      .map { breed(selectedPopulation.random(), selectedPopulation.random()) }
  }

  /**
   * Create a new Individual by breeding the mother and the father.
   */
  abstract fun breed(mother: Individual, father: Individual): Individual
}

interface AlgorithmProperties {
  val populationSize: Int
  val numberOfTopPerformersToPick: Int
  val numberOfLuckyFewToPick: Int
  val iterationsWithoutChangesBeforeConverging: Int
}
