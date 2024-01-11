package ch10.sec02;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class WebReader {
	public static final int TEXT_ROWS = 20;
	public static final int TEXT_COLUMNS = 60;
	
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var frame = new JFrame();
            var textArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
            frame.add(new JScrollPane(textArea));
	        ExecutorService executor = Executors.newCachedThreadPool();
	        var buttonPanel = new JPanel();
	        var readButton = new JButton("Read");
	        buttonPanel.add(readButton);
	        frame.add(buttonPanel, BorderLayout.SOUTH);
	        String url = "https://horstmann.com/index.html";
            Runnable task = () -> {
                try {
                    try (var in = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8)) {
                        while (in.hasNextLine()) {
                            EventQueue.invokeLater(() -> 
                                textArea.append(in.nextLine() + "\n"));
                            Thread.sleep(100);
                        }
                    }               
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };	        
	        readButton.addActionListener(event -> {
	        	textArea.setText("");
	            executor.submit(task);	        	
	        });	        
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.pack();
	        frame.setVisible(true);
	     });
    }
	
	
    

}