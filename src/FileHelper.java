import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class FileHelper {

    private static final String BASE_PATH = "/Users/lukas/Development/CCC17Vienna/input/";

    static List<String> readAllLines(String fileName) {
        try {
            return Files.readAllLines(Paths.get(BASE_PATH + fileName),
                    Charset.defaultCharset());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
