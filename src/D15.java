import helper.Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D15 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(15);
        List<Long> codes = Arrays.stream(input.get(0).split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());

        moveHelper1(new Computer(codes), 1, 1);
        moveHelper1(new Computer(codes), 2, 1);
        moveHelper1(new Computer(codes), 3, 1);
        moveHelper1(new Computer(codes), 4, 1);

    }

    public static void moveHelper1(Computer computer, long move, int depth) {
        Computer computerCopy = new Computer(new ArrayList<>(computer.memory));
        computerCopy.programCounter = computer.programCounter;
        computerCopy.base = computer.base;
        long output = computerCopy.run(move);

        if (output == 1) {
            List<Long> moves = List.of(1L, 2L, 3L, 4L);
            List<Long> validMoves = new ArrayList<>(moves);
            validMoves.removeIf(potentialMove -> {
                if (move == 1 && potentialMove == 2) return true;
                if (move == 2 && potentialMove == 1) return true;
                if (move == 3 && potentialMove == 4) return true;
                if (move == 4 && potentialMove == 3) return true;
                return false;
            });

            for (Long validMove : validMoves) {
                moveHelper1(computerCopy, validMove, depth + 1);
            }
        } else if (output == 2) {
            System.out.println(depth);
        }

    }

    public static void q2() {
        List<String> input = Helper.input(15);
        List<Long> codes = Arrays.stream(input.get(0).split(","))
            .map(Long::parseLong)
            .collect(Collectors.toList());

        List<Integer> moves = new ArrayList<>();
        moveHelper2(new Computer(codes), 2, 1, false, moves);
        System.out.println(moves.stream().max(Integer::compareTo).get());
    }

    public static void moveHelper2(Computer computer, long move, int depth, boolean returning, List<Integer> movesToDeadEnd) {
        Computer computerCopy = new Computer(new ArrayList<>(computer.memory));
        computerCopy.programCounter = computer.programCounter;
        computerCopy.base = computer.base;
        long output = computerCopy.run(move);

        if (output == 1) {
            List<Long> moves = List.of(1L, 2L, 3L, 4L);
            List<Long> validMoves = new ArrayList<>(moves);
            validMoves.removeIf(potentialMove -> {
                if (move == 1 && potentialMove == 2) return true;
                if (move == 2 && potentialMove == 1) return true;
                if (move == 3 && potentialMove == 4) return true;
                if (move == 4 && potentialMove == 3) return true;
                return false;
            });

            List<Long> outputs = new ArrayList<>();
            for (Long validMove : validMoves) {
                outputs.add(tryMove(computerCopy, validMove));
            }
            if (outputs.stream().allMatch(o -> o == 0) && returning) {
                movesToDeadEnd.add(depth);
            }

            for (Long validMove : validMoves) {
                moveHelper2(computerCopy, validMove, depth + 1, returning, movesToDeadEnd);
            }

        } else if (output == 2) {
            long validMove = 0;
            if (move == 1)  validMove = 2;
            if (move == 2)  validMove = 1;
            if (move == 3)  validMove = 4;
            if (move == 4)  validMove = 3;
            moveHelper2(computerCopy, validMove, 1, true, movesToDeadEnd);
        }
    }

    public static long tryMove(Computer computer, long move) {
        Computer computerCopy = new Computer(new ArrayList<>(computer.memory));
        computerCopy.programCounter = computer.programCounter;
        computerCopy.base = computer.base;
        return computerCopy.run(move);
    }


    public static class Computer {
        public List<Long> memory;
        public int programCounter;
        public long base;


        public Computer(List<Long> memory) {
            this.memory = memory;
            while (memory.size() < 10000) {
                memory.add(0L);
            }

            this.programCounter = 0;
            base = 0;
        }

        public long run(long input) {
            for (;programCounter < memory.size();) {
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


//                    long input = 0; // processing of actual input goes here


                    memory.set((int) par1, input);
                    programCounter += 2;
                } else if (opcode == 4) { // output
                    long par1 = getPar(par1mode, 1);


//                    System.out.println(par1); // processing of actual output goes here
                    programCounter += 2;
                    return par1;


                } else if (opcode == 5) { // branch if not zero
                    long par1 = getPar(par1mode, 1);
                    long par2 = getPar(par2mode, 2);
                    if (par1 != 0) programCounter = (int) par2; else programCounter += 3;
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
                        if (par1 < par2) memory.set((int) par3, 1L); else memory.set((int) par3, 0L);
                        programCounter += 4;
                    } else if (opcode == 8) {
                        if (par1 == par2) memory.set((int) par3, 1L); else memory.set((int) par3, 0L);
                        programCounter += 4;
                    }
                }
            }
            return Long.MIN_VALUE; // all programs should terminate in a halt, this should hopefully not happen
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
            return "Computer{" +
                "programCounter=" + programCounter +
                ", memory=" + memory +
                '}';
        }
    }
}
