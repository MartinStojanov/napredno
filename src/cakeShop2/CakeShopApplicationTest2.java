package cakeShop2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum TypeCake{
    cake, pie
}
class InvalidOrderException extends Exception{
    public InvalidOrderException(long num) {
        super(String.format("The order with id %d has less items than the minimum allowed",num));
    }
}
abstract class Item{
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

    abstract TypeCake getTypeCake();

}
class Cake extends Item{

    public Cake(String name) {
        super(name);
    }

    @Override
    TypeCake getTypeCake() {
        return TypeCake.cake;
    }
}
class Pie extends Item{

    public Pie(String name) {
        super(name);
    }

    @Override
    TypeCake getTypeCake() {
        return TypeCake.pie;
    }

    @Override
    public int getPrice() {
        return super.getPrice() + 50;
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

    public static Order create(String line, int min) throws InvalidOrderException {
        String[] parts = line.split("\\s+");
        long id = Long.parseLong(parts[0]);
        List<Item> items1 = new ArrayList<>();
        Arrays.stream(parts)
                .skip(1)
                .forEach(i -> {
                    if(Character.isDigit(i.charAt(0))){
                        items1.get(items1.size()-1).setPrice(Integer.parseInt(i));
                    }else{
                        if(i.charAt(0)=='C')
                            items1.add(new Cake(i));
                        else items1.add(new Pie(i));

                    }
                });
        if(items1.size()< min){
            throw new InvalidOrderException(id);
        }
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
                "totalPies" + totalPies() +
                "totalCakes" + totalCakes() +
                "totalAmount" + calculatePrice() +
                '}';
    }
    public int calculatePrice(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }
    public int totalPies(){
        return (int) items.stream().filter(i->i.getTypeCake().equals(TypeCake.pie)).count();
    }
    public int totalCakes(){
        return (int) items.stream().filter(i->i.getTypeCake().equals(TypeCake.cake)).count();
    }
    @Override
    public int compareTo(Order o) {
        return Integer.compare(o.calculatePrice(),this.calculatePrice());
    }
}
class CakeShopApplication{

    List<Order> order;
    int minNum;

    public CakeShopApplication(int minNum) {
        this.minNum = minNum;
    }

    public CakeShopApplication() {
        order = new ArrayList<>();
    }

    public void readCakeOrders(InputStream in){
        order = new BufferedReader(new InputStreamReader(in))
                .lines()
                .map(line -> {
                    try {
                        return Order.create(line,minNum);
                    } catch (InvalidOrderException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                })
                .collect(Collectors.toList());
        order=order.stream().filter(Objects::nonNull).collect(Collectors.toList());

    }
    public void printLongestOrder(PrintStream out){
        PrintWriter pw = new PrintWriter(out);
        Optional<Order> order1 = order.stream().sorted().findFirst();
        if(order!=null){
            pw.println(order1.toString());
        }
        pw.flush();
    }
    public void printAllOrders(PrintStream out){
        order = order.stream().sorted().collect(Collectors.toList());
        PrintWriter pw = new PrintWriter(out);
        order.stream().forEach(i-> pw.println(i));
        pw.flush();
    }

}
public class CakeShopApplicationTest2 {
    public static void main(String[] args) throws FileNotFoundException {
        CakeShopApplication cakeShopApplication = new CakeShopApplication(4);
        File file = new File("C:\\Users\\Martin\\IdeaProjects\\napredno\\src\\cakeShop\\input.txt");
        FileInputStream input = new FileInputStream(file);
        System.out.println("--- READING FROM INPUT STREAM ---");
        cakeShopApplication.readCakeOrders(input);

        System.out.println("--- PRINTING TO OUTPUT STREAM ---");
        cakeShopApplication.printAllOrders(System.out);
    }
}