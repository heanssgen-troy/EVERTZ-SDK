package metadata;

import java.util.HashMap;

import data.formatter.Abstract.IPacketHeader;

public final class HeaderContainer<T extends IPacketHeader> {

	private HashMap<T , HeaderEntry> metadataValues = new HashMap<T, HeaderEntry>();
	public HeaderContainer(){
	}
	public void putValue(T tag, HeaderEntry value) {
		metadataValues.put(tag, value);
	}
	public HeaderEntry getValue(T tag){
		return metadataValues.get(tag);
	}

}