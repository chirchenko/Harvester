package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import logginig.Logger;

@SuppressWarnings("serial")
public class JAConsole extends JPanel implements ActionListener {
	
	JTextArea output;        
    private JButton buttonExtend;
    private ConsoleFrame consolFrame;

    public JAConsole() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Console"));            

        output = new JTextArea();
        buttonExtend = new JButton("Extend console");
        consolFrame = new ConsoleFrame(this);
        this.add(new JScrollPane(output), BorderLayout.CENTER);
        this.add(buttonExtend, BorderLayout.SOUTH);
        
        Logger.subscribe(new GuiConsoleLogger(this));
        
        buttonExtend.addActionListener(this);
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == buttonExtend){
			consolFrame.display();
		}
	}        
}