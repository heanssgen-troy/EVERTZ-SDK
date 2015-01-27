package data.formatter;

import metadata.MetadataTag;
import data.Packet;

public abstract class AFormatter {
	private int numBytes = 0;
	private String version;
	private MetadataTag metadata;
	public void setBytes(int byteSize){
		this.numBytes = byteSize;
	}
	public int getBytes(){
		return this.numBytes;
	}
	public void setVersion(String version){
		this.version = version;
	}
	public String getVersion(){
		return this.version;
	}
	public abstract Packet doFormatting();
	public void setHeaderPacket(MetadataTag tag){
		this.metadata = tag;
	}
	public MetadataTag getHeaderPacket(){
		return this.metadata;
	}
}
