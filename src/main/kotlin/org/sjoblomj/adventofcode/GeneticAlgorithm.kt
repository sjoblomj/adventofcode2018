package org.sjoblomj.adventofcode

/**
 * A Genetic Algorithm is inspired by natural selection and evolution to find solutions to a problem. A list of possible
 * solutions are evaluated, and the ones giving the best result are selected as the basis for creating new solutions,
 * which are hopefully even better. In each iteration of the algorithm, the solutions are ranked using a Fitness
 * Function which gives each solution a Score, denoting how well it performs.
 *
 * A possible solution is referred to as an Individual, and a list of solutions is called a Population.
 *
 * A Population is initialised and evaluated using the Fitness Function. The best Individuals are selected and breed
 * a new set of Individuals. Some random mutations are introduced in order for the algorithm not to converge at a
 * suboptimal local minimum. This new set of Individuals are ranked using the Fitness Function, until it is determined
 * that the algorithm converges. When that happens, the current population is returned, sorted by their Fitness Score.
 */
interface GeneticAlgorithm<Individual> {

  /**
   * Will generate a population (list of Individuals) that consists of the Individuals who are the fittest.
   * The result is calculated by using the standard Genetic Algorithm.
   */
  fun generateGeneticSolution(): List<Individual> {
    var population: List<Individual> = initialisePopulation()
    var fitnessScore: List<Pair<Individual, Score>> = calculateFitnessFunction(population)

    while (!populationHasConverged(fitnessScore)) {
      population = performSelection(fitnessScore)
      population = performCrossover(population)
      population = performMutation(population)
      fitnessScore = calculateFitnessFunction(population)
    }

    return fitnessScore.sortedBy { it.second }.map { it.first }
  }

  /**
   * Initialise the population.
   */
  fun initialisePopulation(): List<Individual>

  /**
   * This function will assign a Score to each Individual in the population, denoting how good that individual is.
   * The lower the Score, the better the solution.
   */
  fun calculateFitnessFunction(population: List<Individual>): List<Pair<Individual, Score>>

  /**
   * Returns true when the population is considered to have converged. This will make the algorithm return the
   * best population.
   */
  fun populationHasConverged(population: List<Pair<Individual, Score>>): Boolean

  /**
   * Given a list of Individuals and their Scores, this will pick a subset of the population.
   */
  fun performSelection(fitnessScore: List<Pair<Individual, Score>>): List<Individual>

  /**
   * Generate a new solution from the Individuals that were selected to pass their genes on to the next generation.
   * Individuals will be selected to breed children, until we have reached a population of the size we want.
   */
  fun performCrossover(selectedPopulation: List<Individual>): List<Individual>

  /**
   * To prevent the algorithm from getting stuck in a suboptimal local minimum, we inflict random mutations to
   * some Individuals in the population.
   */
  fun performMutation(population: List<Individual>): List<Individual>
}


/**
 * A number denoting how fit an Individual is. The lower the score, the better.
 */
typealias Score = Double
