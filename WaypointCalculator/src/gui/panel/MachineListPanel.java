package gui.panel;

import javax.swing.DefaultListModel;

import domains.Machinery;
import domains.Machinery.Machine;
import gui.panel.dialog.EntityDialog;
import gui.panel.dialog.MachineDialog;

@SuppressWarnings("serial")
public class MachineListPanel extends AbstractListPanel<Machine> {

	public MachineListPanel(String listName, SelectedListener selectedListener) {
		super(listName, selectedListener, new Machine());
	}

	@Override
	public void loadData(DefaultListModel<Machine> model) {
		model.clear();
		for (Machine m : Machinery.getMachinery()) {
			model.addElement(m);
		}
	}

	@Override
	public EntityDialog<Machine> assignDialog() {
		return new MachineDialog(this);
	}
}
