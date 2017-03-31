import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CCC17Main {

    public static void main(String[] args) {
        List<String> lines = FileHelper.readAllLines("level1-4.txt");
        int numberOfLocations = Integer.parseInt(lines.get(0));
        Map<String, Location> locations = new HashMap<>(numberOfLocations);
        int i;
        for (i = 1; i < 1 + numberOfLocations; i++) {
            String[] lineSplits = lines.get(i).split(" ");
            locations.put(lineSplits[0], new Location(lineSplits[0], Integer.parseInt(lineSplits[1]),
                    Integer.parseInt(lineSplits[2])));
        }

        Location location1 = locations.get(lines.get(i).split(" ")[0]);
        Location location2 = locations.get(lines.get(i).split(" ")[1]);
        double distance = getDistance(location1, location2);
        System.out.println(Math.round(distance / 250 + 200));
    }

    private static double getDistance(Location location1, Location location2) {
        return Math.sqrt(Math.pow(location1.x - location2.x, 2) + Math.pow(location1.y - location2.y, 2));
    }
}
