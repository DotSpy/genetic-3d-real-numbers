package by.vkiva.model.view

import view.genetic.Chromosome

data class Reproduction(
    var chromosome: Chromosome,
    var fitnessValue: Double,
    var normalizedValue: Double? = null,
    var expectedChromosomeCount: Double? = null,
    var realChromosomeCount: Int? = null
)