package ui.main.component;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;

import ui.dragndrop.flavor.PanelDropFlavor;
import ui.frame.ComponentFrame;
import ui.frame.DashedGraphicsFrame;


public class DropTargetPanel extends JPanel {
	private static final long serialVersionUID = -1979741202799347471L;
	private final List<JPanel> componentTarget = new ArrayList<JPanel>();
	private JList<?> targetList;
	public DropTargetPanel(JList<?> targetList){
		super();
		this.setLayout(null);
		this.targetList = targetList;
		DashedGraphicsFrame placeholderFrame = new DashedGraphicsFrame();
		this.componentTarget.add(placeholderFrame);
		this.add(placeholderFrame);
		new DropTargetListener(this);

	}
	
	public void setTargetList(JList<?> targetList){
		this.targetList = targetList;
	}
	public void relayout(){
		for(int i = 0; i < componentTarget.size(); i ++){
			if(!(componentTarget.get(i) instanceof DashedGraphicsFrame)){
				componentTarget.get(i).setBounds(5, i * 40 + 5,this.getWidth() - 5,45);
			}else{
				componentTarget.get(i).setBounds(18, i * 40 + 5,this.getWidth() - 30,45);
			}
		}
		this.revalidate();
	}

	public void markPanelForRemoval(JPanel panel){
		componentTarget.remove(panel);
		this.remove(panel);
		this.relayout();
	}
	public Object[] getTargetValue(String nameKey){
		for(int i = 0; i < componentTarget.size(); i++){
			ComponentFrame frame = (ComponentFrame)componentTarget.get(i);
			if(frame.getName().equals(nameKey)){
				return new Object[]{frame.getValue(), "Usepassthrough="+frame.getPassThrough(), "UseapplyLocal="+frame.getApplyLocal(), "Usetimecode="+frame.getUseTimeCode()};
			}
		}
		return null;
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
			final PanelDropFlavor flavor = new PanelDropFlavor();
			try{
				if(transferable.getTransferData(flavor) instanceof JPanel){
					ComponentFrame panel = (ComponentFrame)transferable.getTransferData(flavor);
					componentTarget.add(componentTarget.size() -1,panel);
					ComponentPopupMenu.attachReverseHandler(targetList, panel);
					target.add(panel);
					panel.getUseTimeCode().addItemListener(panel.new EnableTimecodeListener());
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
}