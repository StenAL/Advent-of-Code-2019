package helper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static List<String> input(String file) {
        try {
            return Files.readAllLines(Path.of(file));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<String> input(int day) {
        try {
            return Files.readAllLines(Path.of("day" + day + ".txt"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<String> test() {
        try {
            return Files.readAllLines(Path.of("test.txt"));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static List<Long> intCode(int day) {
        return Arrays.stream(input(day).get(0).split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }
}
