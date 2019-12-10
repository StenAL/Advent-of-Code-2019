import java.util.*;

public class D10 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(10);
        List<Coordinate> coordinates = new ArrayList<>();

        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    coordinates.add(new Coordinate(j, i));
                }
            }
        }

        List<Integer> seen = new ArrayList<>();
        for (Coordinate main : coordinates) {
            Set<Double> checked1 = new HashSet<>(); // checked linear formulas
            Set<Double> checked2 = new HashSet<>(); // one for each sector of graph
            Set<Double> checked3 = new HashSet<>();
            Set<Double> checked4 = new HashSet<>();
            for (Coordinate coordinate : coordinates) {
                if (coordinate != main) {
                    int dx = main.x - coordinate.x;
                    int dy = main.y - coordinate.y;
                    double k = dy/(double) dx;

                    if (dx < 0 && dy < 0) {
                        if (checked1.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) { // floating point precision
//                            System.out.println(main + " can see " + coordinate);
                            checked1.add(k);
                        }
                    } else if (dx >= 0 && dy < 0) {
                        if (checked2.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) {
                            checked2.add(k);
                        }
                    } else if (dx < 0) {
                        if (checked3.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) {
                            checked3.add(k);
                        }
                    } else {
                        if (checked4.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) {
                            checked4.add(k);
                        }
                    }
                }
            }
            seen.add(checked1.size() + checked2.size() + checked3.size() + checked4.size());

            // get best coordinate for station
//            if (checked1.size() + checked2.size() + checked3.size() + checked4.size() == 286) {
//                System.out.println(main);
//            }
        }
        System.out.println(seen.stream().max(Integer::compareTo).get()); // ans @ coordinate x = 22, y = 25
    }

    public static void q2() {
        List<String> input = Helper.input(10);
        List<Coordinate> coordinates = new ArrayList<>();
        Coordinate station = new Coordinate(22, 25);


        for (int i = 0; i < input.size(); i++) {
            String line = input.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == '#') {
                    coordinates.add(new Coordinate(j, i));
                }
            }
        }
        coordinates.remove(station);
        coordinates.sort(Comparator.comparingInt(a -> (Math.abs(a.x - station.x) + Math.abs(a.y - station.y)))); // sort by distance from station so asteroids destroyed in right order
        int destroyed = 0;
        while (destroyed < 200) {
            List<Coordinate> visible = getVisible(station, coordinates);
            for (Coordinate coord : visible) {
                coordinates.remove(coord);
                destroyed++;
                if (destroyed == 200) {
                    System.out.println(coord.x * 100 + coord.y);
                }
            }
        }
    }

    public static List<Coordinate> getVisible(Coordinate main, List<Coordinate> others) {
        Set<Double> checked1 = new HashSet<>(); // checked linear formulas
        Set<Double> checked2 = new HashSet<>(); // one for each sector of graph
        Set<Double> checked3 = new HashSet<>();
        Set<Double> checked4 = new HashSet<>();

        List<Coordinate> visible = new ArrayList<>();
        List<Coordinate> visible1 = new ArrayList<>();
        List<Coordinate> visible2 = new ArrayList<>();
        List<Coordinate> visible3 = new ArrayList<>();
        List<Coordinate> visible4 = new ArrayList<>();

        for (Coordinate coordinate : others) {
            if (coordinate != main) {
                int dx = main.x - coordinate.x;
                int dy = main.y - coordinate.y;
                double k = dy/(double) dx;

                if (dx > 0 && dy > 0) {
                    if (checked1.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) {
                        if (checked1.add(k)) {
                            visible1.add(coordinate);
                        }
                    }
                } else if (dx <= 0 && dy >= 0) {
                    if (checked2.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) {
                        if (checked2.add(k)) {
                            visible2.add(coordinate);
                        }
                    }
                } else if (dx < 0) {
                    if (checked3.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) {
                        if (checked3.add(k)) {
                            visible3.add(coordinate);
                        }
                    }
                } else {
                    if (checked4.stream().noneMatch(d -> Math.abs(d - k) <= 0.000001)) {
                        if (checked4.add(k)) {
                            visible4.add(coordinate);
                        }
                    }
                }
            }
        }


        visible2.sort(Comparator.comparingDouble(a -> {
            int dx = Math.abs(main.x - a.x);
            int dy = Math.abs(main.y - a.y);
            double k = dy/(double) dx;
            return k * -1;
        }));

        visible3.sort(Comparator.comparingDouble(a -> {
            int dx = Math.abs(main.x - a.x);
            int dy = Math.abs(main.y - a.y);
            double k = dy/(double) dx;
            return k;
        }));

        visible4.sort(Comparator.comparingDouble(a -> {
            int dx = Math.abs(main.x - a.x);
            int dy = Math.abs(main.y - a.y);
            double k = dx/(double) dy;
            return k;
        }));

        visible1.sort(Comparator.comparingDouble(a -> {
            int dx = Math.abs(main.x - a.x);
            int dy = Math.abs(main.y - a.y);
            double k = dy/(double) dx;
            return k;
        }));


        visible.addAll(visible2);
        visible.addAll(visible3);
        visible.addAll(visible4);
        visible.addAll(visible1);
//        System.out.println("1: " + visible.get(0));
//        System.out.println("2: " + visible.get(1));
//        System.out.println("3: " + visible.get(2));
//        System.out.println("10: " + visible.get(9));
//        System.out.println("20: " + visible.get(19));
//        System.out.println("50: " + visible.get(49));
//        System.out.println("100: " + visible.get(99));
//        System.out.println("199: " + visible.get(198));
//        System.out.println("200: " + visible.get(199));
//        System.out.println("201: " + visible.get(200));
        return visible;
    }


    public static class Coordinate {
        public int x;
        public int y;

        @Override
        public String toString() {
            return "(" +
                "x=" + x +
                ", y=" + y +
                ')';
        }

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coordinate that = (Coordinate) o;

            if (x != that.x) return false;
            return y == that.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
