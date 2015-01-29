package data.header;

import data.formatter.Abstract.IPacketHeader;

public enum FirmwareHeader implements IPacketHeader {
	DEVICE_GROUP("devGrp"),
	DEVICE_MAC("devMAC"),
	DEVICE_NAME("devName"),
	DEVICE_SERIAL("devSN"),
	FIRMWARE_NAME("fwName"),
	FIRMWARE_OVERWRITE_NUMBER("fwOverwriteNumber"),
	FIRMWARE_SIZE("fwSize"),
	METADATA_SOURCE("metadataSource"),
	METADATA_VERSION("metadataVersion");
	
	private String name;
	
	private FirmwareHeader(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
}
