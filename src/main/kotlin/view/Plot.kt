package view

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import view.model.Point2D
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.cos
import kotlin.math.pow

/*
rastring y = 20 + Sum(i=1..2, Xi^2 - 10*cos(pi*Xi))
 */
object Plot {

    const val scale = 2
    const val TICK = 0.01
    const val T_MIN = -5.12 + TICK
    const val T_MAX = 5.12 - TICK

    private val points: MutableMap<Point2D, Double> = mutableMapOf()
    val max: Double
    val min: Double
    val rastringLevels: MutableMap<Int, Set<Point2D>> = mutableMapOf()

    init {
        var currentMax = Double.MIN_VALUE
        var currentMin = Double.MAX_VALUE
        val sortedLevels: MutableMap<Int, Set<Point2D>> = mutableMapOf()
        runBlocking {
            val one = async {
                calculatePoints(T_MIN, T_MAX)
            }
//            val two = async {
//                calculatePoints(-3.0, -1.0)
//            }
//            val three = async {
//                calculatePoints(-1.0, 2.0)
//            }
//            val four = async {
//                calculatePoints(2.0, Plot.T_MAX)
//            }
            listOf(one/*, two, three, four*/)
                .map { it.await() }
                .forEach {
                    points.putAll(it.points)
                    if (it.max > currentMax) {
                        currentMax = it.max
                    }
                    if (it.min < currentMin) {
                        currentMin = it.min
                    }
                    rastringLevels.putAll(it.rastringLevels)
                }
        }
        max = currentMax
        min = currentMin
    }

    fun getPoints() = points

    fun getZValue(x: Double, y: Double) = points.getValue(Point2D(x, y))

    fun roundForCurrentPlot(value: Double) =
        BigDecimal.valueOf(value).setScale(scale, RoundingMode.HALF_EVEN).toDouble()

    private fun calculatePoints(x1From: Double, x1To: Double): CalculationResult {
        val points = mutableMapOf<Point2D, Double>()
        var max = Double.MIN_VALUE
        var min = Double.MAX_VALUE
        val levels = mutableMapOf<Int, MutableSet<Point2D>>()
        var currentStepX1 = x1From
        while (currentStepX1 <= x1To) {
            val x1Value = currentStepX1.pow(2) - (10 * cos(Math.PI * currentStepX1))
            var currentStepX2 = T_MIN
            while (currentStepX2 <= T_MAX) {
                val x2Value = currentStepX2.pow(2) - (10 * cos(Math.PI * currentStepX2))
                val currentValue = 20 + x1Value + x2Value
                points[Point2D(currentStepX1, currentStepX2)] = currentValue
                if (currentValue > max) {
                    max = currentValue
                }
                if (currentValue < min) {
                    min = currentValue
                }
                val decimalPart = currentValue.toInt()
                for (level in 10..60 step 10) {
                    if (decimalPart == level) {
                        levels.getOrPut(level, { mutableSetOf(Point2D(currentStepX1, currentStepX2)) })
                            .add(Point2D(currentStepX1, currentStepX2))
                    }
                }
                currentStepX2 =
                    BigDecimal.valueOf(TICK + currentStepX2).setScale(scale, RoundingMode.HALF_EVEN).toDouble()
            }
            currentStepX1 = BigDecimal.valueOf(TICK + currentStepX1).setScale(scale, RoundingMode.HALF_EVEN).toDouble()
        }
        return CalculationResult(points, max, min, levels)
    }

    data class CalculationResult(
        val points: Map<Point2D, Double>,
        val max: Double,
        val min: Double,
        val rastringLevels: Map<Int, Set<Point2D>>
    )
}