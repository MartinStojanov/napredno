package av10;

//package mk.ukim.finki.vtor_kolokvium;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
class OperationNotAllowedException extends Exception{
    public OperationNotAllowedException(String message) {
        super(message);
    }
}
class Course{
    private String courseName;
    private int numStudents;
    private int sumGrades;

    public Course(String courseName) {
        this.courseName = courseName;
        numStudents=0;
        sumGrades=0;
    }
    public void increaseNumOfStudnets(){
        numStudents++;
    }
    public void increaseGradesSum(int grade){
        sumGrades+=grade;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getNumStudents() {
        return numStudents;
    }

    public int getSumGrades() {
        return sumGrades;
    }
    public double averageGradeForCourse(){
        return (sumGrades*1.0)/numStudents;
    }
}
abstract class Student{
private String studentId;
private Map<Integer, Map<String,Integer>> coursesByTerm;

private TreeSet<String> allCourses;

protected int numberOfPassedSubjects;
    public Student(String studentId) {
        this.studentId = studentId;
        coursesByTerm = new HashMap<>();
        allCourses = new TreeSet<>();
        numberOfPassedSubjects=0;
    }

    public String getStudentId() {
        return studentId;
    }

    public Map<Integer, Map<String, Integer>> getCoursesByTerm() {
        return coursesByTerm;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setCoursesByTerm(Map<Integer, Map<String, Integer>> coursesByTerm) {
        this.coursesByTerm = coursesByTerm;
    }

    public TreeSet<String> getAllCourses() {
        return allCourses;
    }

    public int getNumberOfPassedSubjects() {
        return numberOfPassedSubjects;
    }

    public abstract void addGradeToTerm(int term, String courseName, int grade) throws OperationNotAllowedException;
    public abstract boolean checkIfGraduated();
    public abstract int yearsOfStady();

    public double averageGrade(){
        double num= (getCoursesByTerm().values().stream().mapToDouble(i -> i.values().stream()
                .mapToInt(tmp -> tmp.intValue())
                .average().orElse(5.00)).average().orElse(5.00));
        return (num%1.0)>5.0 ? Math.ceil(num) : num;
    }
    protected double averageGradeTerm(int term){
        return  coursesByTerm.get(term).values().stream().mapToInt(i->i.intValue()).average().orElse(5.0);
    }
    public String detailedReport(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s\n",studentId));
        coursesByTerm.entrySet().forEach(term ->{
            int termNum = term.getKey();
            Map<String, Integer> courses = term.getValue();
            sb.append(String.format("Term: %d\n",termNum));
           sb.append(String.format("Courses for term: %d\n",courses.size()));
            sb.append(String.format("Average grade for term: %.2f\n",averageGradeTerm(termNum)));
        });
        sb.append(String.format("Average grade %.2f\n",averageGrade()));
        sb.append("Courses attended");
        sb.append(String.join(", ",allCourses));
        return sb.toString();
    }
}
class ThreeYearsDegreeStudent extends Student{

    private static int yearsOfStudy = 3;
    private static int numbersOfSubjects = 18;
    public ThreeYearsDegreeStudent(String studentId) {
        super(studentId);
        IntStream.range(1,7).boxed().forEach(i -> getCoursesByTerm().put(i,new HashMap<>()));
    }

    @Override
    public void addGradeToTerm(int term, String courseName, int grade) throws OperationNotAllowedException {
        if(term > 6){
            throw new OperationNotAllowedException(
                    String.format("Term %d is not possible for student with ID %s.",term,getStudentId()));
        }
        getCoursesByTerm().putIfAbsent(term,new HashMap<>());
        if((getCoursesByTerm().get(term).values().size())==3){
            throw new OperationNotAllowedException(
                    String.format("Student %s already has 3 grades in term %d.",getStudentId(),term));
        }
        getCoursesByTerm().get(term).put(courseName,grade);
        getAllCourses().add(courseName);
        numberOfPassedSubjects++;
    }

    @Override
    public boolean checkIfGraduated() {
        return numberOfPassedSubjects==numbersOfSubjects;
    }

