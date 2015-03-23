package ui.frame;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import ui.dragndrop.flavor.PanelDropFlavor;

@SuppressWarnings("serial")
public class ComponentFrame extends JPanel implements Transferable{
	private JTextField component;
	private boolean isDefault;
	private String name;
	private JCheckBox  usePassThrough = new JCheckBox();
	private JCheckBox  applyLocal = new JCheckBox();
	private JCheckBox  useTimecode = new JCheckBox();
	private JTextField hourField = new JTextField();
	private JTextField minuteField = new JTextField();
	private JTextField secondField = new JTextField();
	
	public ComponentFrame(String name, String type, boolean isDefault, JTextField component){
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
		JLabel hourMinSeparatorLabel = new JLabel(":");
		JLabel minSecSeparatorLabel = new JLabel(":");
		nameField.setBounds(15,5,180,25);
		typeField.setBounds(200,5,180,25);
		hourMinSeparatorLabel.setBounds(760,5,10,25);
		minSecSeparatorLabel.setBounds(830,5,10,25);
		component.setBounds(385,5,180,25);
		usePassThrough.setBounds(570,5,25,25);
		applyLocal.setBounds(615,5,25,25);
		useTimecode.setBounds(660,5,25,25);
		hourField.setBounds(700,5,50,25);
		secondField.setBounds(770,5,50,25);
		minuteField.setBounds(840,5,50,25);

		nameField.setEditable(false);
		typeField.setEditable(false);
		usePassThrough.setEnabled(!isDefault);
		applyLocal.setEnabled(!isDefault);
		useTimecode.setEnabled(!isDefault);
		hourField.setEditable(false);
		minuteField.setEditable(false);
		secondField.setEditable(false);
		usePassThrough.setSelected(true);
		nameField.setText(name);
		typeField.setText(type);
		this.add(nameField);
		this.add(typeField);
		this.add(hourMinSeparatorLabel);
		this.add(minSecSeparatorLabel);
		this.add(usePassThrough);
		this.add(applyLocal);
		this.add(useTimecode);
		this.add(component);
		this.add(hourField);
		this.add(secondField);
		this.add(minuteField);
		
		
		for(Component c : this.getComponents()){
			if(c instanceof JComponent && !(c instanceof JLabel)){
				((JComponent) c).setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				((JComponent) c).setInheritsPopupMenu(true);
			}
		}
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
	public boolean getPassThrough(){
		return usePassThrough.isSelected();
	}
	public boolean getApplyLocal(){
		return applyLocal.isSelected();
	}
	public JCheckBox getUseTimeCode(){
		return useTimecode;
	}
	public boolean getIsDefault(){
		return this.isDefault;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return new DataFlavor[]{new PanelDropFlavor()};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// TODO Auto-generated method stub
		return true;
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
	
	public class EnableTimecodeListener implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent e) {
			hourField.setEditable(useTimecode.isSelected());
			minuteField.setEditable(useTimecode.isSelected());
			secondField.setEditable(useTimecode.isSelected());
		}
		
	}
}