package data;

public class Packet {
	private int segmentSize;
	private int segmentNumber;
	private byte[] data;
	
	public Packet(int segSize, int segNum, byte[] data){
		this.segmentSize = segSize;
		this.segmentNumber = segNum;
		this.data = data;
	}
	public Packet(byte[] data){
		this.data = data;
	}

	public int getSegmentSize() {
		return segmentSize;
	}

	public int getSegmentNumber() {
		return segmentNumber;
	}

	public byte[] getData() {
		return data;
	}
}
