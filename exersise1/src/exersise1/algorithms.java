package exersise1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

//testcomment
public class algorithms {
	static int num_jobs;
	// size = [num_jobs][2], for every job [0] is the length, [1] is the due time
	static int jobs[][];
	
	// reading a minimum tardiness scheduling problem from a file
	public static void read_problem(String text_file){
		Scanner s = null;
		try {
			s = new Scanner(new BufferedReader(new FileReader(text_file)));
			if(s.hasNextInt()){
				num_jobs = s.nextInt();
				jobs = new int[num_jobs][2];
				int job = 0;
			
				while (s.hasNextInt() && job < num_jobs) {
					jobs[job][0] = s.nextInt();
					jobs[job][1] = s.nextInt();
					job++;
				}
			}
			s.close();
		} catch(Exception e) {
			System.err.println(e);
		}
	}

	// reads a problem, and outputs the result of both greedy and best-first
    public static void main (String args[]) {
		read_problem(args[0]);
		schedule s = greedy.greedy();
		System.out.println(s.get_tardiness());
		
		try {
			s = best_first_search.search();
			System.out.println(s.get_tardiness());
		} catch(Throwable e) {
			// catches out of memory errors
			e.printStackTrace();
		}
	}
}
