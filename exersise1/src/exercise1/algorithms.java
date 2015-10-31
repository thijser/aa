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
        double epsilon = Integer.parseInt(args[0]);
        read_problem(args[1]);
        
        schedule optimal = (new DynamicScheduler()).getSchedule();
        schedule approx = ApproximateScheduler.getSchedule(epsilon);
        System.out.println((int)optimal.get_tardiness() + " " + (int)approx.get_tardiness());
    }
}
