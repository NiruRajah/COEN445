package data;

import java.net.InetAddress;

public class Packet 
{
	private Connection connection;
	private InetAddress iPAddress;
	private byte[] dataBytes;
	private int port;
	
	public Packet(byte[] data, Connection receiver) 
	{
		this.dataBytes = data;
		this.connection = receiver;
	}	

	public Packet(byte[] data, InetAddress IP, int port) 
	{
		this.dataBytes = data;
		this.iPAddress = IP;
		this.port = port;
		this.connection = new Connection(null, IP, port);
	}

	public byte[] getData() 
	{
		return dataBytes;
	}

	public InetAddress getAddr() 
	{
		return iPAddress;
	}

	public int getPort() 
	{
		return port;
	}

	public Connection getConnection() 
	{
		return this.connection;
	}
}
