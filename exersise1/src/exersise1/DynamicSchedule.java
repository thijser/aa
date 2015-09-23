package exersise1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class DynamicSchedule extends schedule {
    private HashMap<ArrayList<int[]>, Integer> cache;

    public DynamicSchedule(int[][] jobs) {
        cache = new HashMap();      
        
        ArrayList<int[]> input = new ArrayList(Arrays.asList(jobs));
        
        Collections.sort(input, new Comparator<int[]>() {
            @Override
            public int compare(int[] t, int[] t1) {
                return t[1] - t1[1];
            }            
        });
        
        System.out.println(getTardiness(input, 0));
    }

    private int getTardiness(ArrayList<int[]> jobs, int time) {
        if (cache.containsKey(jobs)) {
            return cache.get(jobs);
        }
        
        if (jobs.size() == 1) {
            int[] job = jobs.get(0);
            return Math.max(0, job[1] - (job[0] + time));
        } else if (jobs.size() == 0) {
            return 0;
        }

        int indexMax = 0;

        for (int i = 1; i < jobs.size(); i++) {
            if (jobs.get(i)[0] > jobs.get(indexMax)[0]) {
                indexMax = i;
            }
        }

        int min = Integer.MAX_VALUE;

        for (int delta = 0; delta < jobs.size() - indexMax; delta++) {
            ArrayList<int[]> beforeMax = new ArrayList();
            beforeMax.addAll(jobs.subList(0, indexMax));
            beforeMax.addAll(jobs.subList(indexMax+1, indexMax + delta + 1));
            
            ArrayList<int[]> afterMax = new ArrayList();
            afterMax.addAll(jobs.subList(indexMax + delta + 1, jobs.size()));
                        
            int completion = time + getProcessingTime(beforeMax) + jobs.get(indexMax)[0];
            
            int tardiness = Math.max(0, jobs.get(indexMax)[1] - (jobs.get(indexMax)[0] + time)) + getTardiness(beforeMax, time) + getTardiness(afterMax, completion);
            
            if (tardiness < min) {
                min = tardiness;
            }
        }
        
        System.out.println("Time = " + time + " , Tardiness = " + min);

        return min;
    }
    
    private int getProcessingTime(List<int[]> jobs) {
        int t = 0;

        for (int[] job : jobs) {
            t += job[0];
        }
        
        return t;
    }
}
