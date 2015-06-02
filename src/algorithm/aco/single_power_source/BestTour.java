package algorithm.aco.single_power_source;

import javax.xml.crypto.dom.DOMCryptoContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seal on 5/30/15.
 */
public class BestTour {
    private List<Double> powers;
    private List<List<Integer>> bestTour;
    private List<List<Integer>> allTours;
    BestTour() {
        this.powers = new ArrayList<>();
        this.bestTour = new ArrayList<>();
        this.allTours = new ArrayList<>();
    }

    public void addTour(final int index, List<Integer> tour) {
        allTours.add(tour);
        if (tour.size() > bestTour.get(index).size()) {
            bestTour.set(index, tour);
        }
    }

    public void setPowers(final List<Double> powers) {
        this.powers = powers;
    }

    public void printTour() {
        for (List<Integer> i : bestTour) {
            for (int j : i) {
                System.out.println(j);
            }
        }
    }
}
