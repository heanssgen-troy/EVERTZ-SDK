package ui.main.component;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ComponentPopupMenu extends JPopupMenu {
	public static void attachReverseHandler(JList<?> target, Component source) {
		((JComponent) source).setComponentPopupMenu(new ComponentPopupMenu(target,source));
	}

	private static final long serialVersionUID = -212758826249916319L;
	private JList<?> target;
	private Component source;

	public ComponentPopupMenu(JList<?> target, Component source) {
		super();
		this.target = target;
		this.source = source;
		this.addComponents();
	}

	private void addComponents() {
		JMenuItem item = new JMenuItem("Remove Metadata");
		item.addActionListener(new ComponentContextListener());
		this.add(item);
	}

	private class ComponentContextListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			@SuppressWarnings("unchecked")
			DefaultListModel<JPanel> model = (DefaultListModel<JPanel>) target.getModel();
			model.addElement((JPanel)source);
			if (source.getParent() instanceof DropTargetPanel) {
				DropTargetPanel parent = (DropTargetPanel) source.getParent();
				parent.markPanelForRemoval((JPanel) source);
			}

		}
	}

}
