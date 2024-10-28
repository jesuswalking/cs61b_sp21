package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    /* timeHelper helps record the data for each tests given the NUMBER. */
    private static void timeHelper(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts, int number) {
        AList<Integer> testAlist = new AList<>();
        Stopwatch stopwatch = new Stopwatch();
        for (int i = 0; i < number; i += 1) {
            testAlist.addLast(i);
        }
        double timeInSeconds = stopwatch.elapsedTime();
        Ns.addLast(number);
        times.addLast(timeInSeconds);
        opCounts.addLast(number);  
    }

    /* Keeping addlast, until it reaches the certain number. */
    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        timeHelper(Ns, times, opCounts, 1000);
        timeHelper(Ns, times, opCounts, 2000);
        timeHelper(Ns, times, opCounts, 4000);
        timeHelper(Ns, times, opCounts, 8000);
        timeHelper(Ns, times, opCounts, 16000);
        timeHelper(Ns, times, opCounts, 32000);
        timeHelper(Ns, times, opCounts, 64000);
        timeHelper(Ns, times, opCounts, 128000);
        printTimingTable(Ns, times, opCounts);
    }
}
