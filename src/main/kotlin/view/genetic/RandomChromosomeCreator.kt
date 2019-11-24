package view.genetic

import view.Plot
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

object RandomChromosomeCreator {

    fun createRandomChromosome(): Chromosome {
        val randomValueX: Double = Plot.T_MIN + (Plot.T_MAX - Plot.T_MIN) * Random.nextDouble()
        val randomValueY: Double = Plot.T_MIN + (Plot.T_MAX - Plot.T_MIN) * Random.nextDouble()
        return Chromosome(
            x = BigDecimal(randomValueX).setScale(Plot.scale, RoundingMode.HALF_EVEN).toDouble(),
            y = BigDecimal(randomValueY).setScale(Plot.scale, RoundingMode.HALF_EVEN).toDouble()
        )
    }
}