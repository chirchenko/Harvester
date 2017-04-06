package gui;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import domains.Fields;
import domains.Fields.Field;
import geometry.Point;

@SuppressWarnings("serial")
public class JAFieldList extends JPanel{

	private JButton showDialogButton;
	public JList<Field> displayList;
	
	public JAFieldList() {
	    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder("Fields"));
        
        showDialogButton = new JButton("Import");
	    
	    displayList = new JList<>(Fields.getFields().toArray(new Field[0]));
	    
	    displayList.setPreferredSize(new Dimension (300, 400));
	    displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    displayList.setLayoutOrientation(JList.HORIZONTAL_WRAP);  
	    
	    this.add(new JScrollPane(displayList));
	    this.add(showDialogButton);	   
	}

	public Field getSelected() {
		return displayList.getSelectedValue();
	}

}
