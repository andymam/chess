package ch04.sec01;

public class ConcurrentWorker extends Worker {
    public void work() { 
        var t = new Thread(super::work);
        t.start();
    }
}