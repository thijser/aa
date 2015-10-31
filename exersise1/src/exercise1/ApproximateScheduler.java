package exercise1;

import java.util.ArrayList;
import java.util.Collections;

/**
 *Returns a approximately optimal schedule
 */
public class ApproximateScheduler {
	public static schedule getSchedule(double epsilon) {
		
    	ArrayList<Integer> jobs = new ArrayList<Integer>();
    	for(int i=0;i<algorithms.num_jobs;i++){
    		jobs.set(i, i);
    	}
    	Collections.sort(jobs,new DueComparator());
    	double highestTardiness=getMaxTardiness(jobs);
    	if(highestTardiness<0.0001) //avoid rounding errors
    		return schedule.MakeSchedule(jobs);
    	double K = getK(jobs.size(),getMaxTardiness(jobs),epsilon);
    	double[] due=algorithms.due;
    	int[] proccesing = algorithms.processing;
    	int[] scaledProponent=getProcesingScaled(proccesing,K);
    	double[] scaledDue=getDueScaled(due,K);
    	
    	algorithms.due=scaledDue;
    	algorithms.processing=scaledProponent;
    	schedule s = (new DynamicScheduler()).getSchedule();
    	algorithms.due = due;
    	algorithms.processing = proccesing;
    	s.recalculateTardiness();
    	return s;
    }
    public static int[] getProcesingScaled(int[] unscaled,double K){
    	int[] scaled = new int[unscaled.length];
    	for(int i=0;i <unscaled.length;i++){
    		scaled[i]=(int)((double)unscaled[i]/K);
    	}
    	return scaled;
    }
	public static double[] getDueScaled(double[] unscaled,double K){
		double[] scaled = new double[unscaled.length];
    	for(int i=0;i <unscaled.length;i++){
    		scaled[i]=unscaled[i]/K;
    	}
		return scaled;
	}
	
	public static double getK(int n, double tmax, double epsilon){
		return tmax*((2*epsilon)/(n*(n+1)));
	}
	public static double getMaxTardiness(ArrayList<Integer> jobs){
		int time=0;
		double highestTardiness=0;
		for(int i=0;i<jobs.size();i++){
			time=time+algorithms.processing[jobs.get(i)];
			highestTardiness=Math.max(highestTardiness,time-algorithms.due[jobs.get(i)]);
		}
		return highestTardiness;
	}
}

