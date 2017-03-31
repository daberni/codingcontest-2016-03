import java.io.File
import java.util.concurrent.LinkedBlockingQueue


data class Location(val name: String, val x: Int, val y: Int)

data class Trip(val start: Location, val end: Location, val time: Int)

fun main(args: Array<String>) {
    val lines = LinkedBlockingQueue(File("input/level4-2.txt").readLines())

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

    val minBenefitialJourness = lines.poll().toInt()

    // END INPUT

    fun calculateTripLocation(start: Location, startHyperloop: Location, destinationHyperloop: Location, end: Location): Double {
        val driveDistance = pythagoras(start, startHyperloop)
        val driveTime = driveDistance / 15

        val hyperloopDistance = pythagoras(startHyperloop, destinationHyperloop)
        val hyperloopTime = hyperloopDistance / 250 + 200

        val walkDistance = pythagoras(destinationHyperloop, end)
        val walkTime = walkDistance / 15

        return listOf(driveTime, hyperloopTime, walkTime).sum()
    }

    val possibilities = locations.permute()
    possibilities.forEach { hyperloopLocations ->
        val shorterHyperloopTimes = journeys.filter { journey ->
            calculateTripLocation(journey.start, hyperloopLocations.first, hyperloopLocations.second, journey.end) < journey.time
        }
        // println(shorterHyperloopTimes.size)
        if (shorterHyperloopTimes.size >= (minBenefitialJourness - 5)) {
            println("${hyperloopLocations.first.name} ${hyperloopLocations.second.name}")
            return
        }
    }
}

fun pythagoras(start: Location, end: Location): Double =
        Math.sqrt(Math.pow(Math.abs(start.x.toDouble() - end.x), 2.0) + Math.pow(Math.abs(start.y.toDouble() - end.y), 2.0))

fun <T> Collection<T>.permute(): List<Pair<T, T>> {
    return this.combine(this).filter { it.first != it.second }
}

fun <T1, T2> Collection<T1>.combine(other: Iterable<T2>): List<Pair<T1, T2>> {
    return combine(other, { thisItem: T1, otherItem: T2 -> Pair(thisItem, otherItem) })
}

fun <T1, T2, R> Collection<T1>.combine(other: Iterable<T2>, transformer: (thisItem: T1, otherItem: T2) -> R): List<R> {
    return this.flatMap { thisItem -> other.map { otherItem -> transformer(thisItem, otherItem) } }
}
