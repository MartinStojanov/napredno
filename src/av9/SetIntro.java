package av9;

import java.util.*;

public class SetIntro {
    public static void main(String[] args) {
        Set<String> hashStringSet =  new HashSet<>();
        hashStringSet.add("Napredno");
        hashStringSet.add("Martin");
        hashStringSet.add("Martin");
        hashStringSet.add("Stojanov");
        System.out.println(hashStringSet);

        Comparator<String> stringComparator = (o1,o2) -> Character.compare(o1.charAt(4),o2.charAt(4));
        Set<String> treeSet = new TreeSet<>(stringComparator);
        treeSet.add("Napredno");
        treeSet.add("Martin");
        treeSet.add("martin");
        treeSet.add("Stojanov");

        System.out.println(treeSet);

        Set<String> linkedSet = new LinkedHashSet<>();
        linkedSet.add("Napredno");
        linkedSet.add("Martin");
        linkedSet.add("martin");
        linkedSet.add("Stojanov");
        System.out.println(linkedSet);
    }
}
