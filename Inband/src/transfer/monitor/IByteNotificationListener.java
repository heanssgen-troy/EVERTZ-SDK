package transfer.monitor;

import transfer.datagram.State;

public interface IByteNotificationListener {

	boolean hasRemainingBytes();
	int getRemainingBytes();
	State getState();

}
