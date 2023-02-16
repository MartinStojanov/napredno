public class CharTest {
    public static void main(String[] args) {
        String a = "Zdravo jas sum bartin";

        a.chars().forEach(i -> System.out.println(String.valueOf((char)i)));
    }
}
