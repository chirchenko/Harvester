package gui;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class JAInteger extends JPanel{
	private JTextField sectionsField;

	public JAInteger(String name) {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setBorder(BorderFactory.createTitledBorder(name));
        setMaximumSize(new Dimension (300, 75));
        
		JLabel label1 = new JLabel(String.format("%s:", name));
        
        sectionsField = new JTextField();
        sectionsField.setText("0");
        sectionsField.setEditable(true);
               
        add(label1);
        add(sectionsField);
	}

	public Double getSections() {
		return Double.parseDouble(sectionsField.getText());
	}
	
	public void setSections(int value) {
		sectionsField.setText(String.valueOf(value));
	}

}
