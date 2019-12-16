import helper.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D9 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(9);
        List<Long> codes = Arrays.stream(input.get(0).split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());
        Computer c = new Computer(new ArrayList<>(codes), 1);
        c.run();
    }

    public static void q2() {
        List<String> input = Helper.input(9);
        List<Long> codes = Arrays.stream(input.get(0).split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());
        Computer c = new Computer(new ArrayList<>(codes), 2);
        c.run();
    }

    public static class Computer {
        public List<Long> memory;
        public int programCounter;
        public long phase;
        public boolean firstInput;
        public long base;
        public long input;

        public Computer(List<Long> memory, int input) {
            this.memory = memory;
            while (memory.size() < 10000) {
                memory.add(0L);
            }

            this.input = input;
            this.programCounter = 0;
            firstInput = true;
            base = 0;
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
                    memory.set((int) par1, input);
                    programCounter += 2;
                } else if (opcode == 4) { // output
                    long par = getPar(par1mode, 1);
                    programCounter += 2;
                    System.out.println(par);
                } else if (opcode == 5) {
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 != 0) programCounter = (int) par2; else programCounter += 3;
                } else if (opcode == 6) {
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 == 0) programCounter = (int) par2;
                    else programCounter += 3;
                } else if (opcode == 9) {
                    long par = getPar(par1mode, 1);
                    base += par;
                    programCounter += 2;
                } else {
                    if (opcode == 99) {
                        break;
                    }

                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    long par3 = getParAlt(par3mode, 3);
                    if (opcode == 1) {
                        memory.set((int) par3, par1 + par2);
                        programCounter += 4;
                    } else if (opcode == 2) {
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
                ", phase=" + phase +
                '}';
        }
    }
}
