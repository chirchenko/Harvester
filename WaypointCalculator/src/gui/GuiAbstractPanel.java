package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import domains.DataChangeListener;
import domains.Fields.Field;
import domains.ToolTipRecord;

@SuppressWarnings("serial")
public abstract class GuiAbstractPanel<T> extends JPanel 
							implements DataChangeListener, MouseListener{

	private SelectedListener selectedListener;
	
	public interface SelectedListener {
		public void onDoubleClick();
	}
	
	public GuiEntityDialog<Field> entityDialog;
	public JList<T> displayList;
	
	protected JButton buttonAdd;
	protected JButton buttonRemove;
	protected JButton buttonModify;

	public GuiAbstractPanel(String listName, SelectedListener selectedListener) {
		super();
		this.selectedListener = selectedListener;
		this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(listName));
        
        /*
         * Tooltips are configured to be shown for entities with type
         * ToolTipRecord
         */
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
		
		this.displayList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    this.displayList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
	    this.displayList.setModel(new DefaultListModel<T>());	    
	    this.displayList.addMouseListener(this);
	    
        JScrollPane scrollPane = new JScrollPane(displayList
        		, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        		, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    this.add(scrollPane, BorderLayout.CENTER);
	    
	    /*
	     * 3-button section below list
	     */
	    buttonAdd = new JButton("Add");
	    buttonRemove = new JButton("Remove");
	    buttonModify = new JButton("Edit");
	    
	    buttonAdd.setEnabled(true);
	    buttonRemove.setEnabled(false);
	    buttonModify.setEnabled(false);

	    /*
	     * button click calls edit dialog
	     */
		buttonAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				entityDialog.showDialog(null);
				try {
					entityDialog.getEntity().save();
				} catch (SQLException e1) {
					//logger.info("Saving " + entity.toSttring());
					e1.printStackTrace();
				}
			}
		});
	    
	    /*
	     * Button are enabled/disabled depending on selection
	     */
	    displayList.getSelectionModel().addListSelectionListener(e -> {
	    	
	    	buttonAdd.setEnabled(true);
    		buttonRemove.setEnabled(!displayList.isSelectionEmpty());
	    	buttonModify.setEnabled(!displayList.isSelectionEmpty()
	    			&& displayList.getSelectedValuesList().size() == 1);
	    });
	    
	    JPanel managePanel = new JPanel(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1;
	    managePanel.add(buttonAdd, c);
	    managePanel.add(buttonRemove, c);
	    managePanel.add(buttonModify, c);

	    this.add(managePanel, BorderLayout.SOUTH);
	    
	    dataChanged();
	}

	public abstract void loadData(DefaultListModel<T> model);
	
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
		for(Component c : getComponents()){
			c.setEnabled(enabled);
			for(Component s : getComponents()){
				s.setEnabled(enabled);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.displayList.isEnabled()){
			if(this.displayList.getModel().getSize() > 0 
					&& e.getClickCount()==2 && !e.isConsumed()){
		    	e.consume();
		    	selectedListener.onDoubleClick();
		    }
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