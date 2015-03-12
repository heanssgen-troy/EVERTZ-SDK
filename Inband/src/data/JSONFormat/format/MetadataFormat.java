package data.JSONFormat.format;

import java.nio.ByteBuffer;

import org.json.JSONException;

import data.JSONFormat.JSONMetadata;

public class MetadataFormat extends Format{

	@Override
	public ByteBuffer format(JSONMetadata jsonMetadata) {
		ByteBuffer b;
		b = ByteBuffer.allocate(64);
		try {
			b.put((byte)jsonMetadata.getData("Metadata_Header_Version").getInt("Metadata_Header_Version"));
			b.put((byte)jsonMetadata.getData("Metadata_Header_Length").getInt("Metadata_Header_Length"));
			b.put((byte)jsonMetadata.getData("Metadata_Header_Program_Reference").getInt("Metadata_Header_Program_Reference"));
			b.put((byte)jsonMetadata.getData("Metadata_Header_Association_Type").getInt("Metadata_Header_Association_Type"));
			b.put(new byte[10]);
			for(String i : determineAssociationSource(jsonMetadata.getData("Metadata_Header_Association_Source").getString("Metadata_Header_Association_Source"))){
				b.put((byte)Long.parseLong(i,16));
			}
			b.putShort((short)jsonMetadata.getData("Metadata_Header_Association_Program").getInt("Metadata_Header_Association_Program"));
			b.putShort((short)jsonMetadata.getData("Metadata_Header_Association_PID").getInt("Metadata_Header_Association_PID"));
			b.put((byte)0);
			for(int i : determineInjectTime(jsonMetadata.getData("Metadata_Header_Inject_Time").getString("Metadata_Header_Inject_Time"))){
				b.put((byte)i);
			}
			b.putInt(jsonMetadata.getData("Metadata_Header_Payload_Length").getInt("Metadata_Header_Payload_Length"));
			b.put((byte)jsonMetadata.getData("Metadata_Header_PTS_MODE").getInt("Metadata_Header_PTS_MODE"));
			b.putInt(jsonMetadata.getData("Metadata_Header_PTS_OFFSET").getInt("Metadata_Header_PTS_OFFSET"));
			b.put(new byte[26]);
			return b;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public String[] determineAssociationSource(String assocSource) {
		String[] returnable = new String[6];
		
		String[] segments = assocSource.split("[.:]");
		String hexString = "";
		for(String s : segments){
			s = Integer.toHexString(Integer.parseInt(s));
			for(int i = 0; i < s.length() % 2; i++){
				s = "0" + s;
			}
			hexString += s;
		}
		hexString = hexString.toUpperCase();
		for(int i = 0; i < 6; i ++){
			returnable[i] = hexString.substring(0, 2);
			hexString = hexString.substring(2);
			
		}
		
		return returnable;
	}

	private int[] determineInjectTime(String s) {
		String[] integerInterface = s.split("[:-]");
		int[] returnableIntegers = new int[integerInterface.length];

		for (int i = 0; i < integerInterface.length; i++) {
			returnableIntegers[i] = Integer.parseInt(integerInterface[i]);
		}
		int hour = returnableIntegers[0];
		int minute = returnableIntegers[1];
		int second = returnableIntegers[2];
		int frame = returnableIntegers[3];

		if (hour <= 23 && minute <= 59 && second <= 59 && frame <= 29) {
			return returnableIntegers;
		} else {
			return null;
		}
	}
	public static void main(String [] args){
		MetadataFormat format = new MetadataFormat();
		ByteBuffer b = null;
		
		JSONMetadata data = new JSONMetadata("X$EVTFWIMG");
		data.putValue("Metadata_Header_Version", 1);
		data.flagIsApplyLocal("Metadata_Header_Version",true);
		data.putValue("Metadata_Header_Length", 64);
		data.putValue("Metadata_Header_Program_Reference", 1);
		data.putValue("Metadata_Header_Association_Type", 1);
		data.putValue("Metadata_Header_Association_Source", "233.0.0.1:1234");
		data.putValue("Metadata_Header_Association_Program", "224");
		data.putValue("Metadata_Header_Association_PID", "61");
		data.putValue("Metadata_Header_Inject_Time", "11:10:21-16");
		data.putValue("Metadata_Header_Payload_Length", 1024);
		data.putValue("Metadata_Header_PTS_MODE", 1);
		data.putValue("Metadata_Header_PTS_OFFSET", 1);	
		b = format.format(data);
		
		for(byte character : data.JSONtoData()){
			System.out.print((char)character);
		}
	}
}
