package data.formatter;

import java.nio.ByteBuffer;

import metadata.MetadataEntry;
import metadata.MetadataTag;
import data.Packet;
import data.util.Converter;

public class HeaderFormatter extends AFormatter {

	public HeaderFormatter(int headerSize, String version, MetadataTag tag) {
		super.setBytes(headerSize);
		super.setVersion(version);
		super.setHeaderPacket(tag);
	}

	@Override
	public Packet doFormatting() {
		ByteBuffer header = ByteBuffer.allocate(super.getBytes());
		header.put(super
				.performDefaultFormatting(MetadataTag.Metadata.HEADER_VERSION));
		header.put(super
				.performDefaultFormatting(MetadataTag.Metadata.HEADER_SIZE));
		header.put(super
				.performDefaultFormatting(MetadataTag.Metadata.PROGRAM_REFERENCE));
		header.put(super
				.performDefaultFormatting(MetadataTag.Metadata.ASSOCIATION_TYPE));

		int[] ipBlockValues = determineAssociationSource(((String) super
				.getHeaderPacket().getValue(
						MetadataTag.Metadata.ASSOCIATION_SOURCE).metadataValue));

		header.put(new byte[10]);
		header.put(Converter.convertIntToByte(ipBlockValues[0]));
		header.put(Converter.convertIntToByte(ipBlockValues[1]));
		header.put(Converter.convertIntToByte(ipBlockValues[2]));
		header.put(Converter.convertIntToByte(ipBlockValues[3]));
		header.put(Converter.convertIntToByte(ipBlockValues[4]));
		header.put(super
				.performDefaultFormatting(MetadataTag.Metadata.ASSOCIATION_PROGRAM));
		header.put(super
				.performDefaultFormatting(MetadataTag.Metadata.ASSOCIATION_PID));
		header.put((byte) 0);
		
		int[] injectTimeValues = determineInjectTime(((String) super
				.getHeaderPacket().getValue(
						MetadataTag.Metadata.INJECT_TIME).metadataValue));
		
		header.put(Converter.convertIntToByte(injectTimeValues[0]));
		header.put(Converter.convertIntToByte(injectTimeValues[1]));
		header.put(Converter.convertIntToByte(injectTimeValues[2]));
		header.put(Converter.convertIntToByte(injectTimeValues[3]));
		header.put(super.performDefaultFormatting(MetadataTag.Metadata.PAYLOAD_LENGTH));
		header.put(super.performDefaultFormatting(MetadataTag.Metadata.PTS_MODE));
		header.put(super.performDefaultFormatting(MetadataTag.Metadata.PTS_OFFSET));
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
		
		MetadataTag tag = new MetadataTag();
		tag.putValue(MetadataTag.Metadata.HEADER_VERSION, new MetadataEntry(1,1));
		tag.putValue(MetadataTag.Metadata.HEADER_SIZE, new MetadataEntry(64,1));
		tag.putValue(MetadataTag.Metadata.PROGRAM_REFERENCE, new MetadataEntry(0,1));
		tag.putValue(MetadataTag.Metadata.ASSOCIATION_TYPE, new MetadataEntry(2,1));
		tag.putValue(MetadataTag.Metadata.ASSOCIATION_SOURCE, new MetadataEntry("233.0.0.1:1234",6));
		tag.putValue(MetadataTag.Metadata.ASSOCIATION_PROGRAM, new MetadataEntry(17,2));
		tag.putValue(MetadataTag.Metadata.ASSOCIATION_PID, new MetadataEntry(66,2));
		tag.putValue(MetadataTag.Metadata.INJECT_TIME, new MetadataEntry("11:10:21-16",4));
		tag.putValue(MetadataTag.Metadata.PAYLOAD_LENGTH, new MetadataEntry(1023,4));
		tag.putValue(MetadataTag.Metadata.PTS_MODE, new MetadataEntry(5,1));
		tag.putValue(MetadataTag.Metadata.PTS_OFFSET, new MetadataEntry(2,4));
		HeaderFormatter formatter = new HeaderFormatter(64, "v1", tag);
		
		Packet p = formatter.doFormatting();
		
		for(byte b : p.getData()){
			System.out.println(b);
		}
	
	}

}