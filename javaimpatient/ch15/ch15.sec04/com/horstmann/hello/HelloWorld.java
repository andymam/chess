package com.horstmann.hello;

/*

javac ch15.sec04/module-info.java ch15.sec04/com/horstmann/hello/HelloWorld.java
java -p ch15.sec04 -m ch15.sec04/com.horstmann.hello.HelloWorld

*/


import javax.swing.JOptionPane;

public class HelloWorld {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Hello, Modular World!");
    }
}
