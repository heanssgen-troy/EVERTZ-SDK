package process;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import metadata.MetadataTag;
import data.Packet;

public class DefaultUpgradeProcess implements IControlInterface {
	private HashMap<String,Object[]> valueList;
	@Override
	public Packet initializationPacket() {
		List<MetadataTag> initializationMetadata = new LinkedList<MetadataTag>();
		initializationMetadata.add(new MetadataTag("fwImage",valueList.get("fwImage")));
		initializationMetadata.add(new MetadataTag("fwSize",valueList.get("fwSize")));
		initializationMetadata.add(new MetadataTag("fwOverwriteNumber",valueList.get("fwOverwriteNumber")));
		return new Packet(null);
	}

	@Override
	public Packet progressPacket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Packet endingPacket() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DefaultUpgradeProcess(HashMap<String,Object[]> headerData){
		this.valueList = headerData;
	}
}
