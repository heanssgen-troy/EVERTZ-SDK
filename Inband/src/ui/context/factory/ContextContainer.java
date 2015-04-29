package ui.context.factory;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.regex.Pattern;

import javax.swing.JPanel;
import javax.swing.JTextField;

import ui.context.data.ContextType;

public class ContextContainer extends JPanel{
	private static final long serialVersionUID = -5526772094949607300L;
	private ContextType context;
	private Pattern inputConstraints;
	public ContextContainer(ContextType context){
		this.context = context;
		this.initContext();
	}
	private void initContext(){
		if(this.context.getContextClass().equals(JTextField.class)){
			this.setLayout(new GridLayout(1,3));
			JTextField labelField = ItemFactory.getLabelFields(context.getContextName());

			JTextField typeField = ItemFactory.getLabelFields(context.getContextType());
			JTextField valueField = new JTextField();
			context.setContextComp(valueField);
			typeField.setEditable(false);
			labelField.setEditable(false);
			
			labelField.setInheritsPopupMenu(true);
			typeField.setInheritsPopupMenu(true);
			valueField.setInheritsPopupMenu(true);
			
			this.add(labelField);
			this.add(typeField);
			this.add(valueField);
			if(context.getContextTag().equals("IPV4")){
				this.inputConstraints = Pattern.compile("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$");
			}
		}
		
		if(this.context.getIsDefault()){
			this.setBackground(Color.DARK_GRAY);
		}
	}
	public boolean isInputValid(){
		if(this.context.getContextClass().equals(JTextField.class)){
			JTextField field = (JTextField)context.getContextComponent();
			return inputConstraints.matcher(field.getText()).matches();
		}
		return true;
	}
}
