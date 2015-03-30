package ui.loader.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Container")
@XmlAccessorType (XmlAccessType.FIELD)
public class DragTypeContainer implements Serializable {
	private static final long serialVersionUID = -6621017721950525627L;
	@XmlElement(name="Type")
	private List<DragType> typeList = new ArrayList<DragType>();
	
	public List<DragType> getList(){
		return typeList;
	}
	public void setList(List<DragType> list){
		this.typeList = list;
	}
	
}
