package transfer.connection;

import java.net.InetAddress;

/**
 * @author Troy Heanssgen
 *
 */

public class ConnectionMetadata {
	/** Ethernet address the connection metadata refers to. This can be either a
	 *  {@link Inet4Address} or a {@link Inet6Address}
	 */
	private InetAddress address;
	/**
	 * Host name of the machine the connection metadata refers to.
	 */
	private String hostName;
	/**
	 * Port on which the communications will take place
	 */
	private int port;

	/**
	 * @return Returns the port the connection is being established on.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the port to establish a connection on.
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Three argument constructor used to generate a connection metadata file. This is used by the socket handler
	 * to provide an easy interface to the user to find out details about the connection.
	 * 
	 * @param address - The address to which the connection is being made.
	 * @param hostname - The hostname of the computer.
	 * @param port - The port on which communications take place.
	 */
	public ConnectionMetadata(InetAddress address, String hostname, int port) {
		this.address = address;
		this.hostName = hostname;
		this.port = port;
	}

	
	/**
	 * @return - The address to which the connection is being made
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Sets the address of the connection.
	 * @param address
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * @return - The hostname of the connection.
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Sets the hostname of the connection.
	 * @param hostName - The name of the host
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
