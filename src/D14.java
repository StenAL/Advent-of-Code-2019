import java.util.*;
import java.util.stream.Collectors;

public class D14 {

    public static void main(String[] args) {
        q1();
        q2();
    }

    public static void q1() {
        List<String> input = helper.Helper.input(14);
        List<Reaction> reactions = input.stream()
            .map(reaction -> reaction.split(" => "))
            .map(reaction -> {
                List<ReactionPair> reactants = Arrays.stream(reaction[0].split(", "))
                    .map(reactant -> reactant.split(" "))
                    .map(reactant -> new ReactionPair(Integer.parseInt(reactant[0]), reactant[1]))
                    .collect(Collectors.toList());

                String[] resultArray = reaction[1].split(" ");
                ReactionPair result = new ReactionPair(Integer.parseInt(resultArray[0]), resultArray[1]);
                return new Reaction(reactants, result);
            })
            .collect(Collectors.toList());

        int oreCount = 0;
        Reaction reaction = findReaction(reactions, "FUEL");
        List<ReactionPair> needed = new ArrayList<>(reaction.reactants);
        magicSort(needed, reactions);
        Map<String, Integer> leftover = new HashMap<>();

        while (!needed.isEmpty()) {
            ReactionPair next = needed.remove(0);
            if (next.name.equals("ORE")) {
                oreCount += next.amount;
                continue;
            }
            reaction = findReaction(reactions, next.name);

            int existing = leftover.getOrDefault(next.name, 0);
            int amountNeeded = Math.max(0, next.amount - existing);
            leftover.put(next.name, Math.max(0, existing - amountNeeded));

            if (amountNeeded > 0) {
                double ratio = (double) amountNeeded/reaction.result.amount;
                int coefficent = Math.max(1, (int) Math.ceil(ratio));
                reaction.reactants.forEach(r -> needed.add(new ReactionPair(r.amount * coefficent, r.name)));
                leftover.put(next.name, leftover.getOrDefault(next.name, 0) + (coefficent*reaction.result.amount) - amountNeeded);
            }


            collapseNeeded(needed);
            magicSort(needed, reactions);
        }
        System.out.println("Ore " + oreCount);
    }

    public static void magicSort(List<ReactionPair> needed, List<Reaction> reactions) {
        needed.sort((a, b) -> {
            if (a.name.equals("ORE")) {
                if (b.name.equals("ORE")) {
                    return 0;
                }
                return 10000;
            }
            if (b.name.equals("ORE")) {
                return -10000;
            }

            if (findReaction(reactions, a.name).reactants.stream().anyMatch(x -> x.name.equals("ORE"))) {
                return 1000;
            } else if (findReaction(reactions, b.name).reactants.stream().anyMatch(x -> x.name.equals("ORE"))) {
                return -1000;
            }
            return 0;
        });
    }
    public static void collapseNeeded(List<ReactionPair> needed) {
        for (int i = 0; i < needed.size(); i++) {
            ReactionPair a = needed.get(i);
            for (int j = i + 1; j < needed.size(); j++) {
                ReactionPair b = needed.get(j);
                if (a.name.equals(b.name)) {
                    a.amount += b.amount;
                    b.amount = 0;
                }
            }
        }
        needed.removeIf(a -> a.amount == 0);
    }

    public static Reaction findReaction(List<Reaction> reactions, String result) {
        return reactions.stream()
            .filter(r -> r.result.name.equals(result))
            .findFirst().get();
    }

