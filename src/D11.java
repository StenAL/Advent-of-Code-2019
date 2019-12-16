import helper.Helper;

import java.util.*;
import java.util.stream.Collectors;

public class D11 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(11);
        List<Long> codes = Arrays.stream(input.get(0).split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());

        Computer computer = new Computer(codes);
        Robot robot = new Robot(computer);
        computer.run();

        Set<Coordinate> painted = new HashSet<>();
        painted.addAll(robot.black);
        painted.addAll(robot.white);
        System.out.println(painted.size());
    }

    public static void q2() {
        List<String> input = Helper.input(11);
        List<Long> codes = Arrays.stream(input.get(0).split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());

        Computer computer = new Computer(codes);
        Robot robot = new Robot(computer);
        robot.white.add(new Coordinate(0, 0));
        computer.run();

        Set<Coordinate> white = robot.white;
        int minX = -1;
        int minY = -1;
        for (Coordinate coordinate : white) {
            if (minX == -1 || coordinate.x < minX) {
                minX = coordinate.x;
            }
            if (minY == -1 || coordinate.y < minY) {
                minY = coordinate.y;
            }
        }

        int maxX = 0;
        int maxY = 0;
        for (Coordinate coordinate : white) { // normalize all coordinates to start at 0,0
            coordinate.x += (minX * (-1));
            coordinate.y += (minY * (-1));
            if (coordinate.x > maxX) {
                maxX = coordinate.x;
            }
            if (coordinate.y > maxY) {
                maxY = coordinate.y;
            }
        }
        System.out.println(white);

        for (int i = 0; i <= maxY; i++) {
            List<String> line = new ArrayList<>();
            for (int j = 0; j <= maxX; j++) {
                int finalI = i;
                int finalJ = j;
                if (white.stream().anyMatch(a -> a.x == finalJ && a.y == finalI)) {
                    line.add("#");
                } else {
                    line.add(".");
                }
            }
            System.out.println(line); // prints upside-down mirrored answer
        }

    }

    public static class Robot {

        public Computer computer;
        public int x;
        public int y;
        public String direction;
        public Set<Coordinate> black;
        public Set<Coordinate> white;

        public Robot(Computer computer) {
            this.computer = computer;
            computer.robot = this;
            x = 0;
            y = 0;
            black = new HashSet<>();
            white = new HashSet<>();
            direction = "U";
        }

        public void move() {
            switch (direction) {
                case "U":
                    y += 1;
                    break;
                case "R":
                    x += 1;
                    break;
                case "D":
                    y -= 1;
                    break;
                case "L":
                    x -= 1;
                    break;
            }
        }

        public void turn(String dir) {
            if (dir.equals("L")) {
                switch (direction) {
                    case "U":
                        direction = "L";
                        break;
                    case "R":
                        direction = "U";
                        break;
                    case "D":
                        direction = "R";
                        break;
                    case "L":
                        direction = "D";
                        break;
                }
            } else {
                switch (direction) {
                    case "U":
                        direction = "R";
                        break;
                    case "R":
                        direction = "D";
                        break;
                    case "D":
                        direction = "L";
                        break;
                    case "L":
                        direction = "U";
                        break;
                }
            }
        }

    }

    public static class Computer {
        public Robot robot;
        public List<Long> memory;
        public int programCounter;
        public long base;
        public boolean outputColor;


        public Computer(List<Long> memory) {
            this.memory = memory;
            while (memory.size() < 10000) {
                memory.add(0L);
            }

            this.programCounter = 0;
            base = 0;
            outputColor = true;
        }

        public long run() {
            for (;programCounter < memory.size();) {
                long code = memory.get(programCounter);
                String s = String.valueOf(code);
                while (s.length() < 5) {
                    s = "0" + s;
                }
                long opcode = Long.parseLong(s.substring(s.length() - 2));
                String par3mode = String.valueOf(s.charAt(0));
                String par2mode = String.valueOf(s.charAt(1));
                String par1mode = String.valueOf(s.charAt(2));


                if (opcode == 3) { // input
                    long par1 = getParAlt(par1mode, 1);
                    Coordinate location = new Coordinate(robot.x, robot.y);
                    long input = robot.white.contains(location) ? 1 : 0;
                    memory.set((int) par1, input);
                    programCounter += 2;
                } else if (opcode == 4) { // output
                    long par = getPar(par1mode, 1);
                    Coordinate location = new Coordinate(robot.x, robot.y);

                    if (outputColor) {
                        if (par == 1) {
                            robot.white.add(location);
                            robot.black.remove(location);
                        } else {
                            robot.black.add(location);
                            robot.white.remove(location);
                        }
                        outputColor = false;
                    } else {
                        if (par == 0) {
                            robot.turn("L");
                        } else {
                            robot.turn("R");
                        }
                        robot.move();
                        outputColor = true;
                    }
                    programCounter += 2;
//                    System.out.println(par);
                } else if (opcode == 5) { // branch if not zero
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 != 0) programCounter = (int) par2; else programCounter += 3;
                } else if (opcode == 6) { // branch if zero
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 == 0) programCounter = (int) par2;
                    else programCounter += 3;
                } else if (opcode == 9) { // change base
                    long par = getPar(par1mode, 1);
                    base += par;
                    programCounter += 2;
                } else {
                    if (opcode == 99) { // halt
                        break;
                    }

                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    long par3 = getParAlt(par3mode, 3);
                    if (opcode == 1) { // add
                        memory.set((int) par3, par1 + par2);
                        programCounter += 4;
                    } else if (opcode == 2) { // multiply
                        memory.set((int) par3, par1 * par2);
                        programCounter += 4;
                    } else if (opcode == 7) {
                        if (par1 < par2) memory.set((int) par3, 1L); else memory.set((int) par3, 0L);
                        programCounter += 4;
                    } else if (opcode == 8) {
                        if (par1 == par2) memory.set((int) par3, 1L); else memory.set((int) par3, 0L);
                        programCounter += 4;
                    }
                }
            }
            return Long.MIN_VALUE;
        }

        private long getPar(String mode, int offset) {
            switch (mode) {
                case "0": return memory.get(memory.get((programCounter + offset)).intValue());
                case "1": return memory.get((programCounter + offset));
                case "2": return memory.get(memory.get(programCounter + offset).intValue() + (int) base);
                default: return -1;
            }
        }

        private long getParAlt(String mode, int offset) {
            switch (mode) {
                case "0": return memory.get(programCounter + offset);
                case "2": return memory.get(programCounter + offset) + base;
                default: return -1;
            }
        }

        @Override
        public String toString() {
            return "Amplifier{" +
                "memory=" + memory +
                ", programCounter=" + programCounter +
                '}';
        }
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
