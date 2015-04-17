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
import ui.main.component.JSONTextArea;
import ui.main.component.SourceList;

public class Executable {
	private JFrame frame;
	private DropTargetPanel dropPanel;
	private SourceList elementSourceList;
	private AddressPanel hostPanel;
	private AddressPanel targetPanel;
	private JSONTextArea metadataArea;
	private JTextField associationType;
	private JTextField associationPID;
	private JTextField associationProgram;
	public JFrame init(){
		frame = new JFrame();
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100,100,1200,900);
		hostPanel = new AddressPanel(true,false);
		targetPanel = new AddressPanel(true,true);
		elementSourceList = new SourceList();
		dropPanel = new DropTargetPanel(elementSourceList);
		metadataArea = new JSONTextArea(dropPanel);
		dropPanel.setMetadataListenerArea(metadataArea);
		associationType = new JTextField();
		associationPID = new JTextField();
		associationProgram = new JTextField();
		
		elementSourceList.setTransferHandler(new PanelTransferHandler());
		JScrollPane dropPane = new JScrollPane(dropPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		JScrollPane listPane = new JScrollPane(elementSourceList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		listPane.setBounds(950,110,200,300);
		dropPane.setBounds(10,110,930,300);
		metadataArea.setBounds(10,420,400,300);
		
		JTextField hostLabel = getLabelFields("Host Address");
		hostLabel.setBounds(10,10,120,25);
		frame.add(hostLabel);
		JTextField targetLabel = getLabelFields("Association Source");
		targetLabel.setBounds(10,45,120,25);
		frame.add(targetLabel);
		
		this.initMetadataComps();
		
		try {
			hostPanel.setAddr(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		hostPanel.setBounds(140,5,300,30);
		targetPanel.setBounds(140,40,300,30);
		
		frame.add(listPane);
		frame.add(dropPane);
		frame.add(hostPanel);
		frame.add(targetPanel);
		frame.add(metadataArea);
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
	private void initMetadataComps(){
		JTextField associationTypeLabel = getLabelFields("Association Type");
		associationTypeLabel.setBounds(450, 10, 120, 25);
		associationType.setBounds(580,10,180,25);
		frame.add(associationTypeLabel);
		JTextField associationPIDLabel = getLabelFields("Association PID");
		associationPIDLabel.setBounds(450, 45, 120, 25);
		associationPID.setBounds(580,45,180,25);
		frame.add(associationPIDLabel);
		JTextField associationProgramLabel = getLabelFields("Association Program");
		associationProgramLabel.setBounds(800, 10, 140, 25);
		associationProgram.setBounds(950,10,200,25);
		frame.add(associationProgramLabel);
		frame.add(associationProgram);
		frame.add(associationPID);
		frame.add(associationType);
	}
	private void initValueLabels(){
		
	}
	public static void main(String [] args){
		Executable e = new Executable();
		JFrame frame = e.init();
		frame.setVisible(true);
	}
}
