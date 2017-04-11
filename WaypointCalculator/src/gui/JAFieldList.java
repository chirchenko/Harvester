package gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import domains.Fields;
import domains.Fields.Field;

@SuppressWarnings("serial")
public class JAFieldList extends JPanel{

	public JList<Field> displayList;
	
	public JAFieldList() {
	    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder("Fields"));
	    
	    displayList = new JList<>(Fields.getFields().toArray(new Field[0]));
	    
	    displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    displayList.setLayoutOrientation(JList.HORIZONTAL_WRAP);  
	    
	    this.add(new JScrollPane(displayList)); 
	}

	public Field getSelected() {
		return displayList.getSelectedValue();
	}

}
