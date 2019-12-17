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
        List<String> input = Helper.input(16);
        String signal = input.get(0).repeat(10000);
        int offset = Integer.parseInt(signal.substring(0, 7));
//        System.out.println((double) offset/signal.length()); // ratio = 0.92

        String ans = calculateSignal2(signal, offset, 0);
        System.out.println(ans.substring(offset, offset + 8));
    }

    public static String calculateSignal2(String input, int offset, int depth) {
        if (depth == 100) {
            return input;
        }

        StringBuilder newSignal = new StringBuilder(input.substring(0, offset));
        StringBuilder digitsFromEnd = new StringBuilder();
        int sum = 0;

        for (int i = input.length() - 1; i >= offset; i--) { // skip first offset numbers since they're equal to 0
            // offset/length ratio is 0.9 which means all other numbers have a pattern of 1, have to find their sum

            int number = Integer.parseInt(String.valueOf(input.charAt(i)));
            sum += number;

            String sumString = String.valueOf(sum);
            String digit = String.valueOf(sumString.charAt(sumString.length() - 1));
            digitsFromEnd.append(digit);
        }

        newSignal.append(digitsFromEnd.reverse());
        return calculateSignal2(newSignal.toString(), offset, depth + 1);
    }
}
