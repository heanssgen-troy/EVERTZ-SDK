package ui.dragndrop;
import java.awt.GridLayout;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.dragndrop.handler.PanelDropFlavor;
import ui.dragndrop.handler.PanelTransferHandler;
import ui.frame.AbstractFrame;
import ui.frame.DashedGraphicsFrame;


public class DropTargetPanel extends JPanel {
	private static final long serialVersionUID = -1979741202799347471L;
	private final List<JPanel> componentTarget = new ArrayList<JPanel>();
	
	public DropTargetPanel(){
		DashedGraphicsFrame placeholderFrame = new DashedGraphicsFrame();
		componentTarget.add(placeholderFrame);
		this.add(placeholderFrame);
		relayout();
		new DropTargetListener(this);
		this.setLayout(null);
		
		this.setVisible(true);
	}
	
	private void relayout(){
		for(int i = 0; i < componentTarget.size(); i ++){
			componentTarget.get(i).setBounds(5, i * 40 + 5,this.getWidth() - 5,45);
		}
	}
	private class DropTargetListener extends DropTargetAdapter{
		private JPanel target;
		public DropTargetListener(JPanel panel){
			this.target = panel;
			new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
		}
		@Override
		public void drop(DropTargetDropEvent dropEvent) {
			final Transferable transferable = dropEvent.getTransferable();
			try{
				if(transferable.getTransferData(new PanelDropFlavor()) instanceof JPanel){
					JPanel panel = (JPanel)transferable.getTransferData(new PanelDropFlavor());
					componentTarget.add(componentTarget.size() -1,panel);
					target.add(panel);
					dropEvent.acceptDrop(DnDConstants.ACTION_COPY);
					dropEvent.dropComplete(true);
					relayout();
				}
			}catch(Exception e){
				e.printStackTrace();
				dropEvent.dropComplete(false);
			}
		}
		
	}

	public static void main(String [] args){
		DropTargetPanel handler = new DropTargetPanel();
		JFrame frame = new JFrame();
		JList<JPanel> list = new JList<JPanel>();
		list.setTransferHandler(new PanelTransferHandler());
		DefaultListModel<JPanel> model = new DefaultListModel<JPanel>();
		List<JPanel> panelList = new ArrayList<JPanel>();
		for(int i = 0; i < 5; i ++){
			panelList.add(new AbstractFrame("Test" + i, "STRING \\16", true, new JTextField()));
			model.addElement(panelList.get(i));
		}
		list.setModel(model);
		list.setDragEnabled(true);
		frame.setLayout(new GridLayout(2,1));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100,100,900,900);
		frame.add(handler);
		frame.add(list);
		frame.setVisible(true);
		
	}
	
}