    @Override
    public int yearsOfStady() {
        return yearsOfStudy;
    }
//
//    @Override
//    public double averageGrade() {
//        return getCoursesByTerm().values().stream().mapToDouble(i -> i.values().stream()
//                .mapToDouble(tmp -> tmp.intValue()*1.0)
//                .average().orElse(5.00)).average().orElse(5.00);
//
//    }

//
//    @Override
//    public String detailedReport() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(String.format("Student: %s\n",getStudentId()));
//        getCoursesByTerm().entrySet().forEach(term ->{
//            int termNum = term.getKey();
//            int coursesNum = term.getValue().size();
//
//        });
//        return null;
//    }
}
class FourYearsDegreeStudent extends Student{

    private static int yearsOfStudy = 4;

    private static int numbersOfSubjects = 24;
    public FourYearsDegreeStudent(String studentId) {
        super(studentId);
        IntStream.range(1,9).boxed().forEach(i -> getCoursesByTerm().put(i,new HashMap<>()));
    }

    @Override
    public void addGradeToTerm(int term, String courseName, int grade) throws OperationNotAllowedException {
        if(term > 8){
            throw new OperationNotAllowedException(
                    String.format("Term %d is not possible for student with ID %s.",term,getStudentId()));
        }
        if(getCoursesByTerm().get(term).size()==3){
            throw new OperationNotAllowedException(
                    String.format("Student %s already has 3 grades in term %d.",getStudentId(),term));
        }
        getCoursesByTerm().get(term).put(courseName,grade);
        getAllCourses().add(courseName);
        numberOfPassedSubjects++;
    }
    public int yearsOfStady(){
        return yearsOfStudy;
    }

//    @Override
//    public double averageGrade() {
//        int sumGrades = getCoursesByTerm().values().stream().mapToInt(i -> i.values().stream().mapToInt(tmp -> tmp.intValue())
//                .sum()).sum();
//        return sumGrades * 1.0 / numbersOfSubjects;
//    }
//
//    @Override
//    public String detailedReport() {
//        return null;
//    }

    @Override
    public boolean checkIfGraduated() {
        return numberOfPassedSubjects==numbersOfSubjects;
    }
}


    class Faculty {
        List<Student> students;
        Map<String,Student> studentsbyId;
        List<String> logs;

        Map<String,Course> coursesByName;
    public Faculty() {
        students = new ArrayList<>();
        studentsbyId = new HashMap<>();
        logs = new ArrayList<>();
        coursesByName = new HashMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        if (yearsOfStudies == 4) {
            students.add(new FourYearsDegreeStudent(id));
            studentsbyId.put(id, new FourYearsDegreeStudent(id));
        }else{
            students.add(new ThreeYearsDegreeStudent(id));
            studentsbyId.put(id, new ThreeYearsDegreeStudent(id));
        }
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade)
            throws OperationNotAllowedException {
            studentsbyId.get(studentId).addGradeToTerm(term,courseName,grade);
            if(studentsbyId.get(studentId).checkIfGraduated()){
                    logs.add(String.format("Student with ID %s graduated with average grade %.2f in $d years.",
                            studentId,studentsbyId.get(studentId).averageGrade(),
                            studentsbyId.get(studentId).yearsOfStady()));
            studentsbyId.remove(studentId);
            }
            coursesByName.putIfAbsent(courseName,new Course(courseName));
            coursesByName.get(courseName).increaseNumOfStudnets();
            coursesByName.get(courseName).increaseGradesSum(grade);

    }

    String getFacultyLogs() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder = new StringBuilder(logs.stream().collect(Collectors.joining("/n")));
        return String.join("\n",logs);
    }

    String getDetailedReportForStudent(String id) {
        Student student = studentsbyId.get(id);
        return student.detailedReport();
    }

    void printFirstNStudents(int n) {
        Comparator<Student> comparator = Comparator.comparing(Student::getNumberOfPassedSubjects)
                .thenComparing(Student::averageGrade).thenComparing(Student::getStudentId);
        studentsbyId.values().stream().sorted(comparator).limit(n).forEach(i->
                System.out.println(String.format(
                        "Student: %s Courses passed: %d Average grade: %.2f.",
                        i.getStudentId(),i.getNumberOfPassedSubjects(),i.averageGrade())));
    }

    void printCourses() {
        Comparator<Course> comparator = Comparator.comparing(Course::getNumStudents)
                        .thenComparing(Course::averageGradeForCourse);
            coursesByName.values().stream().sorted(comparator)
                    .forEach(i->System.out.println(
                            String.format("%s %10d %10.2f",
                                    i.getCourseName(),
                                    i.getNumStudents(),
                                    i.averageGradeForCourse())));
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}
