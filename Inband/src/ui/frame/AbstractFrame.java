package ui.frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.dragndrop.DropTargetPanel;
import ui.dragndrop.flavor.PanelDropFlavor;

@SuppressWarnings("serial")
public class AbstractFrame extends JPanel implements Transferable{
	private JTextField component;
	private boolean isDefault;
	private String name;
	public AbstractFrame(String name, String type, boolean isDefault, JTextField component){
		super();
		this.component = component;
		this.isDefault = isDefault;
		this.name = name;
		this.setLayout(null);
		this.init(name,type);
	}
	
	private void init(String name, String type){
		JTextField nameField = new JTextField();
		JTextField typeField = new JTextField();
		JCheckBox  usePassThrough = new JCheckBox();
		JCheckBox  applyLocal = new JCheckBox();
		JCheckBox  useTimecode = new JCheckBox();
		
		nameField.setBounds(15,5,180,25);
		typeField.setBounds(195,5,180,25);
		component.setBounds(385,5,180,25);
		usePassThrough.setBounds(575,5,25,25);
		applyLocal.setBounds(675,5,25,25);
		useTimecode.setBounds(775,5,25,25);
		
		nameField.setEditable(false);
		typeField.setEditable(false);
		
		usePassThrough.setEnabled(!isDefault);
		applyLocal.setEnabled(!isDefault);
		useTimecode.setEnabled(!isDefault);
		usePassThrough.setSelected(isDefault);
		applyLocal.setSelected(isDefault);
		useTimecode.setSelected(isDefault);
		
		this.add(nameField);
		this.add(typeField);
		this.add(usePassThrough);
		this.add(applyLocal);
		this.add(useTimecode);
		this.add(component);
	}
	
	public String getValue(){
		return component.getText();
	}
	public String getName(){
		return component.getName();
	}
	public String toString(){
		return this.name;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return new DataFlavor[]{new PanelDropFlavor()};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		return flavor instanceof PanelDropFlavor;
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if(isDataFlavorSupported(flavor))
			return this;
		else{
			return null;
		}
	}
}