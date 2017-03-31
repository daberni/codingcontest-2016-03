import java.io.File
import java.util.*
import java.util.concurrent.LinkedBlockingQueue


data class Location(val name: String, val x: Int, val y: Int)

data class Trip(val start: Location, val end: Location, val time: Int)

fun main(args: Array<String>) {
    val lines = LinkedBlockingQueue(File("input/level6-eg.txt").readLines())

    val numberOfLocations = lines.poll().toInt()
    val locations = (1..numberOfLocations).map {
        val parts = lines.poll().split(" ")
        Location(parts[0], parts[1].toInt(), parts[2].toInt())
    }


    /*
    val journey = lines.poll().split(" ")
    val journeyStart = locations.single { journey[0] == it.name }
    val journeyEnd = locations.single { journey[1] == it.name }
    */

    /*
    val hyperloopStopInput = lines.poll().split(" ")
    val stopCount = hyperloopStopInput[0].toInt()
    val hyperloopStops = hyperloopStopInput.subList(1, stopCount + 1).map { stop -> locations.single { stop == it.name } }.withIndex().toList()
    */

    val numberJourneys = lines.poll().toInt()
    val journeys = (1..numberJourneys).map {
        val parts = lines.poll().split(" ")
        Trip(locations.single { it.name == parts[0] }, locations.single { it.name == parts[1] }, parts[2].toInt())
    }

    val minBenefitialJourness = lines.poll().toInt()
    val maxHyperloopDistance = lines.poll().toInt()

    // END INPUT

    fun calculateTripLocation(start: Location, hyperloopStops: List<Location>, end: Location): Double {



        val driveDistance = pythagoras(start, hyperloopStops.first())
        val driveTime = driveDistance / 15

        val currentDriveTime = journeys.find { it.start == start && it.end == hyperloopStops.first() }?.time?.toDouble() ?: driveTime



        val hyperloopTime = (0 until hyperloopStops.size - 1).sumByDouble { i ->
            val hyperloopStart = hyperloopStops[i]
            val hyperloopEnd = hyperloopStops[i + 1]

            val hyperloopDistance = pythagoras(hyperloopStart, hyperloopEnd)
            val time = hyperloopDistance / 250 + 200
            return@sumByDouble time
        }

        val walkDistance = pythagoras(hyperloopStops.last(), end)
        val walkTime = walkDistance / 15

        val currentWalkTime = journeys.find { it.start == hyperloopStops.last() && it.end == end }?.time?.toDouble() ?: walkTime

        return listOf(minOf(driveTime, currentDriveTime), hyperloopTime, minOf(walkTime, currentWalkTime)).sum()
    }

    /*
    val startHyperloopStop = hyperloopStops.sortedBy { pythagoras(journeyStart, it.value) }.first()
    val endHyperloopStop = hyperloopStops.sortedBy { pythagoras(journeyEnd, it.value) }.first()

    val hyperloopTrack = when {
        startHyperloopStop.index < endHyperloopStop.index -> hyperloopStops.subList(startHyperloopStop.index, endHyperloopStop.index + 1)
        else -> hyperloopStops.subList(endHyperloopStop.index, startHyperloopStop.index + 1).reversed()
    }

    val totalTime = calculateTripLocation(journeyStart, hyperloopTrack.map { it.value }, journeyEnd)
    println(totalTime)
    */


    println(minBenefitialJourness)

    val allPerms = generatePerm(locations.toMutableList())

    val permuted = (2..5).flatMap { limit ->
        allPerms.map { it.subList(0, limit) }.distinct()
    }

    permuted.forEach { hyperloopTrack ->
        if (totalDistance(hyperloopTrack) <= maxHyperloopDistance) {
            val benefitCounts = journeys.count { journey ->
                calculateTripLocation(journey.start, hyperloopTrack, journey.end) < journey.time
            }

            println("$benefitCounts: $hyperloopTrack")
            if (benefitCounts > minBenefitialJourness) {
                return@forEach
            }
        }
    }
}

fun totalDistance(locations: List<Location>) = (0 until locations.size - 1).sumByDouble { i ->
    val hyperloopStart = locations[i]
    val hyperloopEnd = locations[i + 1]
    return@sumByDouble pythagoras(hyperloopStart, hyperloopEnd)
}


fun <E> generatePerm(original: MutableList<E>): List<List<E>> {
    if (original.size == 0) {
        val result = ArrayList<List<E>>()
        result.add(ArrayList<E>())
        return result
    }
    val firstElement = original.removeAt(0)
    val returnValue = ArrayList<List<E>>()
    val permutations = generatePerm(original)
    for (smallerPermutated in permutations) {
        for (index in 0..smallerPermutated.size) {
            val temp = ArrayList<E>(smallerPermutated)
            temp.add(index, firstElement)
            returnValue.add(temp)
        }
    }
    return returnValue
}
/*
fun possibleTracks(currentTrack: List<Location>, maxLength: Int): List<List<Location>> {
    if (totalDistance(currentTrack) > maxLength)
        return currentTrack
    else {
        return currentTrack + currentTrack.flatMap {
            currentTrack + it
        }
    }
}
*/

/*
fun generatePerm(original: MutableList<E>): List<List<E>> {
    if (original.size == 0) {
        val result = ArrayList<List<E>>()
        result.add(ArrayList<E>())
        return result
    }
    val firstElement = original.removeAt(0)
    val returnValue = ArrayList<List<E>>()
    val permutations = generatePerm(original)
    for (smallerPermutated in permutations) {
        for (index in 0..smallerPermutated.size) {
            val temp = ArrayList<E>(smallerPermutated)
            temp.add(index, firstElement)
            returnValue.add(temp)
        }
    }
    return returnValue
}
*/

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
