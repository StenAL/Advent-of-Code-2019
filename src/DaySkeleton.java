import java.util.List;

public abstract class DaySkeleton {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.test();
//        input = Helper.input();

    }

    public static void q2() {
        List<String> input = Helper.test();
//        input = Helper.input();
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

        public long run() {
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


                    long input = 0; // processing of actual input goes here


                    memory.set((int) par1, input);
                    programCounter += 2;
                } else if (opcode == 4) { // output
                    long par1 = getPar(par1mode, 1);


//                    System.out.println(par1); // processing of actual output goes here


                    programCounter += 2;
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
