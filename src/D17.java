import helper.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class D17 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<Long> codes = Helper.intCode(17);
        Computer c = new Computer(codes);
        c.run();
        List<Coordinate> coordinates = c.coordinates;
        List<Coordinate> intersections = coordinates.stream()
            .filter(coord -> coordinates.containsAll(List.of(
                new Coordinate(coord.x + 1, coord.y),
                new Coordinate(coord.x - 1, coord.y),
                new Coordinate(coord.x, coord.y + 1),
                new Coordinate(coord.x, coord.y - 1)
            )))
            .collect(Collectors.toList());
        int sum = intersections.stream()
            .mapToInt(coord -> coord.x * coord.y)
            .sum();
        System.out.println(sum);

    }

    public static void q2() {
        List<Long> codes = Helper.intCode(17);
        codes.set(0, 2L);
        // path: L,10,L,12,R,6,R,10,L,4,L,4,L,12,L,10,L,12,R,6,R,10,L,4,L,4,L,12,L,10,L,12,R,6,L,10,R,10,R,6,L,4,R,10,L,4,L,4,L,12,L,10,R,10,R,6,L,4,L,10,L,12,R,6,L,10,R,10,R,6,L,4
        // A: L,10,R,10,R,6,L,4
        // B: L,10,L,12,R,6
        // C: R,10,L,4,L,4,L,12
        // result:          B           C               B            C           B        A          C          A           B            A
        // B,C,B,C,B,A,C,A,B,A
        List<Integer> main = getAscii("B,C,B,C,B,A,C,A,B,A");
        List<Integer> a = getAscii("L,10,R,10,R,6,L,4");
        List<Integer> b = getAscii("L,10,L,12,R,6");
        List<Integer> c = getAscii("R,10,L,4,L,4,L,12");

        Computer2 computer = new Computer2(codes, main, a, b, c);
        computer.run();
    }

    public static List<Integer> getAscii(String code) {
        List<Integer> codes = code.chars().boxed().collect(Collectors.toList());
        codes.add(10);
        return codes;
    }

    public static class Computer {
        public List<Long> memory;
        public int programCounter;
        public long base;

        public int x;
        public int y;
        public List<Coordinate> coordinates;


        public Computer(List<Long> memory) {
            this.memory = memory;
            while (memory.size() < 10000) {
                memory.add(0L);
            }

            this.programCounter = 0;
            base = 0;
            coordinates = new ArrayList<>();
        }

        public long run() {
            for (; programCounter < memory.size(); ) {
                long code = memory.get(programCounter);
                StringBuilder s = new StringBuilder(String.valueOf(code));
                while (s.length() < 5) {
                    s.insert(0, "0");
                }
                long opcode = Long.parseLong(s.substring(s.length() - 2));
                String par3mode = String.valueOf(s.charAt(0));
                String par2mode = String.valueOf(s.charAt(1));
                String par1mode = String.valueOf(s.charAt(2));


                if (opcode == 3) { // input
                    long par1 = getParAlt(par1mode, 1);


                    long input = 0; // processing of actual input goes here


                    memory.set((int) par1, input);
                    programCounter += 2;
                } else if (opcode == 4) { // output
                    long par1 = getPar(par1mode, 1);


                    if (par1 == 35) {
                        coordinates.add(new Coordinate(x, y));
                        x++;
                        System.out.print("#");
                    } else if (par1 == 46) {
                        x++;
                        System.out.print(".");
                    } else if (par1 == 10) {
                        y++;
                        x = 0;
                        System.out.println();
                    }

                    programCounter += 2;
                } else if (opcode == 5) { // branch if not zero
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 != 0) programCounter = (int) par2;
                    else programCounter += 3;
                } else if (opcode == 6) { // branch if zero
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 == 0) programCounter = (int) par2;
                    else programCounter += 3;
                } else if (opcode == 9) { // increment base
                    long par = getPar(par1mode, 1);
                    base += par;
                    programCounter += 2;
                } else if (opcode == 99) { // halt
                    break;
                } else {
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
                        if (par1 < par2) memory.set((int) par3, 1L);
                        else memory.set((int) par3, 0L);
                        programCounter += 4;
                    } else if (opcode == 8) {
                        if (par1 == par2) memory.set((int) par3, 1L);
                        else memory.set((int) par3, 0L);
                        programCounter += 4;
                    }
                }
            }
            return Long.MIN_VALUE; // all programs should terminate in a halt, this should hopefully not happen
        }

        private long getPar(String mode, int offset) {
            switch (mode) {
                case "0":
                    return memory.get(memory.get((programCounter + offset)).intValue());
                case "1":
                    return memory.get((programCounter + offset));
                case "2":
                    return memory.get(memory.get(programCounter + offset).intValue() + (int) base);
                default:
                    return -1;
            }
        }

        private long getParAlt(String mode, int offset) {
            switch (mode) {
                case "0":
                    return memory.get(programCounter + offset);
                case "2":
                    return memory.get(programCounter + offset) + base;
                default:
                    return -1;
            }
        }

        @Override
        public String toString() {
            return "Computer{" +
                "programCounter=" + programCounter +
                ", memory=" + memory +
                '}';
        }
    }

    public static class Computer2 {
        public List<Long> memory;
        public int programCounter;
        public long base;

        public List<Integer> main;
        public List<Integer> a;
        public List<Integer> b;
        public List<Integer> c;

        public List<Integer> inputMethod;
        public int inputCount;
        public int inputIndex;


        public Computer2(List<Long> memory, List<Integer> main, List<Integer> a, List<Integer> b, List<Integer> c) {
            this.memory = memory;
            while (memory.size() < 10000) {
                memory.add(0L);
            }

            this.programCounter = 0;
            base = 0;
            this.main = main;
            this.a = a;
            this.b = b;
            this.c = c;
            inputCount = 0;
            inputIndex = 0;
            inputMethod = main;
        }

        public long run() {
            for (; programCounter < memory.size(); ) {
                long code = memory.get(programCounter);
                StringBuilder s = new StringBuilder(String.valueOf(code));
                while (s.length() < 5) {
                    s.insert(0, "0");
                }
                long opcode = Long.parseLong(s.substring(s.length() - 2));
                String par3mode = String.valueOf(s.charAt(0));
                String par2mode = String.valueOf(s.charAt(1));
                String par1mode = String.valueOf(s.charAt(2));


                if (opcode == 3) { // input
                    long par1 = getParAlt(par1mode, 1);


                    if (inputIndex == inputMethod.size()) {
                        inputCount++;
                        inputIndex = 0;
                    }

                    switch (inputCount) {
                        case 0:
                            inputMethod = main;
                            break;
                        case 1:
                            inputMethod = a;
                            break;
                        case 2:
                            inputMethod = b;
                            break;
                        case 3:
                            inputMethod = c;
                            break;
                        default:
                            inputMethod = main;
                    }

                    long input;
                    if (inputCount <= 3) {
                        input = inputMethod.get(inputIndex);
                        inputIndex++;
                    } else {
                        if (inputCount == 4) {
                            input = Character.getNumericValue('n');
                            inputCount++;
                        } else {
                            input = 10;
                        }
                    }


                    memory.set((int) par1, input);
                    programCounter += 2;
                } else if (opcode == 4) { // output
                    long par1 = getPar(par1mode, 1);


                    if (par1 == 35) {
                        System.out.print("#");
                    } else if (par1 == 46) {
                        System.out.print(".");
                    } else if (par1 == 10) {
                        System.out.println();
                    } else if (par1 == 94) {
                        System.out.print("^");
                    } else {
                        System.out.println(par1);
                    }

                    programCounter += 2;
                } else if (opcode == 5) { // branch if not zero
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 != 0) programCounter = (int) par2;
                    else programCounter += 3;
                } else if (opcode == 6) { // branch if zero
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 == 0) programCounter = (int) par2;
                    else programCounter += 3;
                } else if (opcode == 9) { // increment base
                    long par = getPar(par1mode, 1);
                    base += par;
                    programCounter += 2;
                } else if (opcode == 99) { // halt
                    break;
                } else {
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
                        if (par1 < par2) memory.set((int) par3, 1L);
                        else memory.set((int) par3, 0L);
                        programCounter += 4;
                    } else if (opcode == 8) {
                        if (par1 == par2) memory.set((int) par3, 1L);
                        else memory.set((int) par3, 0L);
                        programCounter += 4;
                    }
                }
            }
            return Long.MIN_VALUE; // all programs should terminate in a halt, this should hopefully not happen
        }

        private long getPar(String mode, int offset) {
            switch (mode) {
                case "0":
                    return memory.get(memory.get((programCounter + offset)).intValue());
                case "1":
                    return memory.get((programCounter + offset));
                case "2":
                    return memory.get(memory.get(programCounter + offset).intValue() + (int) base);
                default:
                    return -1;
            }
        }

        private long getParAlt(String mode, int offset) {
            switch (mode) {
                case "0":
                    return memory.get(programCounter + offset);
                case "2":
                    return memory.get(programCounter + offset) + base;
                default:
                    return -1;
            }
        }

        @Override
        public String toString() {
            return "Computer{" +
                "programCounter=" + programCounter +
                ", memory=" + memory +
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
