import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D6 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(6);

        Map<String, String> orbits = new HashMap<>();
        for (String in : input) {
            String[] inArray = in.split("\\)");
            String orbiting = inArray[1];
            String around = inArray[0];
            orbits.put(orbiting, around);
        }

        int checksum = 0;
        for (Map.Entry<String, String> entry : orbits.entrySet()) {
            String around = entry.getValue();
            int orbitCount = 1;
            while (!around.equals("COM")) {
                around = orbits.get(around);
                orbitCount++;
            }
            checksum += orbitCount;
        }
        System.out.println(checksum);
    }

    public static void q2() {
        List<String> input = Helper.input(6);

        Map<String, String> orbits = new HashMap<>();
        for (String in : input) {
            String[] inArray = in.split("\\)");
            String orbiting = inArray[1];
            String around = inArray[0];
            orbits.put(orbiting, around);
        }


        List<String> youToCom = new ArrayList<>();
        String around = orbits.get("YOU");
        youToCom.add(around);
        while (!around.equals("COM")) {
            around = orbits.get(around);
            youToCom.add(around);
        }

        around = orbits.get("SAN");
        int orbitsFromSanta = 0;
        while (true) {
            if (youToCom.contains(around)) {
                int orbitsFromYou = youToCom.indexOf(around);
                System.out.println(orbitsFromSanta + orbitsFromYou);
                break;
            }
            around = orbits.get(around);
            orbitsFromSanta++;
        }

    }
}
