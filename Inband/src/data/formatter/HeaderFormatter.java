package data.formatter;

import metadata.MetadataTag;
import data.Packet;
import data.util.Packager;

public class HeaderFormatter extends AFormatter{
	
	public HeaderFormatter(int headerSize, String version, MetadataTag tag){
		super.setBytes(headerSize);
		super.setVersion(version);
		super.setHeaderPacket(tag);
	}	
	@Override
	public Packet doFormatting() {
		byte[] header = new byte[64];
		header[0] = Packager.convertIntToByte((Integer)super.getHeaderPacket().getValue(MetadataTag.Metadata.HEADER_VERSION));
		header[1] = Packager.convertIntToByte((Integer)super.getHeaderPacket().getValue(MetadataTag.Metadata.HEADER_SIZE));
		header[2] = Packager.convertIntToByte((Integer)super.getHeaderPacket().getValue(MetadataTag.Metadata.PROGRAM_REFERENCE));
		header[3] = Packager.convertIntToByte((Integer)super.getHeaderPacket().getValue(MetadataTag.Metadata.ASSOCIATION_TYPE));
		
		int[] ipValue= determineAssociationSource(((String)super.getHeaderPacket().getValue(MetadataTag.Metadata.ASSOCIATION_SOURCE)));
		for(int i = 4; i < 14; i ++){
			header[i] = 0;
		}
		//header[14] = ipBlocks[]
		return null;
	}
	public int[] determineAssociationSource(String assocSource){
		String[] values = assocSource.split("[.:]");
		int[] returnables = new int[values.length];
		String hexInterface = "";
		for(int i =0; i < values.length; i++){
			hexInterface +=Integer.toHexString(Integer.parseInt(values[i]));
		}
		System.out.println(hexInterface.toUpperCase());
		int integerInterface = Integer.parseInt(hexInterface.toUpperCase(),16);
		String finalHexInterface = ""+integerInterface;
		for(int i = 0; i <returnables.length; i ++){
			returnables[i] = Integer.parseInt(finalHexInterface.substring(i*3, (i+1) * 3 - 1));
		}
		
		return returnables;
	}
	
	public static void main(String [] args){
		HeaderFormatter formatter = new HeaderFormatter(64,null,null);
		int[] temp = formatter.determineAssociationSource("233.0.0.1:1234");
		for(int c  : temp){
			System.out.println(c);
		}
	}
	
}