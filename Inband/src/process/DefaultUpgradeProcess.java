package process;

import java.io.File;
import java.io.IOException;

import metadata.HeaderContainer;
import metadata.HeaderEntry;
import transfer.connection.SocketHandler;
import data.Packet;
import data.formatter.FirmwareHeaderFormatter;
import data.formatter.Abstract.IPacketHeader;
import data.header.FirmwareHeader;
import data.util.Packager;


public class DefaultUpgradeProcess  {

	public static void start(Object ... args) throws IOException{
		int counter = 0;
		for(DefaultUpgradeEnumeration passThrough : DefaultUpgradeEnumeration.values()){
			passThrough.setValue(args[counter]);
			counter++;
		}
		Packet firmwareHeader = DefaultUpgradeProcess.determineFirmwareHeader();
		Packet upgradeRequest = DefaultUpgradeProcess.determineUpgradePacket();
		
		SocketHandler handler = new SocketHandler();
		handler.initializeSocket(false, (String)DefaultUpgradeEnumeration.INJECT_IP.getValue(), 9669);
		handler.send(Packager.Package(new File((String)DefaultUpgradeEnumeration.FIRMWARE_FILEPATH.getValue()), 1024), null, firmwareHeader, upgradeRequest, 200000);
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
