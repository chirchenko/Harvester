package gui;

import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import domains.DataChangeListener;
import domains.ToolTipRecord;

@SuppressWarnings("serial")
public abstract class GuiAbstractPanel<T> extends JPanel 
							implements DataChangeListener, MouseListener{
	
	public interface SelectedListener {
		public void onDoubleClick();
	}
	
	private SelectedListener selectedListener;
	public JList<T> displayList;
	protected JScrollPane scrollPane;

	public GuiAbstractPanel(String listName, SelectedListener selectedListener) {
		super();
		this.selectedListener = selectedListener;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(BorderFactory.createTitledBorder(listName));
        
		this.displayList = new JList<T>(){
			public String getToolTipText(MouseEvent evt) {
		        int index = locationToIndex(evt.getPoint());
		        if(index < 0) return null;
		        T item = getModel().getElementAt(index);
		        if(item instanceof ToolTipRecord){
		        	return ((ToolTipRecord) item).getTooltip();
		        }
		        return item.toString();
		      }
		};
		this.displayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    this.displayList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    this.displayList.setModel(new DefaultListModel<T>());	    
	    this.displayList.addMouseListener(this);
	    
        this.scrollPane = new JScrollPane(displayList
        		, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        		, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    this.add(scrollPane);
	    
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

	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.displayList.getModel().getSize() > 0 && e.getClickCount()==2 && !e.isConsumed()){
        	e.consume();
        	selectedListener.onDoubleClick();
        }
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}