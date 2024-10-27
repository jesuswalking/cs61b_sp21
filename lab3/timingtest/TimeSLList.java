package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
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
        timeGetLast();
    }

    /* timeHelper helps record the data for each tests given the NUMBER. */
    private static void timeHelper(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts, int number) {
        SLList<Integer> testSLList = new SLList<>();
        for (int i = 0; i < number; i += 1) {
            testSLList.addLast(i);
        }

        Stopwatch stopwatch = new Stopwatch();
        for (int j = 0; j < 10000; j += 1) {
            testSLList.getLast();
        }
        double timeInSeconds = stopwatch.elapsedTime();

        Ns.addLast(number);
        times.addLast(timeInSeconds);
        opCounts.addLast(10000);  
    }

    public static void timeGetLast() {
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
