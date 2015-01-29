package data.formatter;

import java.nio.ByteBuffer;

import metadata.HeaderContainer;
import data.Packet;
import data.formatter.Abstract.AFormatter;
import data.formatter.Abstract.IPacketHeader;
import data.header.FirmwareHeader;

public class FirmwareHeaderFormatter extends AFormatter{
	public FirmwareHeaderFormatter(HeaderContainer<IPacketHeader> headerInformation){
		super.setHeaderPacket(headerInformation);
	}
	@Override
	public Packet doFormatting() {
		StringBuffer firmwareBuffer = new StringBuffer();
		
		firmwareBuffer.append("@\n\n");
		firmwareBuffer.append("X$EVTFWIMG{");
		
		for(FirmwareHeader tag : FirmwareHeader.values()){
			final String name = tag.getName();
			final Object value = super.getHeaderPacket().getValue(tag).metadataValue;
			if(value instanceof Object[]){
				firmwareBuffer.append("\"" + name + " : [");
				Object[] values = (Object[])value;
				for(Object arrayValue : values){
					firmwareBuffer.append(" \"" + arrayValue + "\"");
					if(arrayValue != values[values.length]){
						firmwareBuffer.append(",");
					}
				}
				firmwareBuffer.append(" ],");
			}
			firmwareBuffer.append("\"" + name + " : " + "\"" + value + "\"");
		}
		ByteBuffer buffer = ByteBuffer.allocate(firmwareBuffer.toString().length() * 2);
		for(char c : firmwareBuffer.toString().toCharArray()){
			buffer.putChar(c);
		}
		return new Packet(buffer.array());
	}
	
}