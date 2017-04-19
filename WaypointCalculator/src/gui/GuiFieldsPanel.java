package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;

import domains.DataChangeListener;
import domains.Fields;
import domains.Fields.Field;
import domains.Points.Point;

@SuppressWarnings("serial")
public class GuiFieldsPanel extends GuiAbstractPanel<Field> implements DataChangeListener {

	public GuiFieldsPanel(String string, SelectedListener selectedListener){
		super(string, selectedListener, new Field());
	}

	@Override
	public void loadData(DefaultListModel<Field> model) {
		model.clear();
		for (Field m : Fields.getFields()) {
			model.addElement(m);
		}
	}

	@Override
	public GuiEntityDialog<Field> assignDialog() {
		return entityDialog = new GuiEntityDialog<Field>(this) {

			private JTextField txtId;
			private JTextField txtName;
			private GuiPointsPanel listPoints;
			
			@Override
			public void composeEntityElements(JPanel panel, Field entity) {

				panel.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();

				JLabel lblId = new JLabel("Id");
				lblId.setVisible(entity.id != 0);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.insets = new Insets(0, 10, 5, 0);
				gbc.weightx = 1;
				panel.add(lblId, gbc);

				txtId = new JTextField();
				txtId.setVisible(entity.id != 0);
				txtId.setText(String.valueOf(entity.id));
				gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridwidth = 3;
				gbc.gridx = 1;
				gbc.gridy = 0;
				gbc.insets = new Insets(5, 0, 0, 10);
				gbc.weightx = 1;
				txtId.setEditable(false);
				panel.add(txtId, gbc);

				JLabel lblName = new JLabel("Name");
				gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridwidth = 1;
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.insets = new Insets(0, 10, 0, 0);
				gbc.weightx = 1;
				panel.add(lblName, gbc);

				txtName = new JTextField(entity.name, 15);
				gbc = new GridBagConstraints();
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridwidth = 3;
				gbc.gridx = 1;
				gbc.gridy = 1;
				gbc.insets = new Insets(5, 0, 0, 10);
				gbc.weightx = 1;
				panel.add(txtName, gbc);

				Point point = new Point();
				point.fieldId = entity.id;
				point.seq = entity.points.size();
				
				listPoints = new GuiPointsPanel("Points", null, point);
				gbc = new GridBagConstraints();
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.fill = GridBagConstraints.BOTH;
				gbc.gridwidth = 4;
				gbc.gridheight = 4;
				gbc.gridx = 0;
				gbc.gridy = 3;
				gbc.insets = new Insets(5, 10, 5, 10);
				gbc.weightx = 2;
				gbc.weighty = 2;
				panel.add(listPoints, gbc);				
			}

			@Override
			public Field collectEntity(Field field) {
				field.id = Integer.valueOf(txtId.getText());
				field.name = txtName.getText();
				field.points = new ArrayList<>();
				ListModel<Point> elements = listPoints.displayList.getModel();
				for(int i = 0; i < elements.getSize(); i++){
					field.points.add(elements.getElementAt(i));
				}
				return field;
			}
		};
	}
}
