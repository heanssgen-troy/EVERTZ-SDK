package ui.main.component;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class AddressPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final HashMap<String,JTextField> IPTables = new HashMap<String,JTextField>();
	private boolean isIPV4;
	private boolean isEditable;
	
	public boolean isEditable() {
		return isEditable;
	}

	public AddressPanel(boolean isIPV4, boolean isEditable){
		super();
		if(isIPV4){
			this.isIPV4 = true;
			this.isEditable = isEditable;
			this.setLayout(null);
			this.generateIPv4Table();
		}else{
			this.isIPV4 = false;
		}
	}
	public void setAddr(String address){
		String[] resultant = address.split("[.]");
		for(int i = 0; i < 4; i ++){
			IPTables.get("FIELD"+i).setText(resultant[i]);
		}
	}
	private void generateIPv4Table(){
		for(int i = 0; i < 4; i++){
			JTextField field = new JTextField();
			field.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			field.addKeyListener(new SizeLimiter());
			field.setEditable(isEditable);
			field.setBounds(5 + i * 75, 5, 60,25);
			field.setHorizontalAlignment(JTextField.CENTER);
			IPTables.put("FIELD"+i, field);
			this.add(field);
			if(i < 3){
				JLabel label = new JLabel(".");
				label.setBounds(70 + i * 75, 10,15,25);
				label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
				label.setAlignmentY(JLabel.BOTTOM_ALIGNMENT);
				this.add(label);
			}
		}
	}
	public String toString(){
		if(isIPV4){
			String returnable = "";
			for(int i = 0; i < 4; i ++){
				returnable += IPTables.get("FIELD"+i).getText();
				returnable += ".";
			}
			return returnable;
		}else{
			String returnable = "";
			for(int i = 0; i < 8; i ++){
				returnable += IPTables.get("FIELD"+i).getText();
				returnable += ":";
			}
			return returnable;
		}
	}
	
	private class SizeLimiter implements KeyListener{
		@Override
		public void keyTyped(KeyEvent e) {
			JTextField source = (JTextField)e.getSource();
			if((source.getText().length() + 1 > 3 || !Pattern.matches("[0-9]", ""+e.getKeyChar())) && e.getKeyChar() != '\b'){
				e.consume();
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
		
	}
}
