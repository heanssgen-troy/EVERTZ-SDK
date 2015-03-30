package ui.loader;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ui.loader.data.DragTypeContainer;

public class DragLoader {
	
	public static DragTypeContainer loadDragTypes(File f) throws JAXBException{
		DragTypeContainer container = null;
		JAXBContext jaxbContext = JAXBContext.newInstance(DragTypeContainer.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		container = (DragTypeContainer) jaxbUnmarshaller.unmarshal(f);
		return container;
	}
	public static void saveDragTypes(File file, DragTypeContainer types) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(DragTypeContainer.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(types, file);
	}
	
	public static void main(String [] args){
		try {
			System.out.println(DragLoader.loadDragTypes(new File("Z:\\drag.xml")).getList());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
