package transfer.connection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import metadata.HeaderContainer;
import metadata.HeaderEntry;

import com.google.common.util.concurrent.RateLimiter;

import data.Packet;
import data.formatter.MetadataHeaderFormatter;
import data.formatter.Abstract.IPacketHeader;
import data.header.MetadataHeader;

public class TransferAction extends Thread {
	private boolean canPerformAction = false;
	private DatagramPacket[] payload;
	private Socket socket;
	private Integer remaining;
	private transfer.datagram.State state;
	private ReentrantLock transferLock = new ReentrantLock();
	private RateLimiter limiter;
	private HeaderContainer<IPacketHeader> globalHeaderPacket;
	private Packet requestTransferPacket;
	private Packet firmwarePacket;
	private MetadataHeaderFormatter formatter;
	/**
	 * Protected constructor to prevent out-of-band subclassing of the Transfer
	 * Action.
	 * 
	 */
	protected TransferAction() {

	}

	/**
	 * Initialization method to set up the Transfer payload on a particular
	 * socket. This method must be called before the thread has been started.
	 * Any in-band subclassing of this method must make a call to
	 * super.initPayload() before any additional functionality may occur.
	 * Failure to do so will disable the rate limiting functionality, and also
	 * perform in an undefined manner.
	 * 
	 * @param socket
	 *            - The socket on which the transfer will be performed.
	 * @param payload
	 *            - The payload to be transferred, contained in a DatagramPacket
	 *            arrray.
	 * @param rateLimit
	 *            - The rate in which the transfer may be performed, calculated
	 *            in bytes per second.
	 */
	public void initPayload(Socket socket, DatagramPacket[] payload, HeaderContainer<IPacketHeader> header,Packet requestPacket,Packet firmwarePacket, Integer rateLimit) {
		if (validatePayload(payload)) {
			this.payload = payload;
			this.globalHeaderPacket = header;
			this.requestTransferPacket = requestPacket;
			this.firmwarePacket = firmwarePacket;
			this.limiter = RateLimiter.create(rateLimit);
			this.canPerformAction = true;
			this.socket = socket;
			this.formatter = new MetadataHeaderFormatter(64,"v1",globalHeaderPacket);
		}
		this.state = transfer.datagram.State.READY;
	}

	public Packet getGlobalHeaderPacket() {
		formatter = new MetadataHeaderFormatter(64,"v1",globalHeaderPacket);
		return formatter.doFormatting();
	}

	public void setGlobalHeaderPacket(HeaderContainer<IPacketHeader> globalHeaderPacket) {
		this.globalHeaderPacket = globalHeaderPacket;
	}

	public Packet getEndTransferPacket() {
		return firmwarePacket;
	}

	public void setEndTransferPacket(Packet endTransferPacket) {
		this.firmwarePacket = endTransferPacket;
	}

	/**
	 * Returns the number of packets that are remaining in the queue.
	 * 
	 * @return The number of packets remaining in the transfer queue.
	 */
	public int getRemainingPackets() {
		return remaining;
	}

	/**
	 * Acquires the operational lock for this particular transfer thread. Since
	 * the transfer is organized as a packet-by-packet transfer that requires
	 * the lock, any mutation to the open state of this lock will affect the
	 * transfer. Most notably, if the lock() method is called on the operational
	 * lock, any and all transfers will be halted and the thread will deadlock
	 * until the lock is released again via the unlock() method.
	 * 
	 * @return The operational lock of this transfer thread.
	 */
	public ReentrantLock getTransferLock() {
		return transferLock;
	}

	/**
	 * Returns the operational state of this transfer thread. Valid states are:
	 * 
	 * @State Ready - The thread has been set up and is ready to be started.
	 * @State Open - The thread has recently been unlocked and transfer is
	 *        waiting to be resumed.
	 * @State Closed - The thread has been locked and transfer is waiting the
	 *        unlock method.
	 * @State Transfer - The thread is currently actively transferring data.
	 * @State Error - The last packet that attempted a send threw an error. An
	 *        error state means that the transfer did not complete.
	 * @return The state of the transfer.
	 */
	public transfer.datagram.State getTransferState() {
		return this.state;
	}

