package data.JSONFormat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class SendablePacket {
	
	private LinkedList<byte[]> sendableData = new LinkedList<byte[]>();
	public void addSendableHeader(Object o){
		Method m = null;
		try {
			m = o.getClass().getMethod("toData");
		} catch (NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}
		if(m != null){
			if(m.getReturnType() == byte[].class){
				try {
					sendableData.add((byte[]) m.invoke(o));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void addData(byte ... b){
		sendableData.add(b);
	}

	public byte[] toData(){
		int dataSize = 0;
		for(byte[] b : sendableData){
			dataSize += b.length;
		}
		ByteBuffer buffer = ByteBuffer.allocate(dataSize);
		for(byte[] b : sendableData){
			buffer.put(b);
		}
		return buffer.array();
	}
}
