package ui.dragndrop;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.dragndrop.flavor.PanelDropFlavor;
import ui.dragndrop.flavor.PanelTransferHandler;
import ui.frame.AbstractFrame;


public class DropTargetPanel extends JPanel {
	private static final long serialVersionUID = -1979741202799347471L;
	private final List<JPanel> componentTarget = new ArrayList<JPanel>();
	private final BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f);
	public DropTargetPanel(){
		componentTarget.add(new JPanel());
		new DropTargetListener(this);
		this.setLayout(new GridLayout(1,1));
		this.setVisible(true);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D graphics = (Graphics2D)g;
		graphics.setColor(Color.GRAY);
		graphics.setStroke(dashed);
		graphics.draw(new RoundRectangle2D.Double(15,(componentTarget.size()) * 40 + 5, this.getWidth()-30,40,10,10));
	}
	
	private class DropTargetListener extends DropTargetAdapter{
		private JPanel target;
		private DropTarget dropTarget;
		public DropTargetListener(JPanel panel){
			this.target = panel;
			dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY, this, true, null);
		}
		@Override
		public void drop(DropTargetDropEvent dropEvent) {
			System.out.println(dropEvent.getCurrentDataFlavorsAsList());
			final Transferable transferable = dropEvent.getTransferable();
			try{
				System.out.println(transferable.getTransferData(new PanelDropFlavor()));
				System.out.println("HEY");
				if(transferable.getTransferData(new PanelDropFlavor()) instanceof JPanel){
					
					dropEvent.acceptDrop(DnDConstants.ACTION_COPY);
					componentTarget.add((JPanel)transferable.getTransferData(new PanelDropFlavor()));
					target.add(componentTarget.get(componentTarget.size()-1));
					dropEvent.dropComplete(true);
					System.out.println("Proper drop");
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
			panelList.add(new AbstractFrame("Test" + i, "STRING\16", true, new JTextField()));
			model.addElement(panelList.get(i));
		}
		list.setModel(model);
		list.setDragEnabled(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100,100,600,600);
		frame.add(handler,BorderLayout.CENTER);
		frame.add(list,BorderLayout.NORTH);
		frame.setVisible(true);
		
	}
	
}