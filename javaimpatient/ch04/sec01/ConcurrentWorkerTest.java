package ch04.sec01;

public class ConcurrentWorkerTest {
    public static void main(String[] args) {
        var worker = new ConcurrentWorker();
        worker.work();
        System.out.println("Done");
    }
}
