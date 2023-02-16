package av10;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}
class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String message) {
        super(message);
    }
}
class Product{
    private String category;
    private String id;
    private String name;
    private LocalDateTime date;
    private double price;

    int qunatitySold;

    public Product(String category, String id, String name, LocalDateTime date, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getQunatitySold() {
        return qunatitySold;
    }

    public double getPrice() {
        return price;
    }

    public void setQunatitySold(int qunatitySold) {
        this.qunatitySold = qunatitySold;
    }
    public double buy(int quantity){
        qunatitySold+=quantity;
        return price*quantity;
    }
}
class OnlineShop{

    List<Product> products;
    Map<String,Product> productsById;
    Map<String,Integer> numberSoldById;

    Map<String,List<Product>> productsByCategory;
    public OnlineShop() {
        products = new ArrayList<>();
        productsById = new HashMap<>();
        numberSoldById = new HashMap<>();
        productsByCategory = new HashMap<>();
    }

    public void addProduct(String category, String id, String name, LocalDateTime createdAt, double price) {
        Product product = new Product(category,id,name,createdAt,price);
        productsById.putIfAbsent(id,product);
        productsByCategory.putIfAbsent(category,new ArrayList<>());
        productsByCategory.get(category).add(product);
    }

    public double buyProduct(String id, int quantity) throws ProductNotFoundException {
        if(!productsById.containsKey(id)) {
            throw new ProductNotFoundException(id);
        }
        //my way
//        numberSoldById.putIfAbsent(id,0);
//        numberSoldById.computeIfPresent(id,(k,v) -> v+quantity);
//        return productsById.get(id).getPrice()*quantity;
        return productsById.get(id).buy(quantity);
    }

    private Comparator<Product> createComparator(COMPARATOR_TYPE comparatorType){
        Comparator<Product> oldestComparator = Comparator.comparing(Product::getDate);
        Comparator<Product> leastSoldComparator = Comparator.comparing(Product::getQunatitySold);
        Comparator<Product> lowestPriceComparator = Comparator.comparing(Product::getPrice);

        if(comparatorType.equals(COMPARATOR_TYPE.NEWEST_FIRST)){
            return oldestComparator.reversed();
        }else if(comparatorType.equals(COMPARATOR_TYPE.OLDEST_FIRST)){
            return oldestComparator;
        }else if(comparatorType.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST)){
            return leastSoldComparator.reversed();
        }else if(comparatorType.equals(COMPARATOR_TYPE.LEAST_SOLD_FIRST)){
            return leastSoldComparator;
        }else if(comparatorType.equals(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST)){
            return lowestPriceComparator.reversed();
        }else {
            return lowestPriceComparator;
        }
    }
    public List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<Product> relevantProducts;
        if(category==null){
                relevantProducts = new ArrayList<>(productsById.values());
            }else{
            relevantProducts = productsByCategory.get(category);
        }
        relevantProducts = relevantProducts.stream().sorted(createComparator(comparatorType))
                .collect(Collectors.toList());
        int pages = (int)Math.ceil((double)relevantProducts.size()/5);
        List<Integer> starts =
        IntStream.range(0,pages).map(i->i*pageSize).boxed().collect(Collectors.toList());
        List<List<Product>> result = new ArrayList<>();
        for(int start : starts){
            int end = Math.min(start+pageSize,relevantProducts.size());
            result.add(relevantProducts.subList(start,end));
        }
    return result;
    }
}



public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category = null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}
