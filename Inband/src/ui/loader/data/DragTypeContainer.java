package ui.loader.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Container")
@XmlAccessorType (XmlAccessType.FIELD)
public class DragTypeContainer {
	@XmlElement(name="Type")
	private List<DragType> typeList = new ArrayList<DragType>();
	
	public List<DragType> getList(){
		return typeList;
	}
	public void setList(List<DragType> list){
		this.typeList = list;
	}
	
}