    public static void q2() {
        List<String> input = helper.Helper.input(14);
        List<Reaction2> reactions = input.stream()
            .map(reaction -> reaction.split(" => "))
            .map(reaction -> {
                List<ReactionPair2> reactants = Arrays.stream(reaction[0].split(", "))
                    .map(reactant -> reactant.split(" "))
                    .map(reactant -> new ReactionPair2(Long.parseLong(reactant[0]), reactant[1]))
                    .collect(Collectors.toList());

                String[] resultArray = reaction[1].split(" ");
                ReactionPair2 result = new ReactionPair2(Long.parseLong(resultArray[0]), resultArray[1]);
                return new Reaction2(reactants, result);
            })
            .collect(Collectors.toList());

        long oreCount = 0;
        Reaction2 reaction = findReaction2(reactions, "FUEL");
        List<ReactionPair2> needed = new ArrayList<>(reaction.reactants);
        long ans = 3568888; // trial and error lul
        needed.forEach(a -> a.amount *= ans);
        magicSort2(needed, reactions);
        Map<String, Long> leftover = new HashMap<>();

        while (!needed.isEmpty()) {
            ReactionPair2 next = needed.remove(0);
            if (next.name.equals("ORE")) {
                oreCount += next.amount;
                continue;
            }
            reaction = findReaction2(reactions, next.name);

            long existing = leftover.getOrDefault(next.name, 0L);
            long amountNeeded = Math.max(0, next.amount - existing);
            leftover.put(next.name, Math.max(0, existing - amountNeeded));

            if (amountNeeded > 0) {
                double ratio = (double) amountNeeded/reaction.result.amount;
                long coefficent = Math.max(1, (long) Math.ceil(ratio));
                reaction.reactants.forEach(r -> needed.add(new ReactionPair2(r.amount * coefficent, r.name)));
                leftover.put(next.name, leftover.getOrDefault(next.name, 0L) + (coefficent*reaction.result.amount) - amountNeeded);
            }


            collapseNeeded2(needed);
            magicSort2(needed, reactions);
        }

        System.out.println(oreCount);
        System.out.println("Delta " + (oreCount - 1000000000000L));
        System.out.println(ans);
    }

    public static class Reaction {
        List<ReactionPair> reactants;
        ReactionPair result;

        public Reaction(List<ReactionPair> reactants, ReactionPair result) {
            this.reactants = reactants;
            this.result = result;
        }

        @Override
        public String toString() {
            return "" +
                "" + reactants +
                " => " + result +
                '}';
        }
    }

    public static class ReactionPair {
        public int amount;
        public String name;

        public ReactionPair(int amount, String name) {
            this.amount = amount;
            this.name = name;
        }

        @Override
        public String toString() {
            return "{" +
                "" + amount +
                " " + name +
                '}';
        }
    }

    public static class Reaction2 {
        List<ReactionPair2> reactants;
        ReactionPair2 result;

        public Reaction2(List<ReactionPair2> reactants, ReactionPair2 result) {
            this.reactants = reactants;
            this.result = result;
        }

        @Override
        public String toString() {
            return "" +
                "" + reactants +
                " => " + result +
                '}';
        }
    }

    public static class ReactionPair2 {
        public long amount;
        public String name;

        public ReactionPair2(long amount, String name) {
            this.amount = amount;
            this.name = name;
        }

        @Override
        public String toString() {
            return "{" +
                "" + amount +
                " " + name +
                '}';
        }
    }

    public static void magicSort2(List<ReactionPair2> needed, List<Reaction2> reactions) {
        needed.sort((a, b) -> {
            if (a.name.equals("ORE")) {
                if (b.name.equals("ORE")) {
                    return 0;
                }
                return 10000;
            }
            if (b.name.equals("ORE")) {
                return -10000;
            }

            if (findReaction2(reactions, a.name).reactants.stream().anyMatch(x -> x.name.equals("ORE"))) {
                return 1000;
            } else if (findReaction2(reactions, b.name).reactants.stream().anyMatch(x -> x.name.equals("ORE"))) {
                return -1000;
            }
            return 0;
        });
    }
    public static void collapseNeeded2(List<ReactionPair2> needed) {
        for (int i = 0; i < needed.size(); i++) {
            ReactionPair2 a = needed.get(i);
            for (int j = i + 1; j < needed.size(); j++) {
                ReactionPair2 b = needed.get(j);
                if (a.name.equals(b.name)) {
                    a.amount += b.amount;
                    b.amount = 0;
                }
            }
        }
        needed.removeIf(a -> a.amount == 0);
    }

    public static Reaction2 findReaction2(List<Reaction2> reactions, String result) {
        return reactions.stream()
            .filter(r -> r.result.name.equals(result))
            .findFirst().get();
    }
}
