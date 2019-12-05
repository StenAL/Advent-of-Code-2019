import java.util.List;

public class D4 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(4);
        String[] borders = input.get(0).split("-");
        int min = Integer.parseInt(borders[0]);
        int max = Integer.parseInt(borders[1]);

        int count = 0;
        for (int i = min; i <= max; i++) {
            String s = String.valueOf(i);
            if (containsRepeating(s) && growing(s)) {
                count++;
            }

        }
        System.out.println(count);
    }

    public static void q2() {
        List<String> input = Helper.input(4);
        String[] borders = input.get(0).split("-");
        int min = Integer.parseInt(borders[0]);
        int max = Integer.parseInt(borders[1]);

        int count = 0;
        for (int i = min; i <= max; i++) {
            String s = String.valueOf(i);
            if (containsRepeating(s) && growing(s) && smallGroupRepeating(s)) {
                count++;
            }

        }
        System.out.println(count);
    }

    public static boolean containsRepeating(String s) {
        for (int i = 1; i < s.toCharArray().length; i++) {
            if (s.charAt(i) == s.charAt(i - 1)) return true;
        }
        return false;
    }

    public static boolean growing(String s) {
        for (int i = 1; i < s.toCharArray().length; i++) {
            if (Integer.parseInt(s.substring(i - 1, i)) > Integer.parseInt(s.substring(i, i + 1)))  {
                return false;
            }
        }
        return true;
    }

    public static boolean smallGroupRepeating(String s) {
        int count = 1;
        for (int i = 1; i < s.toCharArray().length; i++) {
            if (s.charAt(i) == s.charAt(i - 1)) {
                count++;
            } else {
                if (count == 2) {
                    return true;
                }
                count = 1;
            }
        }

        return count == 2;
    }
}
