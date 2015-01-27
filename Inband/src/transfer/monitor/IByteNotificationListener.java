package transfer.monitor;

import transfer.datagram.State;

public interface IByteNotificationListener {

	boolean hasRemainingBytes();

	State getState();

}
