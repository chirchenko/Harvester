package gui;

import java.awt.HeadlessException;

import javax.swing.JFrame;

import calculator.App;

public class ConsoleFrame extends JFrame {
	private JAConsole console;

	public ConsoleFrame(JAConsole console) throws HeadlessException {
		super("Console");
		this.console = console;
		setSize(App.dim.width*2/4, App.dim.height*2/4);
		setLocationByPlatform(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
	}
	
	public void display(){
		
		this.add(this.console.outputScroll);
		repaint();
		setVisible(true);
	}
}
