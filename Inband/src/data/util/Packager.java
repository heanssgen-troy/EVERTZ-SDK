package data.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class Packager {
	
	/**
	 * This method returns the array representation of the target file as UDP datagram packets. The packet size must follow
	 * the convention of {@code Size % 8 = 0} to allow for language-independent data transfer.
	 * @param file - The file to package into DatagramPackets.
	 * @param packetSize - The chunk size of each packet.
	 * @return DatagramPacket[] - The array containing the packets to be sent.
	 * @throws FileNotFoundException - If the source file cannot be read.
	 * @throws IOException - If there is an issue with the FileInputStream being created.
	 */
	public static DatagramPacket[] Package (File file, Integer packetSize) throws FileNotFoundException, IOException{
		final List<DatagramPacket> packetList = new ArrayList<DatagramPacket>();
		if(packetSize % 8 > 0){
			throw new RuntimeException("Packet size mod 8 must yield 0 for valid packet interfaces");
		}
		byte[] payload = IOUtils.toByteArray(new FileInputStream(file));
		byte[][] splitPayload = new byte[(int)Math.ceil((double)payload.length/(double)packetSize)][];
		for(int i = 0; i < splitPayload.length; i++){
			DatagramPacket packet = null;
			if((i+1) * packetSize > payload.length){
				splitPayload[i] = Arrays.copyOfRange(payload, i*packetSize, payload.length);
				packet = new DatagramPacket(splitPayload[i], splitPayload[i].length);
			}else{
				splitPayload[i] = Arrays.copyOfRange(payload, i * packetSize, (i+1) * packetSize);
				packet = new DatagramPacket(splitPayload[i],packetSize);
			}
			packetList.add(packet);
		}
		return packetList.toArray(new DatagramPacket[0]);
	}
}
