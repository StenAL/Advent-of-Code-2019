import helper.Helper;

import java.util.*;
import java.util.stream.Collectors;

public class D12 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(12);
        List<Planet> planets = input.stream()
            .map(line -> Arrays.stream(line.split(","))
                .map(splitLine -> splitLine.split("=")[1])
                .collect(Collectors.toList()))
            .map(coords -> new Planet(Integer.parseInt(coords.get(0)),
                Integer.parseInt(coords.get(1)),
                Integer.parseInt(coords.get(2).substring(0, coords.get(2).length() - 1))))
            .collect(Collectors.toList());


        for (int i = 0; i < 1000; i++) {
            planets.forEach(planet -> planet.calculateGravity(planets));
            planets.forEach(Planet::move);
        }

        int totalEnergy = planets.stream()
            .mapToInt(Planet::getEnergy)
            .sum();
        System.out.println(totalEnergy);
    }

    public static void q2() {
        List<String> input = Helper.input(12);
        List<Planet> planets = input.stream()
            .map(line -> Arrays.stream(line.split(","))
                .map(splitLine -> splitLine.split("=")[1])
                .collect(Collectors.toList()))
            .map(coords -> new Planet(Integer.parseInt(coords.get(0)),
                Integer.parseInt(coords.get(1)),
                Integer.parseInt(coords.get(2).substring(0, coords.get(2).length() - 1))))
            .collect(Collectors.toList());


        long x = findPeriod(planets.get(0).x, planets.get(1).x, planets.get(2).x, planets.get(3).x);
        long y = findPeriod(planets.get(0).y, planets.get(1).y, planets.get(2).y, planets.get(3).y);
        long z = findPeriod(planets.get(0).z, planets.get(1).z, planets.get(2).z, planets.get(3).z);
        System.out.println(lcm(x, y, z));

    }

    private static long gcd(long x, long y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    public static long lcm(long... numbers) {
        return Arrays.stream(numbers).reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }

    public static long findPeriod(int s1, int s2, int s3, int s4) {
        // two planets motions always cancel each other out => sum of velocities is always 0
        // this means sum of each coordinate remains unchanged over time

        // x1 + x2 + x3 + x4 = C
        // x1v + x2v + x3v + x4v = 0

        List<List<Integer>> inital = List.of(new ArrayList<>(List.of(s1, 0)),
            new ArrayList<>(List.of(s2, 0)),
            new ArrayList<>(List.of(s3, 0)),
            new ArrayList<>(List.of(s4, 0)));

        List<List<Integer>> x = new ArrayList<>();
        for (List<Integer> xCoord : inital) {
            x.add(new ArrayList<>(xCoord));
        }

        long i = 0;
        while (true) {
            for (List<Integer> xCoord : x) { // calculate velocities
                for (List<Integer> xcoord2 : x) {
                    if (xcoord2.get(0) > xCoord.get(0)) {
                        xCoord.set(1, xCoord.get(1) + 1);
                    } else if (xcoord2.get(0) < xCoord.get(0)) {
                        xCoord.set(1, xCoord.get(1) - 1);
                    }
                }
            }
            for (List<Integer> xCoord : x) { // calculate new positions
                xCoord.set(0, xCoord.get(0) + xCoord.get(1));
            }
            i++;
            if (x.equals(inital)) break;
        }
        return i;
    }

    public static class Planet {
        public int x;
        public int y;
        public int z;

        public int xVelocity;
        public int yVelocity;
        public int zVelocity;

        public Planet(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;

            xVelocity = 0;
            yVelocity = 0;
            zVelocity = 0;
        }

        public void calculateGravity(List<Planet> planets) {
            for (Planet planet : planets) {
                if (planet.x > x) {
                    xVelocity+=2;
                } else if (planet.x < x) {
                    xVelocity-=2;
                }

                if (planet.y > y) {
                    yVelocity+=2;
                } else if (planet.y < y) {
                    yVelocity-=2;
                }

                if (planet.z > z) {
                    zVelocity+=2;
                } else if (planet.z < z) {
                    zVelocity-=2;
                }
            }
        }

        public void move() {
            x += xVelocity;
            y += yVelocity;
            z += zVelocity;
        }

        public int getEnergy() {
            int pot = Math.abs(x) + Math.abs(y) + Math.abs(z);
            int kin = Math.abs(xVelocity) + Math.abs(yVelocity) + Math.abs(zVelocity);
            return pot * kin;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Planet planet = (Planet) o;

            if (x != planet.x) return false;
            if (y != planet.y) return false;
            if (z != planet.z) return false;
            if (xVelocity != planet.xVelocity) return false;
            if (yVelocity != planet.yVelocity) return false;
            return zVelocity == planet.zVelocity;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            result = 31 * result + xVelocity;
            result = 31 * result + yVelocity;
            result = 31 * result + zVelocity;
            return result;
        }

        @Override
        public String toString() {
            return "{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", xVelocity=" + xVelocity +
                ", yVelocity=" + yVelocity +
                ", zVelocity=" + zVelocity +
                '}';
        }
    }

}
