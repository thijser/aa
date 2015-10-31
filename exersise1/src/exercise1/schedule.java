package exercise1;
//
//  greedy.java
//  AdvancedProject
//
//  Created by Sicco on 9/17/09.
//  Copyright 2009 __MyCompanyName__. All rights reserved.
//

// for file reading and writing
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// for the best-first (and breadth-first) search
import java.util.PriorityQueue;
import java.util.Comparator;

// can be useful to create an efficient representation of a schedule
import java.util.BitSet;
// can be usefull for memoization
import java.util.HashMap;

// contains usefull functions
import java.lang.Math;

// The schedule class, represents a (partial) sequence of jobs
class schedule implements Comparable {

	// A linked-list is a reletively efficient representation of a schedule
    // Feel free to modify it if you feel there exists a better one
    // The main advantage is that in a search-tree there is a lot of overlap
    // between schedules, this implementation stores this overlap only once
    public schedule previous;
    public int scheduled_job;

	// tardiness can be calculated instead of memorized
    // however, we need to calculate it a lot, so we momorize it
    // if memory is an issue, however, try calculating it
    public double tardiness;

    @Override
    public String toString() {
        String ret = "";
        ret = ret + "; task: " + scheduled_job + ", tardiness = " + tardiness;
        if (previous != null) {
            ret = ret + previous.toString();
        }
        return ret;
    }

    public schedule() {
        scheduled_job = -1;
        previous = null;
        tardiness = 0;
    }

    // add an additional job to the schedule
    public schedule(schedule s, int job) {
        previous = s;
        scheduled_job = job;
        tardiness = Math.max(0, get_total_time() - algorithms.due[scheduled_job]);
        if (previous != null) {
            tardiness += previous.tardiness;
        }
    }

    public static schedule MakeSchedule(ArrayList<Integer> jobs){
    	schedule s = new schedule();
    	for(Integer i : jobs){
    		s=new schedule(s,i);
    	}
    	return s;
    }
	// used by the best-first search
    // currently, schedules are traversed in smallest total tardiness order
    public int compareTo(Object o) {
        double dif = (get_tardiness()) - (((schedule) o).get_tardiness());
        
        if (dif == 0) {
            return 0;
        }
        
        return dif < 0 ? -1 : 1;

		// replace with the following to get a depth-first search
        // return get_depth() - ((schedule)o).get_depth();
    }

    public int get_depth() {
        if (previous != null) {
            return previous.get_depth() + 1;
        }
        return 1;
    }

    public int get_total_time() {
        if (previous != null) {
            return previous.get_total_time() + algorithms.processing[scheduled_job];
        }
        return algorithms.processing[scheduled_job];
    }

    public double get_tardiness() {
        return tardiness;
    }

    public void recalculateTardiness() {           
        tardiness = Math.max(0, get_total_time() - algorithms.due[scheduled_job]);   
        
        if (previous != null) {
            previous.recalculateTardiness();
            tardiness += previous.tardiness;
        }
    }

    public boolean contains(int job) {
        return (scheduled_job == job) || (previous != null && previous.contains(job));
    }
}

class greedy {
	// returns the earliest deadline first schedule
    // sorting is a little quicker, but then it is more tricky
    // to use this as a subroutine for a search method

    public static schedule greedy() {
        double due = -1;
        int job_to_schedule = -1;
        for (int i = 0; i < algorithms.num_jobs; ++i) {
            if (due == -1 || due > algorithms.due[i]) {
                due = algorithms.due[i];
                job_to_schedule = i;
            }
        }
        return greedy(new schedule(null, job_to_schedule));
    }

    // adds the next earliest deadline first job to the schedule
    public static schedule greedy(schedule s) {
        if (s.get_depth() >= algorithms.num_jobs) {
            return s;
        }

        double due = -1;
        int job_to_schedule = -1;
        for (int i = 0; i < algorithms.num_jobs; ++i) {
            if (s.contains(i) == false && (due == -1 || due > algorithms.due[i])) {
                due = algorithms.due[i];
                job_to_schedule = i;
            }
        }

        s = new schedule(s, job_to_schedule);
        return greedy(s);
    }
}

class best_first_search {
	// returns the best-first (or breadth-first) search schedule
    // It uses a PriorityQueue to store schedules, in every iteration
    // it gets the next best schedule, tries to append all possible jobs
    // and stores the resulting schedules on the queue

    public static schedule search() {
        PriorityQueue<schedule> Q = new PriorityQueue<schedule>();

        for (int i = 0; i < algorithms.num_jobs; ++i) {
            Q.offer(new schedule(null, i));
        }

        schedule best_schedule = null;
        while (Q.peek() != null) {
            schedule s = Q.poll();

			// can be useful for debugging
            // System.err.println(Q.size());
            if (s.get_depth() >= algorithms.num_jobs) {
                if (best_schedule == null || best_schedule.get_tardiness() > s.get_tardiness()) {
                    best_schedule = s;
                }
                continue;
            }

            if (best_schedule != null && best_schedule.get_tardiness() < s.get_tardiness()) {
                continue;
            }

            for (int i = 0; i < algorithms.num_jobs; ++i) {
                if (s.contains(i) == false) {
                    Q.offer(new schedule(s, i));
                }
            }
        }
        return best_schedule;
    }

}
