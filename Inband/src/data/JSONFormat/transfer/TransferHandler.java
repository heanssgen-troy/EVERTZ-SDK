package data.JSONFormat.transfer;

import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.util.concurrent.RateLimiter;

import data.JSONFormat.SendablePacket;

public class TransferHandler extends Thread {
	private Socket socket;
	private transfer.datagram.State state;
	private RateLimiter limiter = RateLimiter.create(200000);
	private boolean canPerformAction = false;
	private SendablePacket packet;
	private int remaining;
	private ReentrantLock transferLock = new ReentrantLock();
	public TransferHandler(SendablePacket packet){
		this.packet = packet;
	}
	public void setSocket(Socket s){
		socket = s;
	}
	public int getRemaining(){
		return this.remaining;
	}
	public transfer.datagram.State getTransferState(){
		return this.state;
	}
	public ReentrantLock getTransferLock(){
		return this.transferLock;
	}
	public void run(){
		if(this.canPerformAction){
			this.state = transfer.datagram.State.OPEN;
			this.remaining = packet.toData().length;
			transferLock.lock();
			try{
				this.state = transfer.datagram.State.TRANSFER;
				for(byte b : packet.toData()){
					if(this.isInterrupted()){
						transferLock.unlock();
					}else{
						transferLock.lock();
						limiter.acquire();
						socket.getOutputStream().write(b);
					}
					transferLock.unlock();
				}
				socket.close();
				
			}catch(Exception e){
				this.state = transfer.datagram.State.ERROR;
			}
		}
	}
	public void resumeTransfer() {
		this.canPerformAction = true;
		this.state = transfer.datagram.State.OPEN;
		this.transferLock.lock();
	}
	public void interuptTransfer() {
		this.canPerformAction = false;
		this.interrupt();
		this.state = transfer.datagram.State.CLOSED;
		this.transferLock.unlock();
	}

}
