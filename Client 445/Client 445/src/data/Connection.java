package data;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class Connection 
{
	private InetAddress address;
	private int port;
	private DatagramSocket clientSocket;

	public Connection()
	{
		
	}
	
	public Connection(DatagramSocket socket, InetAddress address, int port) throws SocketException 
	{
		this.address = address;
		this.port = port;
		this.clientSocket = new DatagramSocket();
	}
	

	public synchronized void send(byte[] bytes) 
	{
		DatagramPacket datagramPack = new DatagramPacket(bytes, bytes.length, address, 1337);
		
		try 
		{
			clientSocket.send(datagramPack);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	

	public synchronized byte[] receive() 
	{
		byte[] bytes = new byte[1024];
		DatagramPacket datagramPack = new DatagramPacket(bytes, bytes.length);
		
		try 
		{
			clientSocket.receive(datagramPack);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		byte[] receivedBytes =  datagramPack.getData();
		return receivedBytes;
	}
	

	public int getPort() 
	{
		return this.port;
	}
	

	public InetAddress getAddress() 
	{
		return this.address;
	}
	
	public DatagramSocket getClientSocket()
	{
		return this.clientSocket;
	}
	

	public void close() 
	{
		new Thread() 
		{
			public void run() 
			{
				synchronized(clientSocket) 
				{
					clientSocket.close();
				}
			}
		}.start();
	}
}
