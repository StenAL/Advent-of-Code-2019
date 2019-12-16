import helper.Helper;

import java.util.*;
import java.util.stream.Collectors;

public class D7 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {

        List<String> input = Helper.input(7);
        List<Integer> codes = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        List<Integer> phases = new ArrayList<>(List.of(0, 1, 2, 3, 4));
        List<Integer> results = new ArrayList<>();

        // dont judge me
        for (int phase : phases) {
            int sig1 = runMachine1(new ArrayList<>(codes), phase, 0);
            List<Integer> phases2 = new ArrayList<>(phases);
            phases2.remove((Integer) phase);

            for (int phase2 : phases2) {
                int sig2 = runMachine1(new ArrayList<>(codes), phase2, sig1);
                List<Integer> phases3 = new ArrayList<>(phases2);
                phases3.remove((Integer) phase2);
                for (int phase3 : phases3) {
                    int sig3 = runMachine1(new ArrayList<>(codes), phase3, sig2);
                    List<Integer> phases4 = new ArrayList<>(phases3);
                    phases4.remove((Integer) phase3);
                    for (int phase4 : phases4) {
                        int sig4 = runMachine1(new ArrayList<>(codes), phase4, sig3);
                        List<Integer> phases5 = new ArrayList<>(phases4);
                        phases5.remove((Integer) phase4);
                        for (int phase5 : phases5) {
                            int sig5 = runMachine1(new ArrayList<>(codes), phase5, sig4);
                            results.add(sig5);
                        }
                    }
                }
            }
        }
        System.out.println(results.stream().max(Integer::compareTo).get());
    }

    public static void q2() {

        List<String> input = Helper.input(7);
        List<Integer> codes = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        List<Integer> phases = new ArrayList<>(List.of(5, 6, 7, 8, 9));
        List<Integer> answers = new ArrayList<>();
        q2Helper(codes, phases, new ArrayList<>(), answers);
        System.out.println(answers.stream().max(Integer::compareTo).get());

//        q2Helper(codes, new ArrayList<>(), List.of(new Amplifier(codes, 0, 9),
//            new Amplifier(codes, 0, 8),
//            new Amplifier(codes, 0, 7),
//            new Amplifier(codes, 0, 6),
//            new Amplifier(codes, 0, 5)));
    }

    public static List<Integer> q2Helper(List<Integer> codes, List<Integer> phases, List<Amplifier> amplifiers, List<Integer> answers) {
        if (phases.size() == 0) {
            int signal = 0;
            int i = 0;
            for (Amplifier amplifier : amplifiers) {
                amplifier.programCounter = 0;
                amplifier.firstInput = true;
                amplifier.memory = new ArrayList<>(codes);
            }

            List<Integer> outputs = new ArrayList<>();
            while (signal != Integer.MIN_VALUE) {
                signal = amplifiers.get(i).run(signal, outputs);
                i = (i + 1) % amplifiers.size();
            }
            answers.add(outputs.get(outputs.size() - 1));
        }

        for (int phase : phases) {
            List<Amplifier> amplifiersCopy = new ArrayList<>(amplifiers);
            amplifiersCopy.add(new Amplifier(codes, 0, phase));

            List<Integer> remainingPhases = new ArrayList<>(phases);
            remainingPhases.remove((Integer) phase);

            q2Helper(codes, remainingPhases, amplifiersCopy, answers);
        }
        return answers;
    }

    public static int runMachine1(List<Integer> codes, int phase, int signal) {
        int inputNumber = 0;
        for (int i = 0; i < codes.size();) {
            int code = codes.get(i);
            String s = String.valueOf(code);
            while (s.length() < 5) {
                s = "0" + s;
            }
            int opcode = Integer.parseInt(s.substring(s.length() - 2));
            String par3mode = String.valueOf(s.charAt(0));
            String par2mode = String.valueOf(s.charAt(1));
            String par1mode = String.valueOf(s.charAt(2));


            if (opcode == 3) {
                int par = codes.get(i + 1);
                codes.set(par, inputNumber == 0 ? phase : signal);
                inputNumber++;
                i += 2;
            } else if (opcode == 4) {
                int par = par1mode.equals("0") ? codes.get(codes.get(i + 1)) : codes.get(i + 1);
                return par;
            } else if (opcode == 5) {
                int par1 = par1mode.equals("0") ? codes.get(codes.get(i + 1)) : codes.get(i + 1);
                int par2 = par2mode.equals("0") ? codes.get(codes.get(i + 2)) : codes.get(i + 2);
                if (par1 != 0) i = par2; else i += 3;
            } else if (opcode == 6) {
                int par1 = par1mode.equals("0") ? codes.get(codes.get(i + 1)) : codes.get(i + 1);
                int par2 = par2mode.equals("0") ? codes.get(codes.get(i + 2)) : codes.get(i + 2);
                if (par1 == 0) i = par2; else i += 3;
            } else {
                if (opcode == 99) break;

                int par1 = par1mode.equals("0") ? codes.get(codes.get(i + 1)) : codes.get(i + 1);
                int par2 = par2mode.equals("0") ? codes.get(codes.get(i + 2)) : codes.get(i + 2);
                int par3 = codes.get(i + 3);
                if (opcode == 1) {
                    codes.set(par3, par1 + par2);
                    i += 4;
                } else if (opcode == 2) {
                    codes.set(par3, par1 * par2);
                    i += 4;
                } else if (opcode == 7) {
                    if (par1 < par2) codes.set(par3, 1); else codes.set(par3, 0);
                    i += 4;
                } else if (opcode == 8) {
                    if (par1 == par2) codes.set(par3, 1); else codes.set(par3, 0);
                    i += 4;
                }
            }
        }
        return -1;
    }


    public static class Amplifier {
        public List<Integer> memory;
        public int programCounter;
        public int phase;
        public boolean firstInput;

        public Amplifier(List<Integer> memory, int i, int phase) {
            this.memory = memory;
            this.programCounter = i;
            this.phase = phase;
            firstInput = true;
        }

        public int run(int signal, List<Integer> outputs) {
            for (;programCounter < memory.size();) {
                int code = memory.get(programCounter);
                String s = String.valueOf(code);
                while (s.length() < 5) {
                    s = "0" + s;
                }
                int opcode = Integer.parseInt(s.substring(s.length() - 2));
                String par3mode = String.valueOf(s.charAt(0));
                String par2mode = String.valueOf(s.charAt(1));
                String par1mode = String.valueOf(s.charAt(2));


                if (opcode == 3) {
                    int par = memory.get(programCounter + 1);
                    memory.set(par, firstInput ? phase : signal);
                    firstInput = false;
                    programCounter += 2;
                } else if (opcode == 4) {
                    int par = par1mode.equals("0") ? memory.get(memory.get(programCounter + 1)) : memory.get(programCounter + 1);
                    programCounter += 2;
                    outputs.add(par);
                    return par;
                } else if (opcode == 5) {
                    int par1 = par1mode.equals("0") ? memory.get(memory.get(programCounter + 1)) : memory.get(programCounter + 1);
                    int par2 = par2mode.equals("0") ? memory.get(memory.get(programCounter + 2)) : memory.get(programCounter + 2);
                    if (par1 != 0) programCounter = par2; else programCounter += 3;
                } else if (opcode == 6) {
                    int par1 = par1mode.equals("0") ? memory.get(memory.get(programCounter + 1)) : memory.get(programCounter + 1);
                    int par2 = par2mode.equals("0") ? memory.get(memory.get(programCounter + 2)) : memory.get(programCounter + 2);
                    if (par1 == 0) programCounter = par2; else programCounter += 3;
                } else {
                    if (opcode == 99) {
                        break;
                    }

                    int par1 = par1mode.equals("0") ? memory.get(memory.get(programCounter + 1)) : memory.get(programCounter + 1);
                    int par2 = par2mode.equals("0") ? memory.get(memory.get(programCounter + 2)) : memory.get(programCounter + 2);
                    int par3 = memory.get(programCounter + 3);
                    if (opcode == 1) {
                        memory.set(par3, par1 + par2);
                        programCounter += 4;
                    } else if (opcode == 2) {
                        memory.set(par3, par1 * par2);
                        programCounter += 4;
                    } else if (opcode == 7) {
                        if (par1 < par2) memory.set(par3, 1); else memory.set(par3, 0);
                        programCounter += 4;
                    } else if (opcode == 8) {
                        if (par1 == par2) memory.set(par3, 1); else memory.set(par3, 0);
                        programCounter += 4;
                    }
                }
            }
            return Integer.MIN_VALUE;
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
