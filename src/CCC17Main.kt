import java.io.File

data class Location(val name: String, val x: Int, val y: Int)

class Trip(val locations: List<Location>)
{
    fun getTravelTime() {




    }
}

fun main(args: Array<String>) {
    val lines = File("input/level1-4.txt").readLines()

    val numberOfLocations = lines[0].toInt()
    val locations = lines.subList(1, numberOfLocations + 1).map {
        val parts = it.split(" ")
        Location(parts[0], parts[1].toInt(), parts[2].toInt())
    }
    val line = lines.last().split(" ")


    val start = locations.single { it.name == line.first() }
    val end = locations.single { it.name == line.last() }

    val distance = pythagoras(start, end)
    val time = Math.round(distance / 250 + 200).toInt()

    print(time)
}


fun pythagoras(start: Location, end: Location): Double =
        Math.sqrt(Math.pow(Math.abs(start.x.toDouble() - end.x), 2.0) + Math.pow(Math.abs(start.y.toDouble() - end.y), 2.0))