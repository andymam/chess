package ch06.sec03;

import java.io.PrintStream;
import java.util.ArrayList;

public class CloseablesDemo {
    public static void main(String[] args) throws Exception {
        var p1 = new PrintStream("/tmp/1");
        var p2 = new PrintStream("/tmp/2");
        var ps = new ArrayList<PrintStream>();
        ps.add(p1);
        ps.add(p2);
        Closeables.closeAll(ps);        
    }
}