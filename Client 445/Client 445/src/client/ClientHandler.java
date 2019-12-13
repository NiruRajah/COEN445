package client;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Messeges.AcceptMessage;
import Messeges.AddClient;
import Messeges.CancelMessage;
import Messeges.CancelMessageFromRequester;
import Messeges.CancelInviteMessage;
import Messeges.ConfirmMessage;
import Messeges.InformRequesterOfAddedClient;
import Messeges.InformRequesterOfWithdrawal;
import Messeges.InviteMessage;
import Messeges.NegativeResponseToRequester;
import Messeges.PositiveResponseToRequester;
import Messeges.RejectMessage;
import Messeges.RequestMessage;
import Messeges.RoomChangeMessage;
import Messeges.RoomUnavailableResponse;
import Messeges.WithdrawMessage;
import data.Packet;
import data.PacketHandler;

public class ClientHandler

//this class is responsible for deciding when to send/receive the correct messages for Servers and do the testing
{

	private Client client;
	private int port;
	private boolean breaker;
	private boolean[][] meetingAvailability = new boolean [8][25];
	public boolean dontuse = false;
	//public static ClientHandler c1;
	
	public ClientHandler(String inp1, int inp2) throws SocketException, UnknownHostException 
	{
		this.client = new Client(inp1, inp2);
		if(client.dontuse) {
			dontuse = true;
			return;
		}
		this.port = inp2;
		this.breaker = true;
		for (int i = 1; i <= 7; i++) //set the meeting availability scheduler to all true (available)
		{
			for (int j = 1; j <= 24; j++)
			{
				meetingAvailability[i][j] = true;
			}
		}
	}
	
	public synchronized void test() throws IOException 
	{
		client.receive(new PacketHandler() 
			{
				@Override
				public synchronized void process(Packet packet) throws ClassNotFoundException, IOException 
				{

					
					System.out.print("Received From Server:");
					print(convertToObject(packet));
					//These are functions for checking what type of message was received and what to do depending
					//on what the messages were
					checkForInviteMessage(packet);
					
					checkForConfirmMessage(packet);
					
					checkForNegativeResponseToRequester(packet);
				}
			});
	}
	
	public synchronized void sentToServer(Object object) throws IOException
	{
		client.send(convertToBytes(object));
	}
	
	public synchronized void runner() throws IOException, InterruptedException
	//testing all the functions by allowing the clients to decide what to send
	{
		while (true)
		{
			System.out.println("Press 1 to send a Request Message"
								+ "\nPress 2 To Cancel A Meeting"
								+  "\nPress 3 To Withdraw From A Meeting"
								+  "\nPress 4 To Add Yourself To A Meeting You Declined Before"
								+  "\nPress 5 To Create An Unavailability Scenario For An Existing Booked Meeting Room"
								+  "\nPress 8 To Exit And View Any Pending Messages "
								+	"\nNOTE: 'Received From Server:' Means There Are Pending Messages");
								//+ 	"\nPress 9 To Exit");
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			int inp = 0;
			
			if(sc.hasNextInt()) 
			{
			   inp = sc.nextInt();
			}
			else
			{
				
			}
			if(inp == 1)
			{
				inp = 9;
				ArrayList<InetAddress> list1 = new ArrayList<InetAddress>();
				list1.clear();
				ArrayList<Integer> list2 = new ArrayList<Integer>();
				list2.clear();
				int max = 1000;
				int min = 1;
				int rQ = (int) ((Math.random()*((max-1)+1))+min);
				int date = 0;
				int time = 0;
				int minimum = 0;
				String topic;
				int op = 0;
				while(op == 0) 
				{
					System.out.println("1. Nov 4\t2. Nov 5\t3. Nov 6\t4. Nov 7\t5. Nov 8");
					System.out.println("Enter the day (#): ");
					date = sc.nextInt();
					if(date>0 && date<6) 
					{
						op = 1;
					}
					else 
					{
						System.out.println("try again");
					}
				
				}
				while(op == 1) 
				{
					System.out.println("Open from 8H to 17H");
					System.out.println("Enter the time: ");
					time = sc.nextInt();
					
					if(time>7 && time<18) 
					{
						op = 9;
					}
					else 
					{
						System.out.println("try again");
					}
					
				}
				System.out.println("Enter the minimum number of participants needed for the meeting");
				minimum = sc.nextInt();
				Scanner sc1 = new Scanner(System.in);
				String ip = "false";
				while(!(ip.equals("next")))
				{
					System.out.println("Enter all the attending participant's ip addresses and type in 'next' when done");
					ip = sc1.nextLine();
					if(!(ip.equals("next")))
					{
						System.out.println("added: " + InetAddress.getByName(ip));
						list1.add(InetAddress.getByName(ip));
						System.out.println("Enter the port number of attending participant");
						int portX = sc.nextInt();
						list2.add(portX);
					}
					
				}
				System.out.println("Enter the topic");
				topic = sc1.nextLine();
				if (meetingAvailability[date][time])
				{
					RequestMessage requestMsg = new RequestMessage(rQ, date, time, minimum, list1, topic, list2, 
							port);
					Object obj = (Object) requestMsg;
					sentToServer(obj);
				}
				else
				{
					System.out.println("Cannot send request because you are not available at this date/time");
				}
				
				break;
				
			}
			else if(inp == 2)
			{
				System.out.println("Enter the Meeting Number of the meeting you want to cancel");
				//Scanner scZ = new Scanner(System.in);
				int inpx = 0;
				inpx = sc.nextInt();
				CancelMessageFromRequester cancelMsg = new CancelMessageFromRequester(inpx);
				Object obj = (Object) cancelMsg;
				sentToServer(obj);
				break;
			}
			else if(inp == 3)
			{
				System.out.println("Enter the Meeting Number of the meeting you want to withdraw from");
				int inpx = 0;
				inpx = sc.nextInt();
				WithdrawMessage withdrawMsg = new WithdrawMessage(inpx);
				Object obj = (Object) withdrawMsg;
				sentToServer(obj);
				break;
			}
			else if(inp == 4)
			{
				System.out.println("Enter the Meeting Number of the room you want to add yourself to");
				int inpx = 0;
				inpx = sc.nextInt();
				AddClient addMsg = new AddClient(inpx);
				Object obj = (Object) addMsg;
				sentToServer(obj);
				break;
			}
			else if(inp == 5)
			{
				@SuppressWarnings("resource")
				Scanner sc2 = new Scanner(System.in);
				String roomNumber = null;
				System.out.println("Enter the Meeting Number of the room you want to create the scenario for");
				int inpx = 0;
				inpx = sc.nextInt();
				System.out.println("Enter the Room Number of the room you want to create the scenario for");
				roomNumber = sc2.nextLine();
				RoomChangeMessage roomMsg = new RoomChangeMessage(inpx, roomNumber);
				Object obj = (Object) roomMsg;
				sentToServer(obj);
				break;
			}
			else if (inp == 8) 
			{
				break;
				
			}
			else
			{
				
			}
		}
	
	}
	
	//checks to see if any meetings were cancelled and if so, updates the client's schedule to available
	public synchronized void checkForNegativeResponseToRequester(Packet packet) throws ClassNotFoundException, IOException
	{
		Object obj = convertToObject(packet);
		if(obj.getClass() == CancelMessage.class)
		{
			CancelMessage cancelMsg = new CancelMessage();
			cancelMsg = (CancelMessage)obj;
			meetingAvailability[cancelMsg.getDate()][cancelMsg.getTime()] = true;
		}
		if(obj.getClass() == NegativeResponseToRequester.class)
		{
			NegativeResponseToRequester negMsg = new NegativeResponseToRequester();
			negMsg = (NegativeResponseToRequester)obj;
			meetingAvailability[negMsg.getDate()][negMsg.getTime()] = true;
		}
	}

	//checks for confirm messages and if so, updates the client's schedule to not available
	public synchronized void checkForConfirmMessage(Packet packet) throws ClassNotFoundException, IOException
	{
		Object obj = convertToObject(packet);
		if(obj.getClass() == ConfirmMessage.class)
		{
			ConfirmMessage msg = new ConfirmMessage();
			msg = (ConfirmMessage) obj;
			if(meetingAvailability[msg.getDate()][msg.getTime()])
			{
				meetingAvailability[msg.getDate()][msg.getTime()] = false;				
			}
		}
		if(obj.getClass() == PositiveResponseToRequester.class)
		{
			PositiveResponseToRequester msg = new PositiveResponseToRequester();
			msg = (PositiveResponseToRequester) obj;
			if(meetingAvailability[msg.getDate()][msg.getTime()])
			{
				meetingAvailability[msg.getDate()][msg.getTime()] = false;				
			}
		}
	}
	
	//checks for invite messages and sends accept/reject messages to server depending on availability of client
	public synchronized void checkForInviteMessage(Packet packet) throws ClassNotFoundException, IOException
	{
		Object obj = convertToObject(packet);
		if(obj.getClass() == InviteMessage.class)
		{
			InviteMessage msg = new InviteMessage();
			msg = (InviteMessage) obj;
			if(meetingAvailability[msg.getDate()][msg.getTime()])
			{
				AcceptMessage acceptMsg = new AcceptMessage(msg.getMT());
				client.send(convertToBytes(acceptMsg));
			}
			else
			{
				RejectMessage rejectMsg = new RejectMessage(msg.getMT());
				client.send(convertToBytes(rejectMsg));
			}
		}
	}
	
	//converts objects to bytes
	public synchronized byte[] convertToBytes(Object obj) throws IOException
	{
		ByteArrayOutputStream outStream1 = new ByteArrayOutputStream();
		ObjectOutput output1 = new ObjectOutputStream(outStream1);
		output1.writeObject(obj);
		output1.close();
		byte[] outputByte1 = outStream1.toByteArray();
		return outputByte1;
	}
	
	//converts bytes to object
	public synchronized Object convertToObject(Packet dgpacket) throws IOException, ClassNotFoundException
	{
		byte[] buffer = new byte[1024];
		buffer = dgpacket.getData();
		ObjectInputStream iStreamx = new ObjectInputStream(new ByteArrayInputStream(buffer));
        Object object = iStreamx.readObject();
        iStreamx.close();
		return object;
	}
	
	//all the print functions depending on the messages received from server
	public synchronized void print(Object obj)
	{
		if(obj.getClass() == RequestMessage.class)
		{
			RequestMessage msg = new RequestMessage();
			msg = (RequestMessage) obj;
			msg.print();
		}
		if(obj.getClass() == InviteMessage.class)
		{
			InviteMessage msg = new InviteMessage();
			msg = (InviteMessage) obj;
			msg.print();
		}
		if(obj.getClass() == AcceptMessage.class)
		{
			AcceptMessage msg = new AcceptMessage(6);
			msg = (AcceptMessage) obj;
			msg.print();
		}
		if(obj.getClass() == RejectMessage.class)
		{
			RejectMessage msg = new RejectMessage(6);
			msg = (RejectMessage) obj;
			msg.print();
		}
		if(obj.getClass() == RoomUnavailableResponse.class)
		{
			RoomUnavailableResponse msg = new RoomUnavailableResponse();
			msg = (RoomUnavailableResponse) obj;
			msg.print();
		}
		if(obj.getClass() == ConfirmMessage.class)
		{
			ConfirmMessage msg = new ConfirmMessage();
			msg = (ConfirmMessage) obj;
			msg.print();
		}
		if(obj.getClass() == CancelMessage.class)
		{
			CancelMessage msg = new CancelMessage();
			msg = (CancelMessage) obj;
			msg.print();
		}
		if(obj.getClass() == CancelInviteMessage.class)
		{
			CancelInviteMessage msg = new CancelInviteMessage();
			msg = (CancelInviteMessage) obj;
			msg.print();
		}
		if(obj.getClass() == PositiveResponseToRequester.class)
		{
			PositiveResponseToRequester msg = new PositiveResponseToRequester();
			msg = (PositiveResponseToRequester) obj;
			msg.print();
		}
		if(obj.getClass() == NegativeResponseToRequester.class)
		{
			NegativeResponseToRequester msg = new NegativeResponseToRequester();
			msg = (NegativeResponseToRequester) obj;
			msg.print();
		}
		if(obj.getClass() == WithdrawMessage.class)
		{
			WithdrawMessage msg = new WithdrawMessage();
			msg = (WithdrawMessage) obj;
			msg.print();
		}
		if(obj.getClass() == InformRequesterOfWithdrawal.class)
		{
			InformRequesterOfWithdrawal msg = new InformRequesterOfWithdrawal();
			msg = (InformRequesterOfWithdrawal) obj;
			msg.print();
		}
		if(obj.getClass() == AddClient.class)
		{
			AddClient msg = new AddClient();
			msg = (AddClient) obj;
			msg.print();
		}
		if(obj.getClass() == InformRequesterOfAddedClient.class)
		{
			InformRequesterOfAddedClient msg = new InformRequesterOfAddedClient();
			msg = (InformRequesterOfAddedClient) obj;
			msg.print();
		}
		if(obj.getClass() == RoomChangeMessage.class)
		{
			RoomChangeMessage msg = new RoomChangeMessage();
			msg = (RoomChangeMessage) obj;
			msg.print();
		}
		if(obj.getClass() == CancelMessageFromRequester.class)
		{
			CancelMessageFromRequester msg = new CancelMessageFromRequester();
			msg = (CancelMessageFromRequester) obj;
			msg.print();
		}
	}
	
	
	public static boolean validateIP(final String ip) 
	{
	    String pattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|"
	    		+ "2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

	    return ip.matches(pattern);
	}
	
	public static boolean isNumeric(String stringNum) 
	{
	    if (stringNum == null) 
	    {
	        return false;
	    }
	    return true;
	}
	
	
	//calling the runner function for testing the server
	public static void main(String args[]) throws IOException, InterruptedException 
    {

		Scanner scan = new Scanner(System.in);
		String input1 = "";
		String temp = "";
		int input2 = 0;
		boolean isTrue = false;
		
		while(!isTrue) 
		{
			System.out.println("Enter IP Address of Server");
			input1 = scan.nextLine();
			
			isTrue = validateIP(input1);
			
		}
		
		isTrue = false;
		
		while(!isTrue) 
		{
			System.out.println("Enter Your Port Number");
			temp = scan.nextLine();
			
			isTrue = isNumeric(temp);
			
		}
		
		input2 = Integer.parseInt(temp);
		
		
		ClientHandler c1 = new ClientHandler(input1, input2);
		if(c1.dontuse) {
			main(args);
			return;
		}
		
		c1.test();

	    String inpAddr = input1;
        int inpPort = input2;
        String display = new String("Client || IP Address: " + inpAddr 
        		+ " || Port Number: " + inpPort);
        
        System.out.println("Successfully Started: " + display);
		
		JButton button;
	    JFrame frame;
	    JTextArea textArea;
	    button = new JButton("Click This Button To Send A Message Or Create A Scenario");
	    button.setPreferredSize(new Dimension(50, 50));
	    button.setFont(new Font("Arial", Font.BOLD, 30));
        frame = new JFrame(display);
        textArea = new JTextArea(20, 60);
        textArea.setText("Successfully Started: " + display);
    	textArea.setFont(new Font("Arial", Font.BOLD, 30));
    	textArea.setEditable(false);
        textArea.setLineWrap(true);
        frame.setLayout(new BorderLayout());
        frame.add(textArea, BorderLayout.NORTH);
        frame.add(button, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
		
		button.addActionListener(new ActionListener() 
		{ 
		    public void actionPerformed(ActionEvent e) 
		    {
		    	  
		        try 
		        {
					c1.runner();
				} catch (IOException e1) 
		        {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) 
		        {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    } 
		});
		/*while(true)
		{
			c1.runner();
			Thread.sleep(2000);
		}*/
    }

    
}
