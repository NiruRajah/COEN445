package client;

import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import data.Connection;
import data.Packet;
import data.PacketHandler;


public class Client implements Runnable 
{
	
	private Connection connection = null;
	private boolean running;
	private DatagramSocket socket;
	private Thread process, send, receive;
	public boolean dontuse = false;
	
	public Client(String address, int port) throws SocketException, UnknownHostException 
	{

		try {
			socket = new DatagramSocket(port);
		}
		catch(BindException e) {
			System.out.println("Already In use. Restarting...");
			System.out.println("");
			dontuse = true;
			return;
		}
		
		try 
		{
			connection = new Connection(socket, InetAddress.getByName(address), port);
			this.init();
		} catch (SocketException | UnknownHostException e ) 
		{
			e.printStackTrace();
		}
	}	

	private void init() 
	{
		process = new Thread(this, "server_process");
		process.start();
	}
	
	public synchronized void send(final byte[] bytes) 
	{
		send = new Thread("Sending Thread") {
			public void run() 
			{
				connection.send(bytes);
			}
		};
		send.start();
	}	

	public synchronized void receive(final PacketHandler packHandler) 
	{
		receive = new Thread("receive_thread") 
		{
			public void run() 
			{
				while(running) 
				{
					byte[] bytes = new byte[1024];
					DatagramPacket datagramPack = new DatagramPacket(bytes, bytes.length);
					
					try 
					{
						socket.receive(datagramPack);
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					try 
					{
						packHandler.process(new Packet(datagramPack.getData(), datagramPack.getAddress(), 
								datagramPack.getPort()));
					} catch (ClassNotFoundException e) 
					{
						e.printStackTrace();
					} catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
		};
		receive.start();
	}	

	public void close() 
	{
		connection.close();
		running = false;
	}
	
	@Override
	public void run() 
	{
		running = true;
	}

}
