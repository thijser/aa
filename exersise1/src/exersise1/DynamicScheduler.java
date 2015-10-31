package exersise1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

/**
 * Returns the schedule with the lowest possible tardiness (Computed
 * dynamically)
 *
 */
public class DynamicScheduler {

    private HashMap<Pair, Double> cache;
    private ArrayList<Integer> jobs;

    public DynamicScheduler() {
        cache = new HashMap();

        jobs = new ArrayList(algorithms.num_jobs);
        
        for (int i = 0;i < algorithms.num_jobs;i++) {
            jobs.add(i);
        }

        Collections.sort(jobs, new Comparator<Integer>() {
            @Override
            public int compare(Integer t, Integer t1) {
                double dif = algorithms.due[t] - algorithms.due[t1];
                if (dif == 0) {
                    return 0;
                } 
                return dif < 0 ? -1 : 1;
            }
        });
    }

    public schedule getSchedule() {
        return getSchedule(jobs, 0);
    }
    
    private schedule getSchedule(ArrayList<Integer> jobs, int time) {
        if(jobs.isEmpty()) {
            return new schedule();
        }
        
        int indexMax = getIndexMax(jobs);
        
        int delta = getDelta(jobs, time, indexMax);        
        
        ArrayList<Integer> beforeMax = getBefore(jobs, indexMax, delta);

        ArrayList<Integer> afterMax = getAfter(jobs, indexMax, delta);
        
        int completion = time + getProcessingTime(beforeMax) + algorithms.processing[jobs.get(indexMax)];
        
        schedule first = getSchedule(beforeMax, time);
        if (first.scheduled_job == -1) {
            first = null;
        } 
        
        first = new schedule(first, jobs.get(indexMax));
        
        schedule second = getSchedule(afterMax, completion);
        
        if (second.scheduled_job == -1) {
            return first;
        }
        
        schedule prev = second;
        
        while (prev.previous != null) {
            prev = prev.previous;
        }
        
        prev.previous = first;
        
        second.recalculateTardiness();
        
        return second;
    }

    private double getTardiness(ArrayList<Integer> jobs, int time) {
        Pair key = new Pair(jobs, time);
        
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if (jobs.isEmpty()) {
            return 0;
        }

        int indexMax = getIndexMax(jobs);

        int delta = getDelta(jobs, time, indexMax);

        double tardiness = getTardiness(jobs, time, indexMax, delta);
        
        cache.put(key, tardiness);

        return tardiness;
    }

    private int getIndexMax(ArrayList<Integer> jobs) {
        int indexMax = 0;

        for (int i = 1; i < jobs.size(); i++) {
            if (algorithms.processing[jobs.get(i)] > algorithms.processing[jobs.get(indexMax)]) {
                indexMax = i;
            }
        }

        return indexMax;
    }

    private int getDelta(ArrayList<Integer> jobs, int time, int indexMax) {
        double min = Double.MAX_VALUE;
        int bestDelta = -1;

        for (int delta = 0; delta < jobs.size() - indexMax; delta++) {
            double tardiness = getTardiness(jobs, time, indexMax, delta);

            if (tardiness < min) {
                min = tardiness;
                bestDelta = delta;
            }
        }

        return bestDelta;
    }

    private double getTardiness(ArrayList<Integer> jobs, int time, int indexMax, int delta) {
        ArrayList<Integer> beforeMax = getBefore(jobs, indexMax, delta);

        ArrayList<Integer> afterMax = getAfter(jobs, indexMax, delta);

        int completion = time + getProcessingTime(beforeMax) + algorithms.processing[jobs.get(indexMax)];

        double tardiness = Math.max(0, completion - algorithms.due[jobs.get(indexMax)]) + getTardiness(beforeMax, time) + getTardiness(afterMax, completion);

        return tardiness;
    }
    
    private ArrayList<Integer> getBefore(ArrayList<Integer> jobs, int indexMax, int delta) {
        ArrayList<Integer> beforeMax = new ArrayList();
        beforeMax.addAll(jobs.subList(0, indexMax));
        beforeMax.addAll(jobs.subList(indexMax + 1, indexMax + delta + 1));
        
        return beforeMax;        
    }
    
    private ArrayList<Integer> getAfter(ArrayList<Integer> jobs, int indexMax, int delta) {        
        ArrayList<Integer> afterMax = new ArrayList();
        afterMax.addAll(jobs.subList(indexMax + delta + 1, jobs.size()));
        
        return afterMax;
    }

    private int getProcessingTime(ArrayList<Integer> jobs) {
        int t = 0;

        for (int job : jobs) {
            t += algorithms.processing[job];
        }

        return t;
    }

    
    private class Pair {
        private ArrayList<Integer> jobs;
        private int time;

        private Pair(ArrayList<Integer> jobs, int time) {
            this.jobs = jobs;
            this.time = time;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.jobs);
            hash = 97 * hash + this.time;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Pair other = (Pair) obj;
            if (!Objects.equals(this.jobs, other.jobs)) {
                return false;
            }
            if (this.time != other.time) {
                return false;
            }
            return true;
        }
        
        
    }
}
