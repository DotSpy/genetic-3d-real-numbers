package by.vkiva.view.genetic

import org.junit.jupiter.api.Test
import view.Plot
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.ThreadLocalRandom

class TestRandom {

    @Test
    fun testAlpha() {
        for (i in 1..100) {
            println(
                BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(-0.25, 1.25))
                    .setScale(Plot.scale, RoundingMode.HALF_EVEN).toDouble()
            )
        }
    }
}