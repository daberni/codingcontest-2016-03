import java.io.File


open class Location(val name: String, val x: Int, val y: Int)

fun main(args: Array<String>) {
    val lines = File("input/level2-4.txt").readLines()

    val numberOfLocations = lines[0].toInt()
    val locations = lines.subList(1, numberOfLocations + 1).map {
        val parts = it.split(" ")
        Location(parts[0], parts[1].toInt(), parts[2].toInt())
    }

    val journeyTrip = lines[lines.size - 2].split(" ")

    val tripStart = locations.single { it.name == journeyTrip.first() }
    val tripEnd = locations.single { it.name == journeyTrip.last() }

    val hyperloopTrip = lines[lines.size - 1].split(" ")


    val hyperloopLocations = locations.filter { hyperloopTrip.contains(it.name) }
    val startHyperloop = hyperloopLocations.sortedBy { pythagoras(tripStart, it) }.first()
    val destinationHyperloop = hyperloopLocations.single { it.name != startHyperloop.name}

    val hyperloopDistance = pythagoras(startHyperloop, destinationHyperloop)
    val hyperloopTime = hyperloopDistance / 250 + 200

    val driveDistance = pythagoras(tripStart, startHyperloop)
    val driveTime = driveDistance / 15

    val walkDistance = pythagoras(destinationHyperloop, tripEnd)
    val walkTime = walkDistance / 15

    val totalTime = listOf(driveTime, hyperloopTime, walkTime).sum()

    println(totalTime)
    println(Math.round(totalTime))
}


fun pythagoras(start: Location, end: Location): Double =
        Math.sqrt(Math.pow(Math.abs(start.x.toDouble() - end.x), 2.0) + Math.pow(Math.abs(start.y.toDouble() - end.y), 2.0))