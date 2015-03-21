package data.JSONFormat.JSONUpgrader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import transfer.connection.SocketHandler;
import data.JSONFormat.JSONMetadata;
import data.JSONFormat.SendablePacket;
import data.util.Packager;

public class Upgrade {
	
	public static SendablePacket prepareUpgrade(JSONMetadata metadataPacket,JSONMetadata firmwarePacket,JSONMetadata upgradePacket, File firmwareFile){
		SendablePacket packet = new SendablePacket();
		packet.addData((byte)0);
		metadataPacket.putValue("Metadata_Header_Payload_Length", upgradePacket.JSONtoData().length + metadataPacket.JSONtoData().length);
		packet.addSendableHeader(metadataPacket);
		packet.addSendableHeader(upgradePacket);
		metadataPacket.putValue("Metadata_Header_Payload_Length", firmwarePacket.JSONtoData().length + metadataPacket.JSONtoData().length);
		packet.addSendableHeader(metadataPacket);
		packet.addSendableHeader(firmwarePacket);
		DatagramPacket[] packets = Upgrade.splitFirmwareFile(firmwareFile);
		for(DatagramPacket p : packets){
			
			ByteBuffer b = ByteBuffer.allocate(p.getData().length + metadataPacket.JSONtoData().length);
			metadataPacket.putValue("Metadata_Header_Payload_Length", b.array().length);
			b.put(metadataPacket.JSONtoData());
			b.put(p.getData());
			p.setData(b.array());
			packet.addData(p.getData());
		}
		
		return packet;
		
	}
	private static DatagramPacket[] splitFirmwareFile(File file){
		try {
			return Packager.Package(file, 1024-64);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}
	
	private static boolean doUpgrade(Object ... args){
		JSONMetadata metadata = new JSONMetadata("metadata");
		metadata.putValue("Metadata_Header_Version", 1);
		metadata.putValue("Metadata_Header_Length", 64);
		metadata.putValue("Metadata_Header_Program_Reference", args[2]);
		metadata.putValue("Metadata_Header_Association_Type", args[3]);
		metadata.putValue("Metadata_Header_Association_Source", args[4]);
		metadata.putValue("Metadata_Header_Association_Program", args[5]);
		metadata.putValue("Metadata_Header_Association_PID", args[6]);
		metadata.putValue("Metadata_Header_Inject_Time", args[7]);
		metadata.putValue("Metadata_Header_Payload_Length", args[8]);
		metadata.putValue("Metadata_Header_PTS_MODE", args[9]);
		metadata.putValue("Metadata_Header_PTS_OFFSET", args[10]);
		JSONMetadata firmwareData = new JSONMetadata("firmware");
		firmwareData.putValue("devGrp", args[11]);
		firmwareData.putValue("devMAC", args[12]);
		firmwareData.putValue("devSn", args[13]);
		firmwareData.putValue("devName", args[14]);
		firmwareData.putValue("fwName", ((String)args[15]).substring(((String)args[13]).lastIndexOf(File.pathSeparatorChar)));
		firmwareData.putValue("fwOverwriteNumber", args[16]);
		firmwareData.putValue("fwSize", args[17]);
		firmwareData.putValue("metadataSource", args[18]);
		firmwareData.putValue("metadataVersion", "v01");
		JSONMetadata upgradeData = new JSONMetadata("upgrade");
		upgradeData.putValue("devGrp", args[11]);
		upgradeData.putValue("devMAC", args[12]);
		upgradeData.putValue("devSn", args[13]);
		upgradeData.putValue("devName", args[14]);
		upgradeData.putValue("fwName", args[15]);
		upgradeData.putValue("metadataSource", args[18]);
		upgradeData.putValue("metadataVersion", "v01");
		Preparator.prepare(metadata, firmwareData, upgradeData);
		SendablePacket packet = Upgrade.prepareUpgrade(metadata, firmwareData, upgradeData, new File((String)args[13]));
		SocketHandler socket = new SocketHandler();
		try {
			socket.initializeSocket(false, (String)args[1], 9669);
			socket.send(packet);
			return true;
		} catch (IOException e) {
			return false;
		}
		
	}
	public static boolean doUpgrade(String processName, String injectIP, int programReference, int associationType, String associationSource,int associationProgram, int associationPID, String injectTime, int payloadLength,
			int ptsMode, int ptsOffset, String devGroup, String devMac, String devSerial, String devName, String fwPath, int overwriteNumber, int firmwareSize, String metadataSource){
			return doUpgrade(processName, injectIP, programReference,associationType, associationSource,associationProgram,associationPID, injectTime,payloadLength,ptsMode,ptsOffset,devGroup,devMac,devSerial,devName,fwPath,overwriteNumber,firmwareSize,metadataSource);
			
	}
	
}
