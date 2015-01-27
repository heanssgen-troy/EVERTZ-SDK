package transfer.connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import data.util.Packager;
import transfer.monitor.TransferInterface;

/**
 * @author Troy Heanssgen
 *
 */
public class SocketHandler {
	/** The socket on which the communication will take place.
	 * 
	 */
	private Socket socket;
	/** The connection metadata of the source computer.
	 * 
	 */
	private ConnectionMetadata sourceMetadata;
	/** The connection metadata of the destination computer.
	 * 
	 */
	private ConnectionMetadata destinationMetadata;
	/** No-arg constructor to allow future subclasses to only over-ride a no-arg constructor.
	 * 
	 */
	public SocketHandler() {
	}

	/**  Sends an entire file (payload) to a destination address. Traffic type consists of shaped UDP traffic, and therefore
	 * there is no guarantee that all packets will successfully make it to destination address. Due to requirement for language-
	 * Independent code, the chunk size parameter must follow the convention of {@code Size % 8 = 0}.
	 * 
	 * @since Since the rate limit is an absolute limit (in bytes), the chunk size must be lower than the rate limit
	 * and also follow the convention of {@code Size % 8 = 0}. Failure to abide by these limitations results in undefined
	 * operation.
	 * 
	 * @param payload - The file to be sent as UDP packets.
	 * @param chunkSize - The size (in bytes) of each packet.
	 * @param rateLimit - The rate in which packets are transfered as defined by {@code Bytes/Seconds}
	 * @return {@link TransferInterface} - The interface in which users can send commands to the transfer thread.
	 * @throws FileNotFoundException - If the specified file is not available or the user has no read permissions.
	 * @throws IOException - If the specified file cannot be read or the user has no read permissions.
	 */
	public TransferInterface send(File payload,Integer chunkSize, Integer rateLimit) throws FileNotFoundException, IOException{
		if(socket.isConnected()){
			TransferAction action = new TransferAction();
			TransferInterface transferInterface = new TransferInterface();
			transferInterface.attachListener(action);
			action.initPayload(this.socket, Packager.Package(payload, chunkSize), rateLimit);
			action.start();
			return transferInterface;
		}else{
			throw new RuntimeException("Socket not initialized");
		}
	}
	/**
	 * Initializes the UDP socket used for data transfer. This socket may be cast to either IPv4 or IPv6 traffic on a
	 * specific port. The same port is used for both the sending and receiving client. The receiving address must already
	 * be listening on the specified port and may not be sending data back to the client during the transfer.
	 * In order to begin another transfer operation, the user must make another call to this function with a different
	 * destination address.
	 * 
	 * @param isIpv6 States whether the destination address is an Ipv6 address.
	 * @param hostAddress - The host address to send the data to.
	 * @param port - The port on which to initialize transfer.
	 * @throws IOException - If the Datagram socket cannot be opened on the specified port.
	 */
	public void initializeSocket(boolean isIpv6, String hostAddress, Integer port) throws IOException {
		this.determineMetadata(isIpv6, hostAddress, port);
		this.socket = new Socket(destinationMetadata.getAddress(), destinationMetadata.getPort());
		final boolean connected = connectSocket();
		if (!connected) {
			throw new RuntimeException("Cannot attach datagram socket to stream");
		}
	}

	/**
	 * @return Returns the textual representation of the source meta-data and the destination meta-data.
	 */
	public String displayMetadata() {
		return "" + sourceMetadata + "/n" + destinationMetadata;
	}

	/**
	 * Private helper method to connect the socket to the destination port and address. Marked private to prevent
	 * outside access to the method.
	 * @return boolean - Whether or not the socket is successfully connected.
	 */
	private boolean connectSocket() {
		return socket.isConnected();
	}
	/**
	 * Helper method to determine connection metadata.
	 * @param isIpv6 - Marks if the connection is IPv6 or IPv4.
	 * @param hostAddress - Host address to make the connection to.
	 * @param port - Port on which to make the connection.
	 */
	private void determineMetadata(boolean isIpv6, String hostAddress, int port) {
		this.sourceMetadata = determineHostMetadata(isIpv6, "localhost", port);
		this.destinationMetadata = determineHostMetadata(isIpv6, hostAddress, port);
	}

	/**
	 * @param isIpv6 - Marks if the connection is IPv6 or IPv4.
	 * @param hostAddress - Host address to make the connection to.
	 * @param port - Port on which to make the connection.
	 * @return ConnectionMetadata - The metadata marker that represents the connection.
	 */
	private ConnectionMetadata determineHostMetadata(boolean isIpv6, String hostAddress, int port) {
		InetAddress address;
		try {
			if (!isIpv6) {
				address = (Inet4Address) Inet4Address.getByName(hostAddress);
			} else {
				address = (Inet6Address) Inet6Address.getByName(hostAddress);
			}
			ConnectionMetadata metadata = new ConnectionMetadata(address, address.getHostName(), port);
			return metadata;
		} catch (UnknownHostException e) {
			throw new RuntimeException("Unable to reference localhost");
		}
	}
	/**
	 * Test main method to send UDP traffic in chunks to an address. Change file directory pathway to test other files.
	 * @param args - Arguments are discarded upon running file.
	 */
	public static void main(String [] args){
		SocketHandler handler = new SocketHandler();
		try {
			handler.initializeSocket(false, "192.168.254.197", 44);
			handler.send(new File("/home/troy/testImage.png"), 64, 256);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
