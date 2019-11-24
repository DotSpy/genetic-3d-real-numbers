package view.genetic

import view.Plot
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.random.Random

object Mutator {

    fun mutate(chromosomes: List<Chromosome>, chanceToMutate: Double): List<Chromosome> {
        val mutatedChromosomes = mutableListOf<Chromosome>()
        for (chromosome in chromosomes) {
            mutatedChromosomes.add(
                Chromosome(
                    mutateValue(chanceToMutate, chromosome.x),
                    mutateValue(chanceToMutate, chromosome.y)
                )
            )
        }
        return mutatedChromosomes
    }

    private fun mutateValue(chanceToMutate: Double, value: Double): Double {
        var mutatedValue = value
        if (chanceToMutate > Random.nextDouble()) {
            mutatedValue = if (Math.random() < 0.5) {
                val candidate =
                    BigDecimal.valueOf(value + (0.5 * (Plot.T_MAX + Plot.T_MIN.absoluteValue) * getBetta()))
                        .setScale(Plot.scale, RoundingMode.HALF_EVEN).toDouble()
                if (candidate > Plot.T_MAX) {
                    Plot.T_MAX
                } else {
                    candidate
                }
            } else {
                val candidate =
                    BigDecimal.valueOf(value - (0.5 * (Plot.T_MAX + Plot.T_MIN.absoluteValue) * getBetta()))
                        .setScale(Plot.scale, RoundingMode.HALF_EVEN).toDouble()
                if (candidate < Plot.T_MIN) {
                    Plot.T_MIN
                } else {
                    candidate
                }
            }
        }
        return mutatedValue
    }

    private fun getBetta(m: Int = 20): Double {
        var result = 0.0
        for (i in 1..m) {
            if (1.0 / m < Random.nextDouble()) {
                result += 2.0.pow((-i).toDouble())
            }
        }
        return result
    }
}