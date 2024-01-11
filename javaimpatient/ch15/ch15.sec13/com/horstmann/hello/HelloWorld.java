package com.horstmann.hello;

/*

Compile the service module: 

javac com.horstmann.greetsvc/module-info.java \
   com.horstmann.greetsvc/com/horstmann/greetsvc/GreeterService.java \
   com.horstmann.greetsvc/com/horstmann/greetsvc/internal/*.java
   
Compile and run the program:
   
javac -p com.horstmann.greetsvc \
   ch15.sec13/com/horstmann/hello/HelloWorld.java \
   ch15.sec13/module-info.java
java -p com.horstmann.greetsvc:ch15.sec13 \
   -m ch15.sec13/com.horstmann.hello.HelloWorld de    

 */

import com.horstmann.greetsvc.GreeterService;
import java.util.Locale;
import java.util.ServiceLoader;

public class HelloWorld {
    public static void main(String[] args) {
        ServiceLoader<GreeterService> greeterLoader
            = ServiceLoader.load(GreeterService.class);
        GreeterService chosenGreeter = null;
        for (GreeterService greeter : greeterLoader) {
            if (args.length > 0 &&
                  greeter.getLocale().getLanguage().equals(args[0]))
               chosenGreeter = greeter;
        }
        if (chosenGreeter == null)
           System.out.println("No suitable greeter. Try with arg de or fr");
        else
           System.out.println(chosenGreeter.greet("Modular World"));
    }
}
