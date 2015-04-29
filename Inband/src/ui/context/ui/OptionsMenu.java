package ui.context.ui;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

public class OptionsMenu extends JPopupMenu {
	private static final long serialVersionUID = -7821560266575347645L;
	private JCheckBox applyLocal = new JCheckBox();
	private JCheckBox usePassthrough = new JCheckBox();
	private JTextField timecode = new JTextField();
	
	public OptionsMenu(){
		this.initMenu();
	}
	
	private void initMenu(){
		JPanel localPanel = new JPanel();
		localPanel.setLayout(new GridLayout(1,2));
		localPanel.add(new JLabel("Apply Local?"));
		localPanel.add(applyLocal);
		
		JPanel passThroughPanel = new JPanel();
		passThroughPanel.setLayout(new GridLayout(1,2));
		passThroughPanel.add(new JLabel("Use Passthrough?"));
		passThroughPanel.add(usePassthrough);
		
		JPanel timecodePanel = new JPanel();
		timecodePanel.setLayout(new GridLayout(1,2));
		timecodePanel.add(new JLabel("Timecode"));
		timecodePanel.add(timecode);
		
		this.add(localPanel);
		this.add(passThroughPanel);
		this.add(timecodePanel);
		
		
	}
	
	public boolean getApplyLocal(){
		return this.applyLocal.isSelected();
	}
	public boolean getPassthrough(){
		return this.usePassthrough.isSelected();
	}
	public String getTimecode(){
		return this.timecode.getText();
	}
	
	public static void main(String [] args){
		JFrame frame = new JFrame();
		frame.setBounds(100,100,500,500);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel pane = new JPanel();
		pane.setComponentPopupMenu(new OptionsMenu());
		frame.add(pane);
		frame.setVisible(true);
	}
}
