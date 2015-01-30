package process;

import java.io.File;
import java.io.IOException;

import transfer.connection.SocketHandler;
import metadata.HeaderContainer;
import metadata.HeaderEntry;
import data.Packet;
import data.formatter.FirmwareHeaderFormatter;
import data.formatter.MetadataHeaderFormatter;
import data.formatter.Abstract.IPacketHeader;
import data.header.FirmwareHeader;
import data.header.MetadataHeader;
import data.util.Packager;


public class DefaultUpgradeProcess  {

	public static void start(Object ... args) throws IOException{
		int counter = 0;
		for(DefaultUpgradeEnumeration passThrough : DefaultUpgradeEnumeration.values()){
			passThrough.setValue(args[counter]);
			counter++;
		}
		Packet metadataHeader = DefaultUpgradeProcess.determineMetadataHeader();
		Packet firmwareHeader = DefaultUpgradeProcess.determineFirmwareHeader();
		Packet upgradeRequest = DefaultUpgradeProcess.determineUpgradePacket();
		
		SocketHandler handler = new SocketHandler();
		handler.initializeSocket(false, (String)DefaultUpgradeEnumeration.INJECT_IP.getValue(), 9669);
		handler.send(Packager.Package(new File((String)DefaultUpgradeEnumeration.FIRMWARE_FILEPATH.getValue()), 1024), metadataHeader, firmwareHeader, upgradeRequest, 200000);
	}
	
