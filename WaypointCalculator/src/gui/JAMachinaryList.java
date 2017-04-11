package gui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import domains.Machinery;
import domains.Machinery.Machine;

@SuppressWarnings("serial")
public class JAMachinaryList extends JPanel{

	public JList<Machine> displayList;
	
	public JAMachinaryList() {
	    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder("Harvesters"));        
	    
	    displayList = new JList<>(Machinery.getMachinery().toArray(new Machine[0]));
	    
	    displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    displayList.setLayoutOrientation(JList.HORIZONTAL_WRAP);  
	    
	    this.add(new JScrollPane(displayList));
	}

	public Machine getSelected() {
		return displayList.getSelectedValue();
	}

}
