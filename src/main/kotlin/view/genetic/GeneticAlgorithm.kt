package view.genetic

import by.vkiva.model.view.Population

class GeneticAlgorithm(
    private val chromosomeCount: Int,
    private val crossProbability: Double,
    private val mutationProbability: Double
) {

    private val population: MutableList<Chromosome> = mutableListOf()

    fun solve(oldPopulation: List<Chromosome>): Population {
        return if (oldPopulation.isEmpty()) {
            for (i in 0 until chromosomeCount) {
                population.add(RandomChromosomeCreator.createRandomChromosome())
            }
            FitnessFunction(mutationProbability, crossProbability).fit(population)
        } else {
            FitnessFunction(mutationProbability, crossProbability).fit(oldPopulation)
        }
    }
}