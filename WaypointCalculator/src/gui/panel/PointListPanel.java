package gui.panel;

import javax.swing.DefaultListModel;

import domains.Points;
import domains.Points.Point;
import gui.panel.dialog.GuiEntityDialog;
import gui.panel.dialog.GuiPointDialog;

@SuppressWarnings("serial")
public class PointListPanel extends AbstractListPanel<Point> {

	/*
	 * Unlike other panels this one should have explicit Point as 
	 * argument to lock Field related properties
	 */
	public PointListPanel(String listName, SelectedListener selectedListener, Point point) {
		super(listName, selectedListener, point);
		emptyEntity = point;
	}

	@Override
	public void loadData(DefaultListModel<Point> model) {
		model.clear();
		for (Point p : Points.getPoints(emptyEntity.fieldId)) {
			model.addElement(p);
		}
	}

	@Override
	public GuiEntityDialog<Point> assignDialog() {
		return new GuiPointDialog(this);
	}
}
