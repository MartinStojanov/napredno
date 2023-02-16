package av9;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class NamesTest {
    private static Map<String,Integer> readNames(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        Map<String,Integer> result =  new HashMap<>();
//        br.lines()
//                .forEach(line -> {
//                    String [] parts = line.split("\\s+");
//                    String name = parts[0];
//                    int age = Integer.parseInt(parts[1]);
//                    result.put(name,age);
//                });
        return br.lines().collect(Collectors.toMap(
                line -> line.split("\\s+")[0],
                line -> Integer.parseInt(line.split("\\s+")[1])
        ));
    }

    public static void main(String[] args) throws FileNotFoundException {
        File fileBoys = new File("C:\\Users\\Martin\\IdeaProjects\\napredno\\src\\av9\\boyNames");
        File fileGirls = new File("C:\\Users\\Martin\\IdeaProjects\\napredno\\src\\av9\\girlNames");
        Map<String,Integer> girlNames = readNames(new FileInputStream(fileGirls));
        Map<String,Integer> boysNames = readNames(new FileInputStream(fileBoys));
        girlNames.forEach((k,v) -> boysNames.computeIfPresent(k,(key,val)->val+v));

        Set<String> uniqeNames = new HashSet<>();
        uniqeNames.addAll(boysNames.keySet());
        uniqeNames.addAll(girlNames.keySet());
        Map<String,Integer> unisexNames = uniqeNames.stream()
                .filter(name -> girlNames.containsKey(name) && boysNames.containsKey(name))
                .collect(Collectors.toMap(
                        name -> name,
                        name -> boysNames.get(name)
                ));
        Map<String,Integer> topTen =
                unisexNames.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println(topTen);

    }
}
