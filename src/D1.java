import java.util.List;

public class D1 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input("day1.txt");

        int sum = input.stream()
            .mapToInt(Integer::parseInt)
            .map(weight -> (weight/3) - 2)
            .sum();

        System.out.println(sum);

    }

    public static void q2() {
        List<String> input = Helper.input("day1.txt");

        int sum = input.stream()
            .mapToInt(Integer::parseInt)
            .map(D1::q2Helper)
            .sum();

        System.out.println(sum);
    }

    public static int q2Helper(int in) {
        int acc = 0;

        while (in > 0) {
            in = in/3 - 2;
            if (in > 0) {
                acc += in;
            }
        }
        return acc;
    }
}
