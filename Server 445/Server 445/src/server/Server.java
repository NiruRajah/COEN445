package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import data.Packet;
import data.PacketHandler;


public class Server implements Runnable 
{
	private int port;
	private DatagramSocket socket;
	private boolean running;
	private Thread send, receive, process;

	public Server(int port) 
	{
		this.port = port;
		try 
		{
			this.init();
		} catch (SocketException e) 
		{
			System.err.println("Unable to start server..." + e.getMessage());
		}
	}

	public void init() throws SocketException 
	{
		this.socket = new DatagramSocket(this.port);
		process = new Thread(this, "server_process");
		process.start();
	}

	public int getPort() 
	{
		return port;
	}
	
	public synchronized void send(final Packet packX) 
	{
		send = new Thread("send_thread") 
		{
			public void run() 
			{
				DatagramPacket datagramPack = new DatagramPacket(
						packX.getData(), 
						packX.getData().length, 
						packX.getAddr(), 
						packX.getPort()
				);
				try {
					socket.send(datagramPack);
				} catch (IOException e) 
				{
					System.out.println(e.getMessage());
				}
			}
		};
		send.start();
	}
	
	public synchronized void receive(final PacketHandler packHandlerX) 
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
						packHandlerX.process(new Packet(datagramPack.getData(), datagramPack.getAddress(), datagramPack.getPort()));
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

	public void run() 
	{
		running = true;
		System.out.println("Successfully Started Server On Port: " + port);
	}
}