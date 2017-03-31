import java.io.File
import java.util.concurrent.LinkedBlockingQueue


open class Location(val name: String, val x: Int, val y: Int)

open class Trip(val start: Location, val end: Location, val time: Int)

fun main(args: Array<String>) {
    val lines = LinkedBlockingQueue(File("input/level3-4.txt").readLines())

    val numberOfLocations = lines.poll().toInt()
    val locations = (1..numberOfLocations).map {
        val parts = lines.poll().split(" ")
        Location(parts[0], parts[1].toInt(), parts[2].toInt())
    }

    val numberJourneys = lines.poll().toInt()
    val journeys = (1..numberJourneys).map {
        val parts = lines.poll().split(" ")
        Trip(locations.single { it.name == parts[0] }, locations.single { it.name == parts[1] }, parts[2].toInt())
    }

    val hyperloopTrip = lines.poll().split(" ")

    // END INPUT

    fun calculateTripLocation(start: Location, end: Location): Double {
        val hyperloopLocations = locations.filter { hyperloopTrip.contains(it.name) }
        val startHyperloop = hyperloopLocations.sortedBy { pythagoras(start, it) }.first()
        val destinationHyperloop = hyperloopLocations.single { it.name != startHyperloop.name }

        val hyperloopDistance = pythagoras(startHyperloop, destinationHyperloop)
        val hyperloopTime = hyperloopDistance / 250 + 200

        val driveDistance = pythagoras(start, startHyperloop)
        val driveTime = driveDistance / 15

        val walkDistance = pythagoras(destinationHyperloop, end)
        val walkTime = walkDistance / 15

        return listOf(driveTime, hyperloopTime, walkTime).sum()
    }

    val shorterHyperloopTimes = journeys.filter { calculateTripLocation(it.start, it.end) < it.time }

    println(shorterHyperloopTimes.size)
}

fun pythagoras(start: Location, end: Location): Double =
        Math.sqrt(Math.pow(Math.abs(start.x.toDouble() - end.x), 2.0) + Math.pow(Math.abs(start.y.toDouble() - end.y), 2.0))
