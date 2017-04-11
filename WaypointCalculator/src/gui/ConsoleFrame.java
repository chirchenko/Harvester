package gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import calculator.App;

@SuppressWarnings("serial")
public class ConsoleFrame extends JFrame {
	private JAConsole console;

	public ConsoleFrame(JAConsole console) throws HeadlessException {
		super("Console");
		this.console = console;
		setSize(App.dim.width*2/4, App.dim.height*2/4);
		setLocationByPlatform(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLayout(new BorderLayout());	
	}
	
	public void display(){
		this.getContentPane().removeAll();
		
		this.add(new JScrollPane(new JTextArea(console.output.getText())), BorderLayout.CENTER);

		setVisible(true);
	}
}
