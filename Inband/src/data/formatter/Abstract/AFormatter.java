package data.formatter.Abstract;

import java.nio.ByteBuffer;

import metadata.HeaderEntry;
import metadata.HeaderContainer;
import data.Packet;

public abstract class AFormatter {
	private int numBytes = 0;
	private String version;
	private HeaderContainer<IPacketHeader> metadata;
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
	public byte[] performDefaultFormatting(IPacketHeader data){
		return performBasicFormatting(metadata.getValue((IPacketHeader) data));
	}
	
	private byte[] performBasicFormatting(HeaderEntry entry){
		final Object value = entry.metadataValue;
		byte[] targetBytes = new byte[entry.metadataSize];
		ByteBuffer buffer = ByteBuffer.wrap(targetBytes);
		if(value.getClass() == int.class || value instanceof Integer && entry.metadataSize == 1){
			if(entry.metadataSize == 1){
				buffer.put(((Integer)value).byteValue());
			}
		}
		else if(value instanceof String){
			for(char c : ((String)value).toCharArray()){
				buffer.putChar(c);
			}
		}

		else if(entry.metadataSize == TypeBuffers.INT.getSize() && (Integer)entry.metadataValue < Integer.MAX_VALUE){
			buffer.putInt((int)value);
		}
		else if(entry.metadataSize == TypeBuffers.SHORT.getSize()){
			buffer.putShort(((Integer)value).shortValue());
		}
		
		else if(entry.metadataSize == TypeBuffers.LONG.getSize() && (Integer)entry.metadataValue >Integer.MAX_VALUE && (Integer)entry.metadataValue < Long.MAX_VALUE){
			buffer.putLong(((Integer)value).longValue());
		}
		else if(entry.metadataSize == TypeBuffers.CHAR.getSize()){
			buffer.putChar((char)value);
		}
		return buffer.array();
	}
	public void setHeaderPacket(HeaderContainer<IPacketHeader> tag){
		this.metadata = tag;
	}
	public HeaderContainer<IPacketHeader> getHeaderPacket(){
		return this.metadata;
	}
	
	public enum TypeBuffers{
		CHAR(1),
		SHORT(2),
		INT(4),
		LONG(4),
		FLOAT(4),
		DOUBLE(8);
		
		private int value;
		private TypeBuffers(int value){
			this.value = value;
		}
		public int getSize(){
			return value;
		}
	}
	
}
