import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static List<String> input(String file) {
        try {
            return Files.readAllLines(Path.of(file));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
