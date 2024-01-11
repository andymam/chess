package com.horstmann.hello;

/*
 
javac ch15.sec03/module-info.java ch15.sec03/com/horstmann/hello/HelloWorld.java
java -p ch15.sec03 -m ch15.sec03/com.horstmann.hello.HelloWorld

*/

public class HelloWorld {
    public static void main(String[] args) {
       System.out.println("Hello, Modular World!");
    }
}
