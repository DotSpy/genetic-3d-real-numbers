package view.controller

import by.vkiva.model.view.Population
import view.genetic.Chromosome
import view.genetic.GeneticAlgorithm
import tornadofx.Controller

object GeneticController : Controller() {

    fun calculate(
        chromosomeCount: Int,
        crossProbability: Double,
        mutationProbability: Double,
        oldGeneration: List<Chromosome>
    ): Population =
        GeneticAlgorithm(
            chromosomeCount,
            crossProbability,
            mutationProbability
        ).solve(oldGeneration)

}