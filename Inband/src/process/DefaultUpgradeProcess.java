package process;

import data.Packet;

public class DefaultUpgradeProcess implements IControlInterface {
	@Override
	public Packet initializationPacket() {
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
	
	public DefaultUpgradeProcess(){

	}
}