	/**
	 * Private helper method to ensure that no packets are null.
	 * 
	 * @param payload
	 *            - The payload to be checked for fidelity.
	 * @return boolean - A boolean representing if the check was successful or
	 *         not.
	 */
	private boolean validatePayload(DatagramPacket[] payload) {
		for (DatagramPacket packet : payload) {
			if (packet == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method to interrupt thread transfer. Will only successfully interrupt if
	 * the lock is acquired by a thread other than the executing thread locks
	 * the lock.
	 */
	public void interuptTransfer() {
		this.canPerformAction = false;
		this.interrupt();
		this.state = transfer.datagram.State.CLOSED;
		this.transferLock.unlock();
	}

	/**
	 * Method to resume transfer. The thread currently holding the lock must
	 * release the lock before making this call.
	 */
	public void resumeTransfer() {
		this.canPerformAction = true;
		this.state = transfer.datagram.State.OPEN;
		this.transferLock.lock();
	}
	public void destroy(){
		this.interrupt();
		try {
			this.socket.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			this.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run() This method will perform the socket transfer.
	 */
	public void run() {
		if (this.canPerformAction) {
			List<DatagramPacket> payloadAsList = new ArrayList<DatagramPacket>(Arrays.asList(payload));
			
			this.remaining = payloadAsList.size();
			transferLock.lock();
			try {
				socket.getOutputStream().write(new byte[0]);
				globalHeaderPacket.putValue(MetadataHeader.PAYLOAD_LENGTH, new HeaderEntry(requestTransferPacket.getData().length + 64,4));
				socket.getOutputStream().write(formatter.doFormatting(globalHeaderPacket).getData());
				
				ByteBuffer upgradeBuffer = ByteBuffer.allocate(formatter.doFormatting(globalHeaderPacket).getData().length + requestTransferPacket.getData().length);
				upgradeBuffer.put(formatter.doFormatting(globalHeaderPacket).getData());
				upgradeBuffer.put(requestTransferPacket.getData());
				socket.getOutputStream().write(upgradeBuffer.array());
				
				globalHeaderPacket.putValue(MetadataHeader.PAYLOAD_LENGTH, new HeaderEntry(firmwarePacket.getData().length + 64,4));
				ByteBuffer buffer = ByteBuffer.allocate(formatter.doFormatting(globalHeaderPacket).getData().length + firmwarePacket.getData().length);
				buffer.put(formatter.doFormatting(globalHeaderPacket).getData());
				buffer.put(firmwarePacket.getData());
				socket.getOutputStream().write(buffer.array());
				

			} catch (IOException e1) {
				this.canPerformAction = false;
				e1.printStackTrace();
			}
			while (payloadAsList.size() > 0 && this.canPerformAction) {
				this.state = transfer.datagram.State.TRANSFER;

				transferLock.lock();
				DatagramPacket packet = payloadAsList.get(0);
				limiter.acquire(packet.getLength());
				try {
					if (this.isInterrupted()) {
						transferLock.unlock();
					} else {
						globalHeaderPacket.putValue(MetadataHeader.PAYLOAD_LENGTH, new HeaderEntry(packet.getData().length,4));
						ByteBuffer buffer = ByteBuffer.allocate(packet.getData().length + formatter.doFormatting(globalHeaderPacket).getData().length);
						buffer.put(formatter.doFormatting(globalHeaderPacket).getData());
						buffer.put(packet.getData());
						this.socket.getOutputStream().write(buffer.array());;
						this.remaining -= 1;
						payloadAsList.remove(packet);
					}
				} catch (IOException e) {
					e.printStackTrace();
					this.state = transfer.datagram.State.ERROR;
					transferLock.unlock();
					return;
				}
			}
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Packet getRequestTransferPacket() {
		return requestTransferPacket;
	}

	public void setRequestTransferPacket(Packet requestTransferPacket) {
		this.requestTransferPacket = requestTransferPacket;
	}
}
