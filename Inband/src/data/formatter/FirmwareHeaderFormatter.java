package data.formatter;

import java.nio.ByteBuffer;

import metadata.HeaderContainer;
import metadata.HeaderEntry;
import data.Packet;
import data.formatter.Abstract.AFormatter;
import data.formatter.Abstract.IPacketHeader;
import data.header.FirmwareHeader;

public class FirmwareHeaderFormatter extends AFormatter {
	public FirmwareHeaderFormatter(
			HeaderContainer<IPacketHeader> headerInformation) {
		super.setHeaderPacket(headerInformation);
	}

	@Override
	public Packet doFormatting() {
		StringBuffer firmwareBuffer = new StringBuffer();

		firmwareBuffer.append("@");
		firmwareBuffer.append("X$EVTFWIMG{");
		boolean hasBegun = false;
		for (FirmwareHeader tag : FirmwareHeader.values()) {
			final String name = tag.getName();
			final Object value = super.getHeaderPacket().getValue(tag).metadataValue;
			if(hasBegun)
			firmwareBuffer.append(",");
			if (value instanceof Object[]) {
				firmwareBuffer.append("\"" + name + " : [");
				Object[] values = (Object[]) value;
				for (Object arrayValue : values) {
					firmwareBuffer.append(" \"" + arrayValue + "\"");
					if (arrayValue != values[values.length - 1]) {
						firmwareBuffer.append(",");
					}
				}
				firmwareBuffer.append(" ]");
			} else {
				if (value instanceof String) {
					firmwareBuffer.append("\"" + name + " : " + "\"" + value
							+ "\"");
				} else {
					firmwareBuffer.append("\"" + name + " : "
							+ value.toString() + "");
				}
			}
			hasBegun = true;
		}
		firmwareBuffer.append("}");
		ByteBuffer buffer = ByteBuffer.allocate(firmwareBuffer.toString()
				.length() * 2);
		for (char c : firmwareBuffer.toString().toCharArray()) {
			buffer.putChar(c);
		}
		return new Packet(buffer.array());
	}

	public static void main(String[] args) {
		HeaderContainer<IPacketHeader> t = new HeaderContainer<IPacketHeader>();
		t.putValue(FirmwareHeader.DEVICE_GROUP, new HeaderEntry("*", 0));
		t.putValue(FirmwareHeader.DEVICE_MAC, new HeaderEntry("", 0));
		t.putValue(FirmwareHeader.DEVICE_NAME, new HeaderEntry("7881IRD", 0));
		t.putValue(FirmwareHeader.DEVICE_SERIAL, new HeaderEntry("*", 0));
		t.putValue(FirmwareHeader.FIRMWARE_NAME, new HeaderEntry(new Object[] {
				"7882DEC-V102B20141103-0311.img", "s" }, 0));
		t.putValue(FirmwareHeader.FIRMWARE_OVERWRITE_NUMBER, new HeaderEntry(
				new Object[] { 1, "s" }, 0));
		t.putValue(FirmwareHeader.FIRMWARE_SIZE, new HeaderEntry(new Object[] {
				36597544, "s" }, 0));
		t.putValue(FirmwareHeader.METADATA_SOURCE, new HeaderEntry("7781DM-LB",
				0));
		t.putValue(FirmwareHeader.METADATA_VERSION, new HeaderEntry("v01", 0));

		FirmwareHeaderFormatter formatter = new FirmwareHeaderFormatter(t);
		Packet p = formatter.doFormatting();

		for (byte b : p.getData()) {
			if ((char) b != ' ') {
				System.out.print("" + (char) b);
			}
			if ((char) b == '\n') {
				System.out.println();
			}
		}
	}
}