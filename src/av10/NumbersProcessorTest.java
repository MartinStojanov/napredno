package av10;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class NumberProcessor{
    List<List<Integer>> lines;
    public NumberProcessor() {
        lines = new ArrayList<>();
    }
    private List<Integer> numInLine(String line){
        String[] parts = line.split("\\s+");
        return Arrays.stream(parts).map(i->Integer.parseInt(i)).collect(Collectors.toList());
    }
    public void readRows(InputStream in) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        bf.lines().
                forEach(i -> lines.add(numInLine(i)));
    }
    private String processLine(List<Integer> row){
        int max = row.stream().mapToInt(i ->i).max().orElse(0);

        HashMap<Integer, Long> var = row.stream().collect(Collectors.groupingBy(
                Integer::intValue,
                HashMap::new,
                Collectors.counting()

        ));
//        Map.Entry<Integer,Long> entry = new Map.Entry<Integer, Long>() {
//            @Override
//            public Integer getKey() {
//                return 0;
//            }
//
//            @Override
//            public Long getValue() {
//                return 0L;
//            }
//
//            @Override
//            public Long setValue(Long value) {
//                return null;
//            }
//        };
//        Comparator<Long> comparator = (num1,num2) -> {
//            if(num1>num2)
//                return 1;
//            else return -1;
//        };
//        Map.Entry<Integer, Long> tmp = var.entrySet().stream().sorted(Map.Entry.comparingByValue(comparator).
//                        thenComparing(i->i.getKey()).findFirst()
//                .orElse(entry);
//        int maxInList = tmp.getKey();
//        System.out.println(maxInList);
//        System.out.println(max);
//       return "";
        int maxFrequency
                = var.values().stream().mapToInt(i -> i.intValue()).max().getAsInt();
        int frequencuOnMaxNumber = var.get(max).intValue();
        if(maxFrequency == frequencuOnMaxNumber){
            return String.valueOf(max);
        }
        else return  " ";
    }
    public void printMaxFromRows(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        lines.stream().forEach(i-> out.println(processLine(i)));
    }
}
public class NumbersProcessorTest {
    public static void main(String[] args) throws FileNotFoundException {


        NumberProcessor numberProcessor = new NumberProcessor();
        File file = new File("C:\\Users\\Martin\\IdeaProjects\\napredno\\src\\av10\\numbers");
        FileInputStream in = new FileInputStream(file);
        numberProcessor.readRows(in);

        numberProcessor.printMaxFromRows(System.out);


    }
}
