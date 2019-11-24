package view.genetic

import by.vkiva.model.view.Reproduction
import view.Plot
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

object Crossover {

    fun cross(reproductions: List<Reproduction>, crossoverProbability: Double): List<Chromosome> {
        val shuffled = reproductions.toMutableList().also { it.shuffle() }
        val crossedChromosomes = mutableListOf<Chromosome>()
        for (i in shuffled.indices) {
            if (i + 1 < shuffled.lastIndex && crossoverProbability > Random.nextDouble()) {
                crossedChromosomes.add(
                    Chromosome(
                        Plot.roundForCurrentPlot(recombination(shuffled[i].chromosome.x, shuffled[i + 1].chromosome.x)),
                        Plot.roundForCurrentPlot(recombination(shuffled[i].chromosome.y, shuffled[i + 1].chromosome.y))
                    )
                )
            } else {
                crossedChromosomes.add(shuffled[i].chromosome)
            }
        }
        return crossedChromosomes
    }

    private fun recombination(value1: Double, value2: Double): Double {
        val recombined = value1 + generateAlpha() * (value2 - value1)
        return when {
            recombined > Plot.T_MAX -> {
                Plot.T_MAX
            }
            recombined < Plot.T_MIN -> {
                Plot.T_MIN
            }
            else -> {
                recombined
            }
        }
    }

    private fun generateAlpha() = ThreadLocalRandom.current().nextDouble(-0.25, 1.25)
}