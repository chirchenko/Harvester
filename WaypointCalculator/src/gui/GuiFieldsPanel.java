package gui;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import domains.DataChangeListener;
import domains.Fields;
import domains.Fields.Field;

@SuppressWarnings("serial")
public class GuiFieldsPanel extends GuiAbstractPanel<Field> implements DataChangeListener{
	
	public GuiFieldsPanel(String string, SelectedListener selectedListener) throws InstantiationException, IllegalAccessException {
		super(string, selectedListener);
		
		entityDialog = new GuiEntityDialog<Field>(SwingUtilities.getWindowAncestor(this), new Field()){

					@Override
					public JPanel getEntityPanel() {
						JPanel entityPanel = new JPanel();
						entityPanel.add(new JLabel("Hello"));
						return entityPanel;
					}
					
		};	    
	}
	
	@Override
	public void loadData(DefaultListModel<Field> model) {
		model.clear();
		for(Field m : Fields.getFields()){
			model.addElement(m);
		}	
	}
}
