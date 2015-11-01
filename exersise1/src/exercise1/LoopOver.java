package exercise1;

import java.io.File;

public class LoopOver {

    static int timeout = 180000;
    static double epsilon = 1;

    public static String location = "C:\\Users\\Jorik\\Documents\\NetBeansProjects\\Advanced Algorithms\\tests";

    public static void main(String args[]) throws InterruptedException {

        File dir = new File(location);
        File[] directoryListing = dir.listFiles();
        for (File f : directoryListing) {
            System.out.println();
            System.out.print("RDD=;" + f.getName().substring(11, 14));
            System.out.print(";TF=;" + f.getName().substring(18, 21));
            algorithms.read_problem(f.getPath());
            System.out.print(";size=;" + algorithms.num_jobs);

            Thread t = new Thread(new executeAprox());
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
            t.join(timeout);
            if (t.isAlive()) {
                t.stop();
                t.join();
                System.out.print("interupted;;;interupted;");
            }
        }
    }

}

class executeAprox implements Runnable {

    @Override
    public void run() {
        long time = System.currentTimeMillis();
        schedule s = ApproximateScheduler.getSchedule(LoopOver.epsilon);
        System.out.print(";duration=;" + (System.currentTimeMillis() - time));
        System.out.println(";tardiness=;" + s.get_tardiness());
    }

}
