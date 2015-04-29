package ui.context.data;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class MetadataContextImpl implements IMetadataContext {
	@XmlTransient
	private MetadataContextImpl instance;
	@XmlAnyElement(lax=true)
	private List<ContextType> contextList = new LinkedList<ContextType>();
	
	private List<ContextType> getContextList(){
		return this.contextList;
	}
	public MetadataContextImpl(){
		
	}
	public MetadataContextImpl getContextInstance(){
		return this.instance;
	}
	@Override
	public void loadContext(File filepath) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(MetadataContextImpl.class,ContextType.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			instance = (MetadataContextImpl) jaxbUnmarshaller.unmarshal(filepath);
			this.contextList = instance.getContextList();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void saveContext(File filepath) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(MetadataContextImpl.class,ContextType.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(this.instance, filepath);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<ContextType> getContextTypes() {
		return this.getContextList();
	}

}
