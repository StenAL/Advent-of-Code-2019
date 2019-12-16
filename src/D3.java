import helper.Helper;

import java.util.*;
import java.util.stream.Collectors;

public class D3 {
    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(3);
        List<List<Move>> wires = input.stream()
            .map(D3::processInput)
            .collect(Collectors.toList());

        Set<Coordinate> coordinates = new HashSet<>();
        Set<Coordinate> crossings = new HashSet<>();

        List<Move> wire = wires.get(0);
        int x = 0;
        int y = 0;

        for (Move move : wire) {
            for (int i = 0; i < move.distance; i++) {
                switch (move.dir) {
                    case "R":
                        x++;
                        break;
                    case "L":
                        x--;
                        break;
                    case "U":
                        y++;
                        break;
                    case "D":
                        y--;
                        break;
                }
                if (x != 0 || y != 0) {
//                    System.out.println("Square at " + x + ", " + y);
                    coordinates.add(new Coordinate(x, y));
                }
            }
        }

        x = 0;
        y = 0;
        wire = wires.get(1);
        for (Move move : wire) {
            for (int i = 0; i < move.distance; i++) {
                switch (move.dir) {
                    case "R":
                        x++;
                        break;
                    case "L":
                        x--;
                        break;
                    case "U":
                        y++;
                        break;
                    case "D":
                        y--;
                        break;
                }

                Coordinate coordinate = new Coordinate(x, y);
                if (coordinates.contains(coordinate)) {
                    System.out.println("Crossing at " + x + ", " + y);
                    crossings.add(coordinate);
                }
            }
        }


        int closest = -1;
        for (Coordinate crossing : crossings) {
            int d = Math.abs(crossing.x) + Math.abs(crossing.y);
            if (closest == -1 || d < closest) {
                closest = d;
            }
        }
        System.out.println(closest);


    }

    public static void q2() {
        List<String> input = Helper.input(3);
        List<List<Move>> wires = input.stream()
            .map(D3::processInput)
            .collect(Collectors.toList());

        Map<Coordinate, Integer> coordinates = new HashMap<>();
        List<Integer> crossings = new ArrayList<>();

        List<Move> wire = wires.get(0);
        int x = 0;
        int y = 0;
        int count = 0;

        for (Move move : wire) {
            for (int i = 0; i < move.distance; i++) {
                count++;
                switch (move.dir) {
                    case "R":
                        x++;
                        break;
                    case "L":
                        x--;
                        break;
                    case "U":
                        y++;
                        break;
                    case "D":
                        y--;
                        break;
                }
                if (x != 0 || y != 0) {
                    Coordinate coordinate = new Coordinate(x, y);
                    if (!coordinates.containsKey(coordinate)) {
                        coordinates.put(coordinate, count);
                    }
                }
            }
        }

        x = 0;
        y = 0;
        count = 0;
        wire = wires.get(1);
        for (Move move : wire) {
            for (int i = 0; i < move.distance; i++) {
                count++;
                switch (move.dir) {
                    case "R":
                        x++;
                        break;
                    case "L":
                        x--;
                        break;
                    case "U":
                        y++;
                        break;
                    case "D":
                        y--;
                        break;
                }

                Coordinate coordinate = new Coordinate(x, y);
                if (coordinates.containsKey(coordinate)) {
                    System.out.println("Crossing at " + x + ", " + y);
                    crossings.add(coordinates.get(coordinate) + count);
                }
            }
        }


        int ans = crossings.stream().mapToInt(a -> a).min().getAsInt();
        System.out.println(ans);


    }

    public static List<Move> processInput(String input) {
        return Arrays.stream(input.split(","))
            .map(Move::new)
            .collect(Collectors.toList());
    }

    private static class Move {
        public String dir;
        public int distance;

        public Move(String move) {
            this.dir = String.valueOf(move.charAt(0));
            this.distance = Integer.parseInt(move.substring(1));


        }

        @Override
        public String toString() {
            return "Move{" +
                "dir='" + dir + '\'' +
                ", distance=" + distance +
                '}';
        }
    }

    private static class Coordinate {
        public int x;
        public int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coordinate that = (Coordinate) o;
            return x == that.x &&
                y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

}
