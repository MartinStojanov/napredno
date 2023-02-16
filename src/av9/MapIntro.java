package av9;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapIntro {
    public static void main(String[] args) {
        Map<String, Integer> map;
        //se zadrzuvav redosledot na vnesuvanje na elementite
        System.out.println("Pecatenje na elementi vo linked hash map");
        map = new LinkedHashMap<>();
        map.put("NP1",1);
        map.put("NP2",2);
        map.put("NP3",2);

        System.out.println(map);

        //ako probame da dodademe kluc sto postoi togas, se mnuva samo value za toj kluc
        map.put("NP1",7);
        System.out.println(map);

        System.out.println("Pecatenje na elementi vo tree map");
        map =  new TreeMap<>();
        // redosledot e sortiran spored Comparable na klucot k
        map.put("NP1",1);
        map.put("NP3",2);
        map.put("NP2",2);
   Map<Integer,Integer> mapa = new TreeMap<>();
   mapa.put(4,5);
   mapa.put(3,7);
        System.out.println("Prv element e");
        System.out.println(mapa.keySet().stream().findFirst().orElse(0));

        //da dobieme mnozestvo od site klucevi
//        System.out.println(map.keySet());
//
//        //da dobieme mnozestvo od values
//        System.out.println(map.values());



        System.out.println(map);

        //ako probame da dodademe kluc sto postoi togas, se mnuva samo value za toj kluc
        map.put("NP1",7);
        System.out.println(map);

    }
}
