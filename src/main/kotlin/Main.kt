import kravis.SessionPrefs
import kravis.geomCol
import kravis.plot
import kravis.render.Docker
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

const val MAX_STEPS = 20
const val SIMULATIONS = 1000

const val THREADS = 20

fun main() {
    val executor: ExecutorService = Executors.newFixedThreadPool(THREADS)

    val averageByStep = (1..MAX_STEPS).map { step ->
        val simulations = List(SIMULATIONS) { Simulation(step) }

        val average = executor
            .invokeAll(simulations)
            .stream()
            .mapToInt { future -> future.get() }
            .summaryStatistics().average

        println("$step  $average")

        Pair(step, average)
    }

    executor.shutdown()

    SessionPrefs.RENDER_BACKEND = Docker()

    averageByStep.plot( x = { first }, y = { second })
        .geomCol()
        .xLabel("Steps")
        .yLabel("Average Jumps")
        .title("Average Jumps by Number of Steps")
        .save(File("./plot.png"))
}