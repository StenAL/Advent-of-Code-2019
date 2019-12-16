import helper.Helper;

import java.util.ArrayList;
import java.util.List;

public class D16 {

    public static void main(String[] args) {
//        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(16);
        String signal = input.get(0);
        String ans = calculateSignal(signal, 0);
        System.out.println(ans);
    }

    public static String calculateSignal(String input, int depth) {
        if (depth == 100) {
            return input.substring(0, 8);
        }

        List<Integer> basePattern = List.of(0, 1, 0, -1);
        String newSignal = "";

        for (int i = 0; i < input.length(); i++) {
            List<Integer> pattern = new ArrayList<>();
            for (Integer baseDigit : basePattern) {
                for (int j = 0; j < i + 1; j++) {
                    pattern.add(baseDigit);
                }
            }

            char[] charArray = input.toCharArray();
            int sum = 0;
            for (int j = 0; j < charArray.length; j++) {
                int number = Integer.parseInt(String.valueOf(charArray[j]));
                number *= pattern.get((j + 1) % pattern.size());
                sum += number;
            }

            String sumString = String.valueOf(sum);
            String digit = String.valueOf(sumString.charAt(sumString.length() - 1));
            newSignal += digit;
        }

        return calculateSignal(newSignal, depth + 1);
    }

    public static void q2() {
        List<String> input = Helper.test();
        String signal = input.get(0).repeat(1000);

        String ans = calculateSignal2(signal, 0);
        System.out.println(ans);
    }

    public static String calculateSignal2(String input, int depth) {
        System.out.println(depth);
        if (depth == 100) {
            return input;
        }

        List<Integer> basePattern = List.of(0, 1, 0, -1);
        StringBuilder newSignal = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            List<Integer> pattern = new ArrayList<>();
            for (Integer baseDigit : basePattern) {
                for (int j = 0; j < i + 1; j++) {
                    pattern.add(baseDigit);
                }
            }

            char[] charArray = input.toCharArray();
            int sum = 0;
            for (int j = 0; j < charArray.length; j++) {
                int number = Integer.parseInt(String.valueOf(charArray[j]));
                number *= pattern.get((j + 1) % pattern.size());
                sum += number;
            }

            String sumString = String.valueOf(sum);
            String digit = String.valueOf(sumString.charAt(sumString.length() - 1));
            newSignal.append(digit);
        }

        return calculateSignal2(newSignal.toString(), depth + 1);
    }
}
