package ui.loader.data;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlAttribute;
@XmlRootElement
public class DragType {
	
	private String name;
	private String type;
	private int defaultState;
	public DragType(){
		
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setDefaultState(int defaultState) {
		this.defaultState = defaultState;
	}
	public DragType(String name, String type, int defaultState){
		this.name = name;
		this.type = type;
		this.defaultState = defaultState;
	}
	@XmlAttribute
	public String getName() {
		return name;
	}
	@XmlAttribute
	public String getType() {
		return type;
	}
	@XmlAttribute
	public int getDefaultState() {
		return defaultState;
	}
	public String toString(){
		return this.name;
	}
}
