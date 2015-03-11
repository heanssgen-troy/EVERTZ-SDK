package ui.main.execute;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import ui.dragndrop.handler.PanelTransferHandler;
import ui.main.component.DropTargetPanel;
import ui.main.component.SourceList;

public class Executable {
	
	public static JFrame createMainPanel(){
		JFrame frame = new JFrame();
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		
		SourceList list = new SourceList();
		DropTargetPanel dropPanel = new DropTargetPanel(list);
		list.setTransferHandler(new PanelTransferHandler());
		JScrollPane dropPane = new JScrollPane(dropPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane listPane = new JScrollPane(list,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listPane.setBounds(950,230,200,300);
		dropPane.setBounds(10,230,930,600);
		frame.add(listPane);
		frame.add(dropPane);
		frame.revalidate();
		
		return frame;
		
	}
	
	public static void main(String [] args){
		JFrame frame = Executable.createMainPanel();
		frame.setVisible(true);
	}
}
