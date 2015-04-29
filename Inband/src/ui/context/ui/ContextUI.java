package ui.context.ui;

import java.awt.GridLayout;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ui.context.data.ContextType;
import ui.context.data.IMetadataContext;
import ui.context.data.MetadataContextImpl;
import ui.context.dragndrop.ContextFlavor;
import ui.context.factory.ContextContainer;

public class ContextUI extends JPanel implements DropTargetListener{
	private static final long serialVersionUID = 5013879203506509969L;
	private IMetadataContext context;
	
	public ContextUI(IMetadataContext context){
		this.context = context;
		GridLayout layout = new GridLayout(0,1);
		layout.setVgap(10);
		this.setLayout(layout);
		this.initDefaultComponents();
	}
	
	private void initDefaultComponents(){
		for(ContextType cont : context.getContextTypes()){
			if(!cont.getIsDefault()){
				ContextContainer c = new ContextContainer(cont);
				this.add(c);
				c.setComponentPopupMenu(new OptionsMenu());
			}
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}
	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}
	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}
	@Override
	public void dragExit(DropTargetEvent dte) {
	}
	@Override
	public void drop(DropTargetDropEvent dtde) {
		final Transferable transferable = dtde.getTransferable();
		if(transferable.isDataFlavorSupported(new ContextFlavor())){
			try {
				Object data = transferable.getTransferData(new ContextFlavor());
				if(data instanceof ContextType){
					ContextContainer c = new ContextContainer((ContextType)data);
					c.setComponentPopupMenu(new OptionsMenu());
					this.add(c);
					dtde.acceptDrop(DnDConstants.ACTION_MOVE);
				}
			} catch (UnsupportedFlavorException | IOException e) {
				dtde.rejectDrop();
				e.printStackTrace();
			}
		}
	}
	public static void main(String [] args){
		MetadataContextImpl impl = new MetadataContextImpl();
		impl.loadContext(new File("/home/troy/Documents/test.xml"));
		ContextUI ui = new ContextUI(impl);
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100,100,600,600);
		frame.add(ui);
		frame.setVisible(true);
	}
}
