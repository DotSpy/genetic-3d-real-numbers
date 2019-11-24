package by.vkiva.model.view

import view.genetic.Chromosome

data class Population(
    var population: List<Chromosome>,
    val bestFitnessValue: Double,
    val middleFitnessValue: Double
)