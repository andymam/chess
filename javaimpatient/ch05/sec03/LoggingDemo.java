package ch05.sec03;

/*
javac ch05/sec03/LoggingDemo.java
java -Djava.util.logging.config.file=logging.properties ch05.sec03.LoggingDemo

 */

import static java.lang.System.Logger.Level.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class LoggingDemo {
    public static void main(String[] args) {
    	System.Logger logger = System.getLogger("com.mycompany.myapp");
    	logger.log(System.Logger.Level.INFO, "Starting program");
    	
    	// Suppress computation of unlogged message 
    	logger.log(INFO, () -> "Arguments " + Arrays.toString(args));
    	
    	// Logging exceptions
    	String filename = "fred.txt";
    	try {    		
    		System.out.println(Files.readString(Path.of(filename)));    		
    	} catch (IOException ex) {
    		logger.log(WARNING, "Cannot open file {0}", filename);
    	}    	
    }
}
