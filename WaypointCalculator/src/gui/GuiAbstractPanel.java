package gui;

import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import domains.DataChangeListener;

@SuppressWarnings("serial")
public abstract class GuiAbstractPanel<T> extends JPanel implements DataChangeListener{

	public JList<T> displayList;
	protected JScrollPane scrollPane;

	public GuiAbstractPanel(String listName) {
		super();
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder(listName));        

		displayList = new JList<>();
        scrollPane = new JScrollPane(displayList);
        
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    add(scrollPane);
        
	    displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    displayList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    displayList.setModel(new DefaultListModel<T>());
	    
	    dataChanged();
	}

	public abstract void loadData(DefaultListModel<T> model);

	public GuiAbstractPanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public GuiAbstractPanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	public T getSelected() {
		return displayList.getSelectedValue();
	}

	@Override
	public void dataChanged() {
		loadData((DefaultListModel<T>) displayList.getModel());
	    revalidate();
		repaint();
	}

	public void listEnabled(boolean enabled) {
		scrollPane.setEnabled(enabled);
		displayList.setEnabled(enabled);
	}

}