package logginig;

import java.awt.EventQueue;

import gui.ConsolePanel;

public class GuiConsoleLogger extends LogListenerImpl {
	private final ConsolePanel console;
	
	public GuiConsoleLogger(ConsolePanel jaConsole) {
		this.console = jaConsole;
	}

	@Override
	protected void appendText(String messaage) {
		if (EventQueue.isDispatchThread()) {
			console.output.append(messaage);
			console.output.setCaretPosition(console.output.getText().length());
		} else {
		
		    EventQueue.invokeLater(() -> appendText(messaage));
		
}
		
	}
}
