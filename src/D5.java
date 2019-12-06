import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class D5 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = Helper.input(5);
        List<Integer> codes = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

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
                codes.set(par, 1);
                i += 2;
            } else if (opcode == 4) {
                int par = par1mode.equals("0") ? codes.get(codes.get(i + 1)) : codes.get(i + 1);
                System.out.println(par);
                i += 2;
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
                }
            }
        }
    }

    public static void q2() {
        List<String> input = Helper.input(5);
        List<Integer> codes = Arrays.stream(input.get(0).split(","))
            .map(Integer::parseInt)
            .collect(Collectors.toList());

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
                codes.set(par, 5);
                i += 2;
            } else if (opcode == 4) {
                int par = par1mode.equals("0") ? codes.get(codes.get(i + 1)) : codes.get(i + 1);
                System.out.println(par);
                i += 2;
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
    }
}
