package metadata;

public class HeaderEntry {

	public Object metadataValue;
	public int metadataSize;
	
	public HeaderEntry(Object value, int size){
		this.metadataValue = value;
		this.metadataSize = size;
	}
}
