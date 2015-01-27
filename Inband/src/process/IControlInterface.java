package process;

import data.Packet;

public interface IControlInterface {
	public Packet initializationPacket();
	public Packet progressPacket();
	public Packet endingPacket();
}
