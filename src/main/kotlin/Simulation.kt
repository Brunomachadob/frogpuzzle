import java.util.concurrent.Callable

class Simulation(
    private val n: Int
) : Callable<Int> {
    override fun call(): Int {
        var step = n
        var count = 0

        while (step > 0) {
            val jump = (1..step).random()
            step -= jump
            count++
        }

        return count
    }
}