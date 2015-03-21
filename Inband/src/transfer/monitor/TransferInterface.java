package transfer.monitor;

import java.util.concurrent.locks.ReentrantLock;

import transfer.datagram.State;
import data.JSONFormat.transfer.TransferHandler;

/**
 * @author Troy Heanssgen
 *
 */
public class TransferInterface implements IByteNotificationListener {
	private TransferHandler action;
	private ReentrantLock lock;

	/**
	 * @param action
	 *            Attaches the listener to the particular action.
	 * @return True if the listener does not already have a listener. Function
	 *         has no effect if a listener is already instated.
	 */
	public boolean attachListener(TransferHandler action) {
		if (this.action == null) {
			this.action = action;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see transfer.monitor.IByteNotificationListener#hasRemainingBytes()
	 * Method that returns true IFF the transfer is in progress and has bytes
	 * remaining.
	 */
	@Override
	public boolean hasRemainingBytes() {
		return action.getRemaining() > 0;
	}

	/**
	 * Immediately stops the transfer and kills the transfer thread. This action
	 * is extremely dangerous and should be used to abort a transfer
	 * immediately.
	 */
	@SuppressWarnings("deprecation")
	public void endTransfer() {
		action.destroy();
		action.stop();
	}

	/**
	 * Safer alternative to ending the transfer. This method will halt the
	 * transfer and acquire the operational lock, after which terminating the
	 * thread becomes a matter of calling the destroy() method.
	 * 
	 */
	public void stopTransfer() {
		action.interuptTransfer();
		this.lock = action.getTransferLock();
		this.lock.lock();
	}

	/**
	 * Resumes the transfer IFF the lock is currently held by the TransferInterface. If this is the case, it means that
	 * the current transfer thread is paused and will promptly resume.
	 * 
	 */
	public void resumeTransfer() {
		if (this.lock.isHeldByCurrentThread()) {
			this.lock.unlock();
			action.resumeTransfer();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see transfer.monitor.IByteNotificationListener#getState()
	 * Return the state of the transfer handler.
	 */
	@Override
	public State getState() {
		return action.getTransferState();
	}

}
