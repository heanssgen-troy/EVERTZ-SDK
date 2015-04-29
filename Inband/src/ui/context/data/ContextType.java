package ui.context.data;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import ui.context.dragndrop.ContextFlavor;
@XmlType(name="ContextType")
@XmlRootElement
public class ContextType implements Transferable{
	private String contextName;
	private String contextType;
	private String contextDevelopmentName;
	private Class<?> contextClass;
	private Boolean isDefault;
	private String contextTag;
	@XmlTransient
	private JComponent contextComp;
	public String getContextTag() {
		return contextTag;
	}
	@XmlTransient
	public JComponent getContextComponent() {
		return contextComp;
	}

	public void setContextComp(JComponent contextComp) {
		this.contextComp = contextComp;
	}

	public void setContextTag(String contextTag) {
		this.contextTag = contextTag;
	}
	public String getContextName() {
		return contextName;
	}
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}
	public void setContextDevelopmentName(String contextDevelopmentName) {
		this.contextDevelopmentName = contextDevelopmentName;
	}
	public void setContextClass(Class<?> contextClass) {
		this.contextClass = contextClass;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getContextType() {
		return contextType;
	}
	public String getContextDevelopmentName() {
		return contextDevelopmentName;
	}
	public Class<?> getContextClass() {
		return contextClass;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{new ContextFlavor()};
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(new ContextFlavor());
	}

	@Override
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if(this.isDataFlavorSupported(flavor)){
			return this;
		}
		return null;
	}
	
	public static void main(String [] args){
		MetadataContextImpl impl = new MetadataContextImpl();
		for(int i = 0; i < 10; i++){
			ContextType type = new ContextType();
			type.setContextClass(JTextField.class);
			type.setContextDevelopmentName("dev" + i);
			type.setContextName("name " + i);
			type.setContextTag("ipv4");
			type.setIsDefault(true);
			type.setContextType("String");
			impl.getContextTypes().add(type);
		}
		impl.saveContext(new File("/home/troy/Documents/test.xml"));
	}
}
