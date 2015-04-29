package ui.context.factory;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class ItemFactory {

	public static JTextField getLabelFields(String text){
		JTextField component = new JTextField(text);
		component.setHorizontalAlignment(JTextField.CENTER);
		component.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		component.setEditable(false);
		component.setFont(component.getFont().deriveFont(Font.BOLD));
		return component;
	}
}
