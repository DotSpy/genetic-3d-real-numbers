package view.genetic

import by.vkiva.model.view.Population
import by.vkiva.model.view.Reproduction
import view.Plot
import java.util.*

class FitnessFunction(
    private val mutateChance: Double,
    private val crossoverProbability: Double
) {

    fun fit(chromosomes: List<Chromosome>): Population =
        Function().produceNextGeneration(chromosomes, mutateChance, crossoverProbability)

    private class Function {

        private val reproductionTable = mutableListOf<Reproduction>()
        private var middleValue = 0.0
        private var sumOfFitness = 0.0
        private var minOfFitness = Double.MAX_VALUE

        fun produceNextGeneration(
            chromosomes: List<Chromosome>,
            mutateChance: Double,
            crossoverProbability: Double
        ): Population {
            for (chromosome in chromosomes) {
                val fitness = Plot.getZValue(chromosome.x, chromosome.y)
                sumOfFitness += fitness
                if (fitness < minOfFitness) {
                    minOfFitness = fitness
                }
                reproductionTable.add(Reproduction(chromosome, fitness))
            }
            middleValue = sumOfFitness / chromosomes.size
            for (reproduction in reproductionTable) {
                val normalizedValue = reproduction.fitnessValue / sumOfFitness
                reproduction.normalizedValue = normalizedValue
                reproduction.expectedChromosomeCount = normalizedValue * chromosomes.size
            }
            val newGeneration = tournamentSelection()
            val crossedGeneration = Crossover.cross(newGeneration, crossoverProbability)
            val mutatedChromosomes = Mutator.mutate(crossedGeneration, mutateChance)
            val bestAndMiddleValueOfFitness = getBestAndMiddleValueOfFitness()
            return Population(mutatedChromosomes, bestAndMiddleValueOfFitness.first, bestAndMiddleValueOfFitness.second)
        }

        private fun getBestAndMiddleValueOfFitness(): Pair<Double, Double> {
            var bestFitness = Double.MIN_VALUE
            var middleFitness = 0.0
            for (reproduction in reproductionTable) {
                if (reproduction.fitnessValue > bestFitness) {
                    bestFitness = reproduction.fitnessValue
                }
                middleFitness += reproduction.fitnessValue
            }
            return Pair(bestFitness, middleFitness / reproductionTable.size)
        }

        private fun tournamentSelection(candidateNumber: Int = 3): List<Reproduction> {
            if (reproductionTable.size < candidateNumber) {
                throw IllegalStateException("Candidate number $candidateNumber > than population size ${reproductionTable.size}")
            }
            val winners = mutableListOf<Reproduction>()
            var best: Reproduction
            for (index in reproductionTable.indices) {
                best = reproductionTable[Random().nextInt(reproductionTable.size)]
                for (i in 1 until candidateNumber) {
                    val candidate = reproductionTable[Random().nextInt(reproductionTable.size)]
                    if (best.fitnessValue > candidate.fitnessValue) {
                        best = candidate
                    }
                }
                winners.add(best)
            }
            return winners
        }
    }
}