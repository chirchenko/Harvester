package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import calculator.App;

@SuppressWarnings("serial")
public abstract class GuiEntityDialog<T> extends JPanel implements ActionListener{
    public final static int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
    public final static int OK_OPTION = JOptionPane.OK_OPTION;
    
    //
    protected int             option = GuiEntityDialog.CANCEL_OPTION;
    private LocalDialog     dialog = null;
    private Window          owner = null;
    private String          title = "Entity dialog";
    private T 				entity = null;
	private JButton buttonOk;
	private JButton buttonCancel;

    public GuiEntityDialog(Window owner, T entity) throws InstantiationException, IllegalAccessException {
    	this.entity = entity;
    	this.owner = owner;
    	
    	if(this.entity != null){
    		title = this.entity.toString();
    	}
    	
    	initBasicElements();
	}
    
    private void initBasicElements() {
		buttonOk = new JButton("Ok");
		buttonOk.addActionListener(this);
		
		buttonCancel = new JButton("Cencel");
		buttonCancel.addActionListener(this);	
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		add(buttonOk, c);
		add(buttonCancel, c);
	}

	public abstract JPanel getEntityPanel();
    
    public int showDialog(T entity){
    	this.entity = entity;
    	
    	if (dialog == null) {
    		dialog = new LocalDialog((Frame) owner);
        }
        dialog.setModal(true);
        dialog.setTitle(this.title);
        dialog.setLocationByPlatform(true);
        dialog.getRootPane().setLayout(new BorderLayout());
        dialog.getRootPane().add(getEntityPanel(), BorderLayout.CENTER);
        dialog.getRootPane().add(this, BorderLayout.SOUTH);
        
        dialog.setMinimumSize(new Dimension(App.dim.width * 1 / 4, App.dim.height * 1 / 4));
        dialog.pack();

        dialog.setVisible(true);
        return option;
    }
    
    public T getEntity(){
    	return entity;
    }
    
    protected void clearDialog() {
        if (dialog != null) {
            dialog.setVisible(false);
            dialog.getRootPane().removeAll();
            dialog.removeAll();
            dialog.dispose();
        }
    }

    private class LocalDialog extends JDialog {

        public LocalDialog(Frame owner) {
            super(owner);
        }

        @Override
        protected void processWindowEvent(WindowEvent e) {
            if (e.getID() == WindowEvent.WINDOW_CLOSING) {
                clearDialog();
            }
            super.processWindowEvent(e);
        }
    }
  
    @Override
    public void actionPerformed(ActionEvent e){
    	if(e.getSource() == buttonOk){
    		option = OK_OPTION;
    	}else if(e.getSource() == buttonCancel){
    		option = CANCEL_OPTION;
    	}
    	clearDialog();
    }
    
}