package exersise1;

import java.util.HashMap;

/**
 *
 */
public class DynamicSchedule extends schedule {
    private HashMap<Triple, Integer> cache;

    public DynamicSchedule(int[][] jobs) {
        cache = new HashMap();      
        
        getTardiness(0, jobs.length, jobs, 0);
    }

    private int getTardiness(int start, int end, int[][] jobs, int time) {
        Triple triple = new Triple(start, end, time);
        if (cache.containsKey(triple)) {
            return cache.get(triple);
        }
        
        if (start == end) {
            return Math.max(0, jobs[start][1] - (jobs[start][0] + time));
        }

        int k = 0;

        for (int i = start + 1; i <= end; i++) {
            if (jobs[i][0] > jobs[k][0]) {
                k = i;
            }
        }

        int min = Integer.MAX_VALUE;

        for (int delta = 0; delta <= (end - start) - k; delta++) {
            int completion = time + getProcessingTime(start, start + k + delta, jobs);

            int tardiness = getTardiness(start, start + k + delta, jobs, time) + getTardiness(start + k + delta + 1, end, jobs, completion);

            if (tardiness < min) {
                min = tardiness;
            }
        }

        return min;
    }
    
    private int getProcessingTime(int start, int end, int[][] jobs) {
        int t = 0;

        for (int i = start; i <= end; i++) {
            t += jobs[i][0];
        }
        
        return t;
    }
    
    class Triple {
        private int start,end,time;

        public Triple(int start, int end, int time) {
            this.start = start;
            this.end = end;
            this.time = time;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 69 * hash + this.start;
            hash = 51 * hash + this.end;
            hash = 71 * hash + this.time;
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
            final Triple other = (Triple) obj;
            if (this.start != other.start) {
                return false;
            }
            if (this.end != other.end) {
                return false;
            }
            if (this.time != other.time) {
                return false;
            }
            return true;
        }
        
        
    }
}
