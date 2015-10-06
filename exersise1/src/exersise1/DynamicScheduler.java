package exersise1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Returns the schedule with the lowest possible tardiness (Computed
 * dynamically)
 *
 *
 */
public class DynamicScheduler {

    private HashMap<ArrayList<Integer>, Integer> cache;
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
                return algorithms.jobs[t][1] - algorithms.jobs[t1][1];
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
        
        ArrayList<Integer> beforeMax = new ArrayList();
        beforeMax.addAll(jobs.subList(0, indexMax));
        beforeMax.addAll(jobs.subList(indexMax + 1, indexMax + delta + 1));

        ArrayList<Integer> afterMax = new ArrayList();
        afterMax.addAll(jobs.subList(indexMax + delta + 1, jobs.size()));
        
        int completion = time + getProcessingTime(beforeMax) + algorithms.jobs[jobs.get(indexMax)][0];
        
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

    private int getTardiness(ArrayList<Integer> jobs, int time) {
        if (cache.containsKey(jobs)) {
            return cache.get(jobs);
        }

        if (jobs.isEmpty()) {
            return 0;
        }

        int indexMax = getIndexMax(jobs);

        int delta = getDelta(jobs, time, indexMax);

        int tardiness = getTardiness(jobs, time, indexMax, delta);
        
        cache.put(jobs, tardiness);

        return tardiness;
    }

    private int getIndexMax(ArrayList<Integer> jobs) {
        int indexMax = 0;

        for (int i = 1; i < jobs.size(); i++) {
            if (algorithms.jobs[jobs.get(i)][0] > algorithms.jobs[jobs.get(indexMax)][0]) {
                indexMax = i;
            }
        }

        return indexMax;
    }

    private int getDelta(ArrayList<Integer> jobs, int time, int indexMax) {
        int min = Integer.MAX_VALUE;
        int bestDelta = -1;

        for (int delta = 0; delta < jobs.size() - indexMax; delta++) {
            int tardiness = getTardiness(jobs, time, indexMax, delta);

            if (tardiness < min) {
                min = tardiness;
                bestDelta = delta;
            }
        }

        return bestDelta;
    }

    private int getTardiness(ArrayList<Integer> jobs, int time, int indexMax, int delta) {
        ArrayList<Integer> beforeMax = new ArrayList();
        beforeMax.addAll(jobs.subList(0, indexMax));
        beforeMax.addAll(jobs.subList(indexMax + 1, indexMax + delta + 1));

        ArrayList<Integer> afterMax = new ArrayList();
        afterMax.addAll(jobs.subList(indexMax + delta + 1, jobs.size()));

        int completion = time + getProcessingTime(beforeMax) + algorithms.jobs[jobs.get(indexMax)][0];

        int tardiness = Math.max(0, completion - algorithms.jobs[jobs.get(indexMax)][1]) + getTardiness(beforeMax, time) + getTardiness(afterMax, completion);

        return tardiness;
    }

    private int getProcessingTime(ArrayList<Integer> jobs) {
        int t = 0;

        for (int job : jobs) {
            t += algorithms.jobs[job][0];
        }

        return t;
    }

}
