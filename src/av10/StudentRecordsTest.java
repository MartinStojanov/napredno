package av10;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 1
 */
class StudentDetail{
    private String kod;
    private String nasoka;
    private List<Integer> ocenki;

    public StudentDetail(String kod, String nasoka, List<Integer> ocenki) {
        this.kod = kod;
        this.nasoka = nasoka;
        this.ocenki = ocenki;
    }

}
class StudentRecords{

    private List<StudentDetail> studentDetails;
    private TreeMap<String,List<StudentDetail>> studentsByTerm;

    public StudentRecords() {
        studentDetails = new ArrayList<>();
        studentsByTerm = new TreeMap<>();
    }
    public void create(String line){
        String[] parts = line.split("\\s+");
        String code = parts[0];
        String term = parts[1];
        List<Integer> grades = new ArrayList<>();
        Arrays.stream(parts)
                .skip(2).forEach(i -> grades.add(Integer.parseInt(i)));
        StudentDetail studentDetail = new StudentDetail(code,term,grades);
        studentsByTerm.putIfAbsent(term,new ArrayList<>());
        studentsByTerm.computeIfPresent(term,(k,v) -> )
        return new StudentDetail(code,term,grades);
    }

    public int readRecords(InputStream in) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));
        bf.lines().forEach(i -> create(i));

       return studentDetails.size();
    }

    public void writeTable(PrintStream out) {

    }

    public void writeDistribution(PrintStream out) {


    }
}
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}