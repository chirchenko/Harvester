package gui;

import javax.swing.DefaultListModel;

import domains.Machinery;
import domains.Machinery.Machine;

@SuppressWarnings("serial")
public class GuiMachinaryPanel extends GuiAbstractPanel<Machine>{

	public GuiMachinaryPanel(String listName, SelectedListener selectedListener) {
	    super(listName, selectedListener);
	    listEnabled(false);
	}

	@Override
	public void loadData(DefaultListModel<Machine> model) {
		model.clear();
		for(Machine m : Machinery.getMachinery()){
			model.addElement(m);
		}	
	}
}
