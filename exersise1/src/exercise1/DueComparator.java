package exercise1;

import java.util.Comparator;

/**
 * Compares jobs due times (for sorting to EDD order)
 */
public class DueComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer t, Integer t1) {
        double dif = algorithms.due[t] - algorithms.due[t1];
        if (dif == 0) {
            return 0;
        }
        return dif < 0 ? -1 : 1;
    }

}
