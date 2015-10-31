package exercise1;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sun.corba.se.impl.orbutil.closure.Future;

public class LoopOver {
	
	static int timeout=100000;
	static double epsilon;
	
	public static String location="/home/thijs/Desktop/code/aa/tests";
    public static void main(String args[]) throws InterruptedException {
    	
    	  File dir = new File(location);
    	  File[] directoryListing = dir.listFiles();
    	  for(File f : directoryListing){
    		  System.out.print("RDD="+f.getName().substring(10, 13));
    		  System.out.print(";TF="+f.getName().substring(17, 20));
    		 algorithms.read_problem(f.getPath());
    		  System.out.println();
    		  Thread t = new Thread(new executeAprox());
    		  t.start();
    		  long start=System.currentTimeMillis();
    		  while(t.isAlive()&&start-System.currentTimeMillis()<timeout){
    			  Thread.sleep(1000);
    		  }
    		  if(t.isAlive()){
    			  t.interrupt();
    			  System.out.print("interupted;interupted;");
    		  }
    	  }
    }
    
    
    
    
}   

class executeAprox implements Runnable{

	@Override
	public void run() {
		long time = System.currentTimeMillis();
		schedule s = ApproximateScheduler.getSchedule(LoopOver.epsilon);
		System.out.print(";duration="+(System.currentTimeMillis()-time));
		System.out.println(";tardiness="+s.get_tardiness());
	}
	
}