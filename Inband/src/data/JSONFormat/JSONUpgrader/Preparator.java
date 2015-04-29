package data.JSONFormat.JSONUpgrader;

import java.util.HashMap;

import data.JSONFormat.JSONMetadata;
import data.JSONFormat.format.MetadataFormat;

public class Preparator {
	public static final String HEADER_NAME = "MetadataHeader";
	public static final String UPGRADE_NAME = "XEVTFWIMG";
	public static final String FIRMWARE = "X$EVTFWIMG";
	public static HashMap<String,JSONMetadata> prepare(JSONMetadata MetadataHeader, JSONMetadata FirmwareHeader, JSONMetadata Upgrade){
		HashMap<String,JSONMetadata> map = new HashMap<String,JSONMetadata>();
		
		MetadataHeader.setNonJSONFormat(new MetadataFormat());
		MetadataHeader.flagUseJSONFormat(false);
		FirmwareHeader.setName(Preparator.FIRMWARE);
		Upgrade.setName(Preparator.UPGRADE_NAME);
		MetadataHeader.setName(Preparator.HEADER_NAME);
		map.put(Preparator.HEADER_NAME, MetadataHeader);
		map.put(Preparator.UPGRADE_NAME, Upgrade);
		map.put(Preparator.FIRMWARE, FirmwareHeader);
		
		return map;
	}
}
