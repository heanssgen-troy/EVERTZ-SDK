package ui.main.execute;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JList;

import ui.dragndrop.handler.PanelTransferHandler;
import ui.main.component.DropTargetPanel;
import ui.main.component.SourceList;

public class Executable {
	
	public static void main(String [] args){
		JList<?> sourceList = new SourceList();
		DropTargetPanel panel = new DropTargetPanel();
		sourceList.setTransferHandler(new PanelTransferHandler());
		
		JFrame frame = new JFrame();
		frame.setBounds(100,100,900,900);
		frame.setLayout(new GridLayout(2,1));
		frame.add(sourceList);
		frame.add(panel);
		frame.setVisible(true);
	}
}
