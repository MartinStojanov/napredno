package av9;

import java.util.*;
import java.util.stream.IntStream;

class Trial {

    private int numPeople;
    private static Random RANDOM = new Random();
    public Trial(int numPeople) {
        this.numPeople = numPeople;
    }

    public boolean run(){
        Set<Integer> birthdaus = new HashSet<>();
        for(int i=0;i<numPeople;i++){
            int birthaday = RANDOM.nextInt(365)+1;
            if(birthdaus.contains(birthaday)){
                return true;
            }else {
                birthdaus.add(birthaday);
            }

        }
        return false;
    }
}
class Experiment{
    private static int TRIALS = 5000;
    int people;

    public Experiment(int people) {
        this.people = people;
    }

    public double run(){
        int count = (int) IntStream.range(0,TRIALS)
                .mapToObj(i -> new Trial(people))
                .map(Trial::run)
                .filter(b->b)
                .count();
        return count * 1.0 / TRIALS;
    }
}

public class BirthdayParadoxTest {

    private Map<Integer,Double> probabilities;
    private int maxPeople;
    public BirthdayParadoxTest(int maxPeople) {
        this.probabilities = new TreeMap<>();
        this.maxPeople=maxPeople;

    }
    public void startExperiment(){

        for(int i=2;i<=maxPeople;i++){
            Experiment experiment =  new Experiment(i);
            double probability = experiment.run();
            probabilities.put(i,probability);
        }
       probabilities.forEach((key,value) ->
               System.out.println(String.format("For %d people, the probability is about %.5f",key,value)));
    }

    public static void main(String[] args) {
            BirthdayParadoxTest test =  new BirthdayParadoxTest(50);
            test.startExperiment();
    }
}
