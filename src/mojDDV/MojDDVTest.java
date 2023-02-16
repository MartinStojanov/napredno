package mojDDV;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

enum TaxType{
    A, B, V
}
class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int amount) {
        super(String.format("Receipt with amount %d is not allowed to be scanned",amount));
    }
}
class Item{
    private int price;
    private TaxType tax;

    public Item(int price, TaxType tax) {
        this.price = price;
        this.tax = tax;
    }

    public Item(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public TaxType getTax() {
        return tax;
    }

    public void setTax(TaxType tax) {
        this.tax = tax;
    }

    public double detCalculatedTax(){
        if(tax.equals(TaxType.A)) return 0.18 * price;
        else if (tax.equals(TaxType.B)) return 0.05*price;
        else return 0;
    }

}
class Receipt implements Comparable<Receipt>{
    private long id;
    private List<Item> items;

    public Receipt(long id, List<Item> items) {
        this.id = id;
        this.items = items;
    }

    public Receipt(long id) {
        this.id = id;
        this.items =  new ArrayList<>();
    }
    public static Receipt create(String line) throws AmountNotAllowedException{
        String [] parts = line.split("\\s+");
        long id = Long.parseLong(parts[0]);
        List<Item> items1 = new ArrayList<>();
        Arrays.stream(parts)
                .skip(1)
                .forEach(i->{
                    if(Character.isDigit(i.charAt(0))){
                        items1.add(new Item(Integer.parseInt(i)));
                    }else {
                        items1.get(items1.size()-1).setTax(TaxType.valueOf(i));
                    }
                });
        if(totalAmount(items1)>30000)
            throw new AmountNotAllowedException(totalAmount(items1));
        return new Receipt(id,items1);

    }
    public static int totalAmount(List<Item> items){
        return items.stream().mapToInt(Item::getPrice).sum();
    }
    public int totalAmount(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }

    public double taxReturns(){
        return items.stream().mapToDouble(Item::detCalculatedTax).sum();
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("\t%10d%10d\t%5.5f", id, totalAmount(), taxReturns());
    }


    @Override
    public int compareTo(Receipt o) {
        return Comparator.comparing(Receipt::taxReturns)
                .thenComparing(Receipt::totalAmount)
                .compare(this, o);
    }
}
class MojDDV{

    private List<Receipt> receipts;

    public MojDDV() {
        receipts =  new ArrayList<>();
    }

    public void readRecords(InputStream in) {

        receipts = new BufferedReader(new InputStreamReader(in))
                .lines()
                .map(i-> {
                    try {
                        return Receipt.create(i);
                    } catch (AmountNotAllowedException e) {
                        System.out.println(e.getMessage());
                        return null;
                    }
                }).collect(Collectors.toList());
        receipts = receipts.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void printSorted(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        receipts.stream().sorted().forEach(i->out.println(i.toString()));
        printWriter.flush();

    }

    public void printStatistics(PrintStream out) {
        PrintWriter printWriter = new PrintWriter(out);
        DoubleSummaryStatistics doubleSummaryStatistics =
                receipts.stream().mapToDouble(Receipt::taxReturns).summaryStatistics();
        printWriter.println(String
                .format("min:\t%05.03f\nmax:\t%05.03f\nsum:\t%05.03f\ncount:\t%-5d\navg:\t%05.03f\n",
                        doubleSummaryStatistics.getMin(),
                        doubleSummaryStatistics.getMax(),
                        doubleSummaryStatistics.getSum(),
                        doubleSummaryStatistics.getCount(),
                        doubleSummaryStatistics.getAverage()));
        printWriter.flush();
    }
}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printSorted(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);

    }
}