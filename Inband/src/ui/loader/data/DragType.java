package ui.loader.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlAttribute;
@XmlRootElement
public class DragType implements Serializable {
	private static final long serialVersionUID = 6666929400759312895L;
	private String name;
	private String type;
	private boolean defaultEnabled;
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
	public Boolean getDefaultEnabled() {
		return defaultEnabled;
	}
	public void setDefaultEnabled(Boolean defaultEnabled) {
		this.defaultEnabled = defaultEnabled;
	}
}
