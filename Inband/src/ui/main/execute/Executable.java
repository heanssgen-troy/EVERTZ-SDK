package ui.main.execute;

import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import ui.dragndrop.handler.PanelTransferHandler;
import ui.main.component.AddressPanel;
import ui.main.component.DropTargetPanel;
import ui.main.component.SourceList;

public class Executable {
	private JFrame frame;
	private DropTargetPanel dropPanel;
	private SourceList elementSourceList;
	private AddressPanel hostPanel;
	private AddressPanel targetPanel;
	public JFrame init(){
		frame = new JFrame();
		
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100,100,1200,900);
		
		hostPanel = new AddressPanel(true,false);
		targetPanel = new AddressPanel(true,true);
		elementSourceList = new SourceList();
		dropPanel = new DropTargetPanel(elementSourceList);
		
		elementSourceList.setTransferHandler(new PanelTransferHandler());
		JScrollPane dropPane = new JScrollPane(dropPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane listPane = new JScrollPane(elementSourceList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listPane.setBounds(950,230,200,300);
		dropPane.setBounds(10,230,930,600);
		JTextField hostLabel = getLabelFields("Host Address");
		hostLabel.setBounds(10,155,120,25);
		frame.add(hostLabel);
		JTextField targetLabel = getLabelFields("Target Address");
		targetLabel.setBounds(10,185,120,25);
		frame.add(targetLabel);
		
		try {
			hostPanel.setAddr(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		hostPanel.setBounds(140,150,300,30);
		targetPanel.setBounds(140,180,300,30);
		frame.add(listPane);
		frame.add(dropPane);
		frame.add(hostPanel);
		frame.add(targetPanel);
		return frame;
	}
	private JTextField getLabelFields(String text){
		JTextField component = new JTextField(text);
		component.setHorizontalAlignment(JTextField.CENTER);
		component.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		component.setEditable(false);
		component.setFont(component.getFont().deriveFont(Font.BOLD));
		return component;
	}
	
	public static void main(String [] args){
		Executable e = new Executable();
		JFrame frame = e.init();
		frame.setVisible(true);
	}
}
