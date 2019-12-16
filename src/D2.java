import helper.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D2 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input("day2.txt");
        List<Integer> codes = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

//        codes = new ArrayList<>(List.of(1, 0, 0, 0, 99));
        codes.set(1, 12);
        codes.set(2, 2);

        for (int i = 0; i < codes.size(); i += 4) {
            int code = codes.get(i);

            boolean stop = false;
            switch (code) {
                case 1:
                    codes.set(codes.get(i + 3), codes.get(codes.get(i + 1)) + codes.get(codes.get(i + 2)));
                    break;
                case 2:
                    codes.set(codes.get(i + 3), codes.get(codes.get(i + 1)) * codes.get(codes.get(i + 2)));
                    break;
                case 99:
                    stop = true;
                    break;
            }

            if (stop) break;
        }
        System.out.println(codes.get(0));
    }

    public static void q2() {
        List<String> input = Helper.input("day2.txt");
        List<Integer> originalCodes = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

//        codes = new ArrayList<>(List.of(1, 0, 0, 0, 99));

        boolean found = false;
        int noun = 0;
        int verb = 0;
        int output = -1;

        while (!found) {
            List<Integer> codes = new ArrayList<>(originalCodes);
            codes.set(1, noun);
            codes.set(2, verb);

            for (int i = 0; i < codes.size(); i += 4) {
                int code = codes.get(i);

                boolean stop = false;
                switch (code) {
                    case 1:
                        codes.set(codes.get(i + 3), codes.get(codes.get(i + 1)) + codes.get(codes.get(i + 2)));
                        break;
                    case 2:
                        codes.set(codes.get(i + 3), codes.get(codes.get(i + 1)) * codes.get(codes.get(i + 2)));
                        break;
                    case 99:
                        stop = true;
                        break;
                }

                if (stop) break;
            }

            output = codes.get(0);
            if (output == 19690720) {
                found = true;
            } else if (verb < 99) {
                verb++;
            } else {
                noun++;
                verb = 0;
            }

        }
        System.out.println(output + " noun: " + noun + " verb: " + verb);
    }
}
