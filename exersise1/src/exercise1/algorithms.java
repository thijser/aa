package exercise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

//testcomment
public class algorithms {

    static int num_jobs;
    static int processing[]; //Processing times
    static double due[]; //Due times

    // reading a minimum tardiness scheduling problem from a file
    public static void read_problem(String text_file) {
        Scanner s = null;
        try {
            s = new Scanner(new BufferedReader(new FileReader(text_file)));
            if (s.hasNextInt()) {
                num_jobs = s.nextInt();
                processing = new int[num_jobs];
                due = new double[num_jobs];
                int job = 0;

                while (s.hasNextInt() && job < num_jobs) {
                    processing[job] = s.nextInt();
                    due[job] = s.nextInt();
                    job++;
                }
            }
            s.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    // reads a problem, and outputs the result of both greedy and best-first
    public static void main(String args[]) {
        read_problem(args[0]);
        try {
            schedule s = best_first_search.search();
            
            System.out.println((int)s.get_tardiness());
            
            s = ApproximateScheduler.getSchedule(0.2);

            System.out.println((int)s.get_tardiness());
        } catch (Throwable e) {
            // catches out of memory errors
            e.printStackTrace();
        }
    }
}
