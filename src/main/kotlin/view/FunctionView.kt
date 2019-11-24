package view

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import tornadofx.*
import view.controller.GeneticController
import view.genetic.Chromosome


class FunctionView : View() {

    private val chromosomeData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()
    private val populationSize = SimpleIntegerProperty()
    private val generationCount = SimpleIntegerProperty()
    private val probabilityOfCrossing = SimpleDoubleProperty()
    private val probabilityOfMutation = SimpleDoubleProperty()
    private val middleFitnessValue = SimpleDoubleProperty()
    private val bestFitnessValue = SimpleDoubleProperty()
    private val solution = SimpleDoubleProperty()

    override fun onBeforeShow() {
        this.primaryStage.minWidth = 1500.0
        this.primaryStage.minHeight = 1000.0
    }

    override val root = form {
        fieldset {
            field("Population size") {
                textfield(populationSize).text = "5"
            }
            field("Generation count") {
                textfield(generationCount).text = "5"
            }
            field("Probability of crossing") {
                textfield(probabilityOfCrossing).text = "0.9"
            }
            field("Probability of mutation") {
                textfield(probabilityOfMutation).text = "0.1"
            }
            scatterchart("Genetic Algorithm", NumberAxis(-6.0, 6.0, 1.0), NumberAxis(-6.0, 6.0, 1.0)) {
                prefHeight = 700.0
                usePrefHeight
                series("Genetic", chromosomeData)
                for (rastringLevel in 10..60 step 10) {
                    series("RL$rastringLevel") {
                        val points = Plot.rastringLevels.getValue(rastringLevel)
                        points.forEach { point ->
                            data(point.x, point.y)
                        }
                    }
                }
            }
            button("Start") {
                action {
                    runAsync {
                        var generation = emptyList<Chromosome>()
                        val generationCount = generationCount.value
                        for (i in 1..generationCount) {
                            val uiData = getNextGeneration(generation)
                            var currentSolution: Double = Double.MIN_VALUE
                            generation = uiData.generation
                            runLater {
                                bestFitnessValue.value = uiData.bestFitnessValue
                                middleFitnessValue.value = uiData.middleFitnessValue
                                chromosomeData.clear()
                                uiData.generationPoints.forEach { (x, y) ->
                                    chromosomeData.add(XYChart.Data(x, y))
                                    val z = Plot.getZValue(x, y)
                                    if (z > currentSolution) {
                                        currentSolution = z
                                    }
                                }
                                solution.value = currentSolution
                            }
                            Thread.sleep(1500)
                        }
                    }
                }
            }
            hbox {
                label("Best Fitness Value = ")
                label(bestFitnessValue)
            }
            hbox {
                label("Middle Fitness Value = ")
                label(middleFitnessValue)
            }
            hbox {
                label("Solution\\original = ")
                label(solution)
                label(" \\ ${Plot.min}")
            }
        }
    }

    private fun getNextGeneration(generation: List<Chromosome>): UiData {
        val generationPoints = mutableListOf<Pair<Double, Double>>()
        val population = GeneticController.calculate(
            populationSize.value,
            probabilityOfCrossing.value,
            probabilityOfMutation.value,
            generation
        )
        for (chromosome in population.population) {
            generationPoints.add(Pair(chromosome.x, chromosome.y))
        }
        return UiData(
            generationPoints,
            population.bestFitnessValue,
            population.middleFitnessValue,
            population.population
        )
    }
}

data class UiData(
    val generationPoints: List<Pair<Double, Double>>,
    val bestFitnessValue: Double,
    val middleFitnessValue: Double,
    val generation: List<Chromosome>
)