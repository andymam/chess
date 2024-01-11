package ch06.sec06;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class Exceptions {
    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwAs(Throwable e) throws T {
        throw (T) e; // 
    }
    
    public static <V> V doWork(Callable<V> c) {
        try {
            return c.call();
        } catch (Throwable ex) { 
            Exceptions.<RuntimeException>throwAs(ex);
            return null;
        }
    }
    
    public static String readAll(Path path) {
        return doWork(() -> Files.readString(path)); 
    }
    
    public static void main(String[] args) {
        String result = readAll(Path.of("/tmp/quuqux"));
        System.out.println(result);
    }
}
