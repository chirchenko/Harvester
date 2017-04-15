package gui;

import javax.swing.DefaultListModel;

import domains.DataChangeListener;
import domains.Fields;
import domains.Fields.Field;

@SuppressWarnings("serial")
public class GuiFieldsPanel extends GuiAbstractPanel<Field> implements DataChangeListener{

	public GuiFieldsPanel(String string, SelectedListener selectedListener) {
		super(string, selectedListener);
	}
	
	@Override
	public void loadData(DefaultListModel<Field> model) {
		model.clear();
		for(Field m : Fields.getFields()){
			model.addElement(m);
		}	
	}

}
