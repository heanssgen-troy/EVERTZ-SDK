package data.formatter;

import java.nio.ByteBuffer;

import metadata.HeaderEntry;
import metadata.HeaderContainer;
import data.Packet;
import data.formatter.Abstract.AFormatter;
import data.formatter.Abstract.IPacketHeader;
import data.header.MetadataHeader;
import data.util.Converter;

public class MetadataHeaderFormatter extends AFormatter {

	public MetadataHeaderFormatter(int headerSize, String version, HeaderContainer<IPacketHeader> tag) {
		super.setBytes(headerSize);
		super.setVersion(version);
		super.setHeaderPacket(tag);
	}

	@Override
	public Packet doFormatting() {
		ByteBuffer header = ByteBuffer.allocate(super.getBytes());
		header.put(super
				.performDefaultFormatting(MetadataHeader.HEADER_VERSION));
		header.put(super
				.performDefaultFormatting(MetadataHeader.HEADER_SIZE));
		header.put(super
				.performDefaultFormatting(MetadataHeader.PROGRAM_REFERENCE));
		header.put(super
				.performDefaultFormatting(MetadataHeader.ASSOCIATION_TYPE));

		int[] ipBlockValues = determineAssociationSource(((String) super
				.getHeaderPacket().getValue(
						MetadataHeader.ASSOCIATION_SOURCE).metadataValue));

		header.put(new byte[10]);
		header.put(Converter.convertIntToByte(ipBlockValues[0]));
		header.put(Converter.convertIntToByte(ipBlockValues[1]));
		header.put(Converter.convertIntToByte(ipBlockValues[2]));
		header.put(Converter.convertIntToByte(ipBlockValues[3]));
		header.put(Converter.convertIntToByte(ipBlockValues[4]));
		header.put(super
				.performDefaultFormatting(MetadataHeader.ASSOCIATION_PROGRAM));
		header.put(super
				.performDefaultFormatting(MetadataHeader.ASSOCIATION_PID));
		header.put((byte) 0);
		
		int[] injectTimeValues = determineInjectTime(((String) super
				.getHeaderPacket().getValue(
						MetadataHeader.INJECT_TIME).metadataValue));
		
		header.put(Converter.convertIntToByte(injectTimeValues[0]));
		header.put(Converter.convertIntToByte(injectTimeValues[1]));
		header.put(Converter.convertIntToByte(injectTimeValues[2]));
		header.put(Converter.convertIntToByte(injectTimeValues[3]));
		header.put(super.performDefaultFormatting(MetadataHeader.PAYLOAD_LENGTH));
		header.put(super.performDefaultFormatting(MetadataHeader.PTS_MODE));
		header.put(super.performDefaultFormatting(MetadataHeader.PTS_OFFSET));
		header.put(new byte[26]);

		return new Packet(header.array());
	}

	public int[] determineAssociationSource(String assocSource) {
		String[] values = assocSource.split("[.:]");
		String hexRepresentation = "";
		for (int i = 0; i < values.length; i++) {
			hexRepresentation += Converter.convertLongToStandardHex(Long
					.parseLong(values[i]));
		}

		int[] returnable = new int[5];
		for (int i = 0; i < 4; i++) {
			returnable[i] = Integer.parseInt(
					hexRepresentation.substring(0, 2), 16);
			hexRepresentation = hexRepresentation.substring(2);
			
		}
		returnable[4] = Integer.parseInt(hexRepresentation, 16);
		return returnable;
	}

	public int[] determineInjectTime(String s) {
		String[] integerInterface = s.split("[:-]");
		int[] returnableIntegers = new int[integerInterface.length];

		for (int i = 0; i < integerInterface.length; i++) {
			returnableIntegers[i] = Integer.parseInt(integerInterface[i]);
		}
		int hour = returnableIntegers[0];
		int minute = returnableIntegers[1];
		int second = returnableIntegers[2];
		int frame = returnableIntegers[3];

		if (hour <= 23 && minute <= 59 && second <= 59 && frame <= 29) {
			return returnableIntegers;
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		
		HeaderContainer<IPacketHeader> tag = new HeaderContainer<IPacketHeader>();
		tag.putValue(MetadataHeader.HEADER_VERSION, new HeaderEntry(1,1));
		tag.putValue(MetadataHeader.HEADER_SIZE, new HeaderEntry(64,1));
		tag.putValue(MetadataHeader.PROGRAM_REFERENCE, new HeaderEntry(0,1));
		tag.putValue(MetadataHeader.ASSOCIATION_TYPE, new HeaderEntry(2,1));
		tag.putValue(MetadataHeader.ASSOCIATION_SOURCE, new HeaderEntry("233.0.0.1:1234",6));
		tag.putValue(MetadataHeader.ASSOCIATION_PROGRAM, new HeaderEntry(17,2));
		tag.putValue(MetadataHeader.ASSOCIATION_PID, new HeaderEntry(66,2));
		tag.putValue(MetadataHeader.INJECT_TIME, new HeaderEntry("11:10:21-16",4));
		tag.putValue(MetadataHeader.PAYLOAD_LENGTH, new HeaderEntry(1023,4));
		tag.putValue(MetadataHeader.PTS_MODE, new HeaderEntry(5,1));
		tag.putValue(MetadataHeader.PTS_OFFSET, new HeaderEntry(2,4));
		MetadataHeaderFormatter formatter = new MetadataHeaderFormatter(64, "v1", tag);
		
		Packet p = formatter.doFormatting();
		
		for(byte b : p.getData()){
			System.out.println(b);
		}
	
	}

}