package ch03.sec03;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ButtonDemo {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new JFrame();
            var button = new JButton("Click me!");
            button.addActionListener(new ClickAction());
            frame.add(button);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });       
    }
}

class ClickAction implements ActionListener {
    public void actionPerformed(ActionEvent event) {
        System.out.println("Thanks for clicking!");
    }
}
