import helper.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class D8 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(8);

        List<Integer> digits = input.get(0).chars()
            .mapToObj(c -> (char) c)
            .map(String::valueOf)
            .map(Integer::parseInt)
            .collect(Collectors.toList());


        List<List<Integer>> layers = new ArrayList<>();
        for (int i = 0; i < digits.size(); i+= 25 * 6) {
            layers.add(new ArrayList<>(digits.subList(i, i + 25 * 6)));
        }

        int min = -1;
        int idx = -1;
        for (int i = 0; i < layers.size(); i++) {
            List<Integer> layer = layers.get(i);
            int count = (int) layer.stream().filter(digit -> digit == 0).count();
            if (min == -1 || count < min) {
                min = count;
                idx = i;
            }
        }

        List<Integer> minLayer = layers.get(idx);
        long oneCount = minLayer.stream().filter(digit -> digit == 1).count();
        long twoCount = minLayer.stream().filter(digit -> digit == 2).count();
        System.out.println(oneCount * twoCount);
    }

    public static void q2() {
        List<String> input = Helper.input(8);

        List<Integer> digits = input.get(0).chars()
            .mapToObj(c -> (char) c)
            .map(String::valueOf)
            .map(Integer::parseInt)
            .collect(Collectors.toList());


        List<Integer> picture = new ArrayList<>();
        for (int i = 0; i < 25 * 6; i++) {
            picture.add(2); // fill image with transparent pixels
        }

        List<List<Integer>> layers = new ArrayList<>();
        for (int i = 0; i < digits.size(); i+= 25 * 6) {
            layers.add(new ArrayList<>(digits.subList(i, i + 25 * 6)));
        }

        for (List<Integer> layer : layers) {
            for (int i = 0; i < layer.size(); i++) {
                if (picture.get(i) == 2 && layer.get(i) != 2) {
                    picture.set(i, layer.get(i));
                }
            }
        }

        for (int i = 0; i < picture.size(); i += 25) {
            List<Integer> row = picture.subList(i, i + 25);
            List<String> readableRow = row.stream().map(digit -> digit == 0 ? "." : "#")
                .collect(Collectors.toList());
            System.out.println(readableRow);
        }

    }
}
