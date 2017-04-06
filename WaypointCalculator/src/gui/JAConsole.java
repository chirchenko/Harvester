package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import logginig.LogListener;
import logginig.Logger;
import logginig.Logger.LogLevel;

@SuppressWarnings("serial")
public class JAConsole extends JPanel implements LogListener {
        private JTextArea output;

        public JAConsole() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder("Console"));

            output = new JTextArea();

            this.add(new JScrollPane(output));
            Logger.subscribe(this);
        }

		@Override
		public void update(LogLevel level, Class<?> clazz, String message) {
			if ((level.getMask() & Logger.getLogLevel()) > 0)
			{
			    log(level, clazz, message);
			}			
		}
		
		public void log(LogLevel level, Class<?> clazz, String message){
			if(message == null) return;
			String source = (clazz == null) ? String.format("") : clazz.getSimpleName();
			
			if(message.length() > 1 && "\n".equals(message.substring(0, 1))){
				message = message.substring(1, message.length());
				appendText(String.format("%1$-25s", source));
			}
			appendText(String.format("%1$-5s%2$-25s",level, source) + message + "\n");
		}

        
        public void appendText(final String text) {
            if (EventQueue.isDispatchThread()) {
                output.append(text);
                output.setCaretPosition(output.getText().length());
            } else {

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        appendText(text);
                    }
                });

            }
        }        
    }