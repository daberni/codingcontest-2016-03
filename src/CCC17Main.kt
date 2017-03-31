import java.io.File
import java.util.concurrent.LinkedBlockingQueue


data class Location(val name: String, val x: Int, val y: Int)

data class Trip(val start: Location, val end: Location, val time: Int)

fun main(args: Array<String>) {
    val lines = LinkedBlockingQueue(File("input/level5-eg.txt").readLines())

    val numberOfLocations = lines.poll().toInt()
    val locations = (1..numberOfLocations).map {
        val parts = lines.poll().split(" ")
        Location(parts[0], parts[1].toInt(), parts[2].toInt())
    }


    val journey = lines.poll().split(" ")
    val journeyStart = locations.single { journey[0] == it.name }
    val journeyEnd = locations.single { journey[1] == it.name }



    val hyperloopStopInput = lines.poll().split(" ")
    val stopCount = hyperloopStopInput[0].toInt()
    val hyperloopStops = hyperloopStopInput.subList(1, stopCount + 1).map { stop -> locations.single { stop == it.name } }.withIndex().toList()


    /*
    val numberJourneys = lines.poll().toInt()
    val journeys = (1..numberJourneys).map {
        val parts = lines.poll().split(" ")
        Trip(locations.single { it.name == parts[0] }, locations.single { it.name == parts[1] }, parts[2].toInt())
    }
    */


    /*
    val minBenefitialJourness = lines.poll().toInt()
    val maxHyperloopDistance = lines.poll().toInt()
    */

    // END INPUT

    fun calculateTripLocation(start: Location, hyperloopStops: List<Location>, end: Location): Double {

        val driveDistance = pythagoras(start, hyperloopStops.first())
        val driveTime = driveDistance / 15

        val hyperloopTime = (0 until hyperloopStops.size - 1).sumByDouble { i ->
            val hyperloopStart = hyperloopStops[i]
            val hyperloopEnd = hyperloopStops[i + 1]

            val hyperloopDistance = pythagoras(hyperloopStart, hyperloopEnd)
            val time = hyperloopDistance / 250 + 200
            return@sumByDouble time
        }

        val walkDistance = pythagoras(hyperloopStops.last(), end)
        val walkTime = walkDistance / 15

        return listOf(driveTime, hyperloopTime, walkTime).sum()
    }



    val startHyperloopStop = hyperloopStops.sortedBy { pythagoras(journeyStart, it.value) }.first()
    val endHyperloopStop = hyperloopStops.sortedBy { pythagoras(journeyEnd, it.value) }.first()

    val hyperloopTrack = when {
        startHyperloopStop.index < endHyperloopStop.index -> hyperloopStops.subList(startHyperloopStop.index, endHyperloopStop.index + 1)
        else -> hyperloopStops.subList(endHyperloopStop.index, startHyperloopStop.index + 1).reversed()
    }

    val totalTime = calculateTripLocation(journeyStart, hyperloopTrack.map { it.value }, journeyEnd)
    println(totalTime)


    /*
    println(minBenefitialJourness)


    val possibilities = locations.permute()

    val benefits = possibilities.map { hyperloopLocations ->
        val shorterHyperloopTimes = journeys.count { journey ->
            calculateTripLocation(journey.start, hyperloopLocations.first, hyperloopLocations.second, journey.end) < journey.time
        }
        Pair(hyperloopLocations, shorterHyperloopTimes)
    }

    val mostBenefits = benefits.maxBy { it.second }!!

    println(mostBenefits.first)
    println(mostBenefits.second)

    possibilities.forEach { hyperloopLocations ->
        val journeysBenefitingFromHyperloop = journeys.filter { journey ->
            calculateTripLocation(journey.start, hyperloopLocations.first, hyperloopLocations.second, journey.end) < journey.time
        }
        // println(journeysBenefitingFromHyperloop.size)
        if (journeysBenefitingFromHyperloop.size >= minBenefitialJourness) {
            // println("${hyperloopLocations.first.name} ${hyperloopLocations.second.name}")
            // return
        }
    }
    */
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
