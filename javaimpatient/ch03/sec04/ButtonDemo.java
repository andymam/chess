package ch03.sec04;

import java.awt.*;
import javax.swing.*;

public class ButtonDemo {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new JFrame();
            var button = new JButton("Click me!");
            button.addActionListener(event -> 
                System.out.println("Thanks for clicking!"));
            frame.add(button);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });       
    }
}
