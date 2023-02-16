package cakeShop;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Item{
    private String name;
    private int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
class Order implements Comparable<Order>{
    private List<Item> items;
    private long id;

    public Order(long id) {
        this.id = id;
    }

    public Order(long id, List<Item> items) {
        this.items = items;
        this.id = id;
    }

    public static Order create(String line){
        String[] parts = line.split("\\s+");
        long id = Long.parseLong(parts[0]);
        List<Item> items1 = new ArrayList<>();
        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if(Character.isDigit(i.charAt(0))){
                        items1.get(items1.size()-1).setPrice(Integer.parseInt(i));
                    }else{
                        items1.add(new Item(i));
                    }
                });
        return new Order(id, items1);
    }

    public List<Item> getItems() {
        return items;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                "totalOrderItems" + items.size() +
                '}';
    }

    @Override
    public int compareTo(Order o) {
        return Integer.compare(o.getItems().size(),this.items.size());
    }
}
class CakeShopApplication{

    List<Order> order;

    public CakeShopApplication() {
        order = new ArrayList<>();
    }

    public int readCakeOrders(InputStream in){
        order = new BufferedReader(new InputStreamReader(in))
                .lines()
                .map(Order::create)
                .collect(Collectors.toList());
        int number = order.stream()
                .mapToInt(order -> order.getItems().size())
                .sum();
                return number;
    }
    public void printLongestOrder(PrintStream out){
        PrintWriter pw = new PrintWriter(out);
        Optional<Order> order1 = order.stream().sorted().findFirst();
        if(order!=null){
            pw.println(order1.toString());
        }
        pw.flush();
    }

}
public class CakeShopApplicationTest {
    public static void main(String[] args) throws FileNotFoundException {
        CakeShopApplication cakeShopApplication = new CakeShopApplication();
        File file = new File("C:\\Users\\Martin\\IdeaProjects\\napredno\\src\\cakeShop\\input.txt");
        InputStream inputStream = new FileInputStream(file);
        System.out.println("--- READING FROM INPUT STREAM ---");
        System.out.println(cakeShopApplication.readCakeOrders(inputStream));

        System.out.println("--- PRINTING LARGEST ORDER TO OUTPUT STREAM ---");
        cakeShopApplication.printLongestOrder(System.out);
    }
}