	private static Packet determineMetadataHeader(){
		HeaderContainer<IPacketHeader> container = new HeaderContainer<IPacketHeader>(); 
		container.putValue(MetadataHeader.HEADER_VERSION, new HeaderEntry(1,1));
		container.putValue(MetadataHeader.HEADER_SIZE, new HeaderEntry(64,1));
		container.putValue(MetadataHeader.PROGRAM_REFERENCE, new HeaderEntry(1,1));
		container.putValue(MetadataHeader.ASSOCIATION_TYPE, new HeaderEntry(DefaultUpgradeEnumeration.ASSOCIATION_TYPE.getValue(),1));
		container.putValue(MetadataHeader.ASSOCIATION_SOURCE, new HeaderEntry(DefaultUpgradeEnumeration.ASSOCIATION_ADDRESS.getValue() + ":" + DefaultUpgradeEnumeration.ASSOCIATION_PORT.getValue(),6));
		container.putValue(MetadataHeader.ASSOCIATION_PROGRAM, new HeaderEntry(DefaultUpgradeEnumeration.ASSOCIATION_PROGRAM.getValue(),2));
		container.putValue(MetadataHeader.ASSOCIATION_PID, new HeaderEntry(DefaultUpgradeEnumeration.ASSOCIATION_PID.getValue(),2));
		container.putValue(MetadataHeader.INJECT_TIME, new HeaderEntry(DefaultUpgradeEnumeration.DELAY_HOURS.getValue() + ":" + DefaultUpgradeEnumeration.DELAY_MINUTES.getValue() + ":" + DefaultUpgradeEnumeration.DELAY_SECONDS.getValue()+"-" +1 ,4));
		container.putValue(MetadataHeader.PAYLOAD_LENGTH, new HeaderEntry(DefaultUpgradeEnumeration.ASSOCIATION_PID.getValue(),4));
		container.putValue(MetadataHeader.PTS_MODE, new HeaderEntry(5,1));
		container.putValue(MetadataHeader.PTS_OFFSET, new HeaderEntry(2,4));
		
		MetadataHeaderFormatter formatter = new MetadataHeaderFormatter(64,"v1",container);
		return formatter.doFormatting();
	}
	private static Packet determineFirmwareHeader(){
		File f = new File((String)DefaultUpgradeEnumeration.FIRMWARE_FILEPATH.getValue());
		HeaderContainer<IPacketHeader> container = new HeaderContainer<IPacketHeader>();
		container.putValue(FirmwareHeader.DEVICE_GROUP, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_GROUP.getValue(),1));
		container.putValue(FirmwareHeader.DEVICE_MAC, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_MAC.getValue(),1));
		container.putValue(FirmwareHeader.DEVICE_SERIAL, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_SERIAL.getValue(),1));
		container.putValue(FirmwareHeader.DEVICE_NAME, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_NAME.getValue(),1));
		container.putValue(FirmwareHeader.FIRMWARE_NAME, new HeaderEntry(f.getName(),1));
		container.putValue(FirmwareHeader.FIRMWARE_OVERWRITE_NUMBER, new HeaderEntry(DefaultUpgradeEnumeration.OVERWRITE_NUMBER,1));
		container.putValue(FirmwareHeader.FIRMWARE_SIZE, new HeaderEntry(f.length(),1));
		container.putValue(FirmwareHeader.METADATA_SOURCE, new HeaderEntry(DefaultUpgradeEnumeration.METADATA_SOURCE,1));
		container.putValue(FirmwareHeader.METADATA_VERSION, new HeaderEntry("v01",1));
		container.putValue(FirmwareHeader.DEVICE_GROUP, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_GROUP,1));
		FirmwareHeaderFormatter formatter = new FirmwareHeaderFormatter(container);
		return formatter.doFormatting("@","X$EVTZFWIMG");
	}
	private static Packet determineUpgradePacket(){
		File f = new File((String)DefaultUpgradeEnumeration.FIRMWARE_FILEPATH.getValue());
		HeaderContainer<IPacketHeader> container = new HeaderContainer<IPacketHeader>();
		container.putValue(FirmwareHeader.DEVICE_GROUP, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_GROUP.getValue(),1));
		container.putValue(FirmwareHeader.DEVICE_MAC, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_MAC.getValue(),1));
		container.putValue(FirmwareHeader.DEVICE_SERIAL, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_SERIAL.getValue(),1));
		container.putValue(FirmwareHeader.DEVICE_NAME, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_NAME.getValue(),1));
		container.putValue(FirmwareHeader.FIRMWARE_NAME, new HeaderEntry(f.getName(),1));
		container.putValue(FirmwareHeader.METADATA_SOURCE, new HeaderEntry(DefaultUpgradeEnumeration.METADATA_SOURCE.getValue(),1));
		container.putValue(FirmwareHeader.METADATA_VERSION, new HeaderEntry("v01",1));
		container.putValue(FirmwareHeader.DEVICE_GROUP, new HeaderEntry(DefaultUpgradeEnumeration.DEVICE_GROUP.getValue(),1));
		FirmwareHeaderFormatter formatter = new FirmwareHeaderFormatter(container);
		return formatter.doFormatting("@","XEVTZFWIMG");
	}
	private enum DefaultUpgradeEnumeration{
		INJECT_IP,
		ASSOCIATION_ADDRESS,
		ASSOCIATION_PORT,
		ASSOCIATION_TYPE,
		ASSOCIATION_PID,
		ASSOCIATION_PROGRAM,
		FIRMWARE_FILEPATH,
		OVERWRITE_NUMBER,
		DELAY_HOURS,
		DELAY_MINUTES,
		DELAY_SECONDS,
		METADATA_STRING,
		METADATA_SOURCE,
		NUMBER_ITERATIONS,
		PROCESS_NAME,
		PROCESS_ID,
		DEVICE_NAME,
		DEVICE_MAC,
		DEVICE_SERIAL,
		DEVICE_IP,
		DEVICE_GROUP;
		
		private Object value = "";
		public void setValue(Object value){
			this.value = value;
		}
		public Object getValue(){
			return this.value;
		}
	}
	public static void main(String [] args) throws IOException{
		/*
		 * String - Device to send to
		 * String - Association IP
		 * Int - Association Port
		 * Int - Association Type
		 * Int - Association PID
		 * Int - Association Program
		 * String - Firmware Filepath
		 * Int - Overwrite Number
		 * Int - Delay (Hours)
		 * Int - Delay (Minutes)
		 * Int - Delay (Seconds)
		 * String - Metadata String (Always empty)
		 * String - Metadata Source (IP)
		 * Int - Number of times to do this
		 * String - Process Name
		 * Int - Process ID
		 * String - Device Name
		 * String - Device MAC
		 * Int - Device Serial
		 * String - Device IP
		 * String - Device Group
		 */
		DefaultUpgradeProcess.start("localhost","127.0.0.1",65535,1,17,1,"C:\\Users\\hh14wo\\Downloads\\3080MUX_DMXE_metadata.pdf",1,0,0,0,"","foobar",1,"hey",1,"temp","AB",12,"172.0.0.1","n");
	}
}
