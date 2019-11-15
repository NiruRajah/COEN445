package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import data.Packet;
import data.PacketHandler;
import Messeges.*;
import Rooms.*;

public class ServerHandler 
//this class is responsible for deciding when to send/receive the correct messages for Servers
{

	private Server server;
	private int mT;
	private ArrayList<Meetings> meetingsArray = new ArrayList<Meetings>();
	private Room room;
	
	public synchronized void test() throws IOException //receiving and testing all messages
	{
		mT = 0;
		server = new Server(1337);
		room = new Room();
		server.receive(new PacketHandler() 
		{

			@Override
			public synchronized void process(Packet packet) throws ClassNotFoundException, IOException 
			{
				System.out.print("Received From Client:");
				print(convertToObject(packet));
				
				//These are functions for checking what type of message was received and what to do depending
				//on what the messages were
				checkRoom(packet); 
				
				checkSchedule(packet);
				
				roomChange(packet);
				
				cancelMeetingFromRequester(packet);
				
				withdrawMessageDetector(packet);
				
				addParticipant(packet);
				
			}
		});
	}
	
	//part 2.4 requirements logic for adding participants
	public synchronized void addParticipant(Packet packet) throws ClassNotFoundException, IOException
	{
		Object obj = convertToObject(packet);
		if(obj.getClass() == AddClient.class)
		{
			AddClient addMsg = new AddClient();
			addMsg = (AddClient)obj;
			boolean cancelledMeetingChecker = true;
			boolean invalidEntryChecker = true;
			for (int i = 0; i < meetingsArray.size(); i++)
			{
				if(addMsg.getmTNumber() == meetingsArray.get(i).getmT())
				{
					invalidEntryChecker = false;
					for (int j = 0; j < meetingsArray.get(i).getDeclinedClients().size(); j++)
					{
						if(meetingsArray.get(i).getDeclinedClients().get(j).equals(packet.getAddr()))
						{
							meetingsArray.get(i).getConfirmedClients().add(packet.getAddr());
							meetingsArray.get(i).getDeclinedClients().remove(j);
							meetingsArray.get(i).incrementAcceptCounter();
							
							
							ConfirmMessage confirmMsg = new ConfirmMessage(addMsg.getmTNumber(), 
									meetingsArray.get(i).getRoomNumber(), meetingsArray.get(i).getDate(),
									meetingsArray.get(i).getTime());
							sendToClient(new Packet(convertToBytes(confirmMsg), 
									packet.getAddr(), packet.getPort()));
							InformRequesterOfAddedClient informReq = new InformRequesterOfAddedClient(
									addMsg.getmTNumber(), packet.getAddr());
							sendToClient(new Packet(convertToBytes(informReq), 
									meetingsArray.get(i).getRequester(), packet.getPort()));
							cancelledMeetingChecker = false;
						}
						
					}
				}
			}
			if(cancelledMeetingChecker)
			for (int i = 0; i < meetingsArray.size(); i++)
			{
				CancelMessage cancelMsg = new CancelMessage(addMsg.getmTNumber(), 
						"Meeting Has Already Been Cancelled",
						meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime());
				sendToClient(new Packet(convertToBytes(cancelMsg), 
						packet.getAddr(), packet.getPort()));
			}
			if(invalidEntryChecker)
			{
				System.out.println("Ignored Invalid Add Message Request From Client");
			}
		}
				
	}
	
	//part 2.3 requirements logic for withdrawing from a meeting
	public synchronized void withdrawMessageDetector(Packet packet) throws ClassNotFoundException, IOException
	{
		boolean checkX = true;
		Object obj = convertToObject(packet);
		if(obj.getClass() == WithdrawMessage.class)
		{
			WithdrawMessage withdrawMsg = new WithdrawMessage();
			withdrawMsg = (WithdrawMessage)obj;
			for (int i = 0; i < meetingsArray.size(); i++)
			{
				if(withdrawMsg.getmTNumber() == meetingsArray.get(i).getmT())
				{

					//The below logical conditions are under the assumption that when a client withdraws
					//from the meeting, he still remains in the list of participants, but instead gets
					//added on to the declined participants list and removed from the confirmed participants list
					for (int j = 0; j < meetingsArray.get(i).getConfirmedClients().size(); j++)
					{
						if(meetingsArray.get(i).getConfirmedClients().get(j).equals(packet.getAddr()))
						{
							meetingsArray.get(i).getConfirmedClients().remove(j);
							meetingsArray.get(i).getDeclinedClients().add(packet.getAddr());
							CancelMessage cancelMsg = new CancelMessage(withdrawMsg.getmTNumber(), 
								("As requested you have been withdrawn from: Meeting Number: " 
									+ withdrawMsg.getmTNumber()), 
									meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime());
							sendToClient(new Packet(convertToBytes(cancelMsg),
									packet.getAddr(), packet.getPort()));
						}
						
					}
					
					InformRequesterOfWithdrawal informReqMsg = new InformRequesterOfWithdrawal(
							withdrawMsg.getmTNumber(), packet.getAddr());
					sendToClient(new Packet(convertToBytes(informReqMsg), 
							meetingsArray.get(i).getRequester(), packet.getPort()));
					meetingsArray.get(i).decrementAcceptCounter();
					
					
					// if minimum is higher than accept counter, resend invite to declined list of participants
					if(meetingsArray.get(i).getMinimum() > meetingsArray.get(i).getAcceptCounter())
					{
						InviteMessage inviteMsg = new InviteMessage(meetingsArray.get(i).getmT(), 
								meetingsArray.get(i).getDate(), 
								meetingsArray.get(i).getTime(), 
								meetingsArray.get(i).getTopic(),
								meetingsArray.get(i).getRequester());
						for(int j = 0; j < meetingsArray.get(i).getDeclinedClients().size(); j++)
						{
							sendToClient(new Packet(convertToBytes(inviteMsg),
									meetingsArray.get(i).getDeclinedClients().get(j), 
									packet.getPort()));
						}
					}
					checkX = false;
				}
				
			}
			if(checkX)
			{
				System.out.println("Ignored Invalid Withdraw Message Request From Client");
				
			}
		}
		
	}
	
	//part 2.2 requirements logic for cancelling a meeting which is done only by the requester
	public synchronized void cancelMeetingFromRequester(Packet packet) throws ClassNotFoundException, IOException
	{
		boolean checkX = true;
		Object obj = convertToObject(packet);
		if(obj.getClass() == CancelMessageFromRequester.class)
		{
			CancelMessageFromRequester cancelMsg = new CancelMessageFromRequester();
			cancelMsg = (CancelMessageFromRequester)obj;
			for (int i = 0; i < meetingsArray.size(); i++)
			{
				if(cancelMsg.getmTNumber() == meetingsArray.get(i).getmT() &&
						packet.getAddr().equals(meetingsArray.get(i).getRequester()))
				{
					CancelMessage cancelMsgToSend = new CancelMessage(meetingsArray.get(i).getmT(), 
							"Meeting Cancelled By Requester",
							meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime());
					sendToClient(new Packet(convertToBytes(cancelMsgToSend), 
							meetingsArray.get(i).getRequester(), packet.getPort()));
					for(int j = 0; j < meetingsArray.get(i).getConfirmedClients().size(); j++)
					{
						sendToClient(new Packet(convertToBytes(cancelMsgToSend), 
								meetingsArray.get(i).getConfirmedClients().get(j), packet.getPort()));
					}

					room.delete(meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime(),
							meetingsArray.get(i).getRoomNumberIndex());
					meetingsArray.remove(i);

					checkX = false;
				}
			}
			if(checkX)
			{
				System.out.println("Ignored Invalid Cancel Message Request From Requester");
				
			}
		}
		
			
	}
	
	//part 2.5 requirements logic for room change
	public synchronized void roomChange(Packet packet) throws ClassNotFoundException, IOException
	{
		boolean invalid = true;
		Object obj = convertToObject(packet);
		if(obj.getClass() == RoomChangeMessage.class)
		{
			RoomChangeMessage roomMsg = new RoomChangeMessage();
			roomMsg = (RoomChangeMessage) obj;
			for (int i = 0; i < meetingsArray.size(); i++)
			{
				if(roomMsg.getRoomNumber().equals(meetingsArray.get(i).getRoomNumber())
						&& roomMsg.getmTNumber() == meetingsArray.get(i).getmT())
				{
					invalid = false;
					if(room.checkRoomIsFree(meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime()))
					{
						int index = 0;
						index = room.changeRoom(meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime(),
								meetingsArray.get(i).getRoomNumberIndex(), meetingsArray.get(i));
						
						meetingsArray.get(i).setRoomNumberIndex(index-1);
						meetingsArray.get(i).setRoomNumber(String.valueOf(meetingsArray.get(i).getDate()) + 
								String.valueOf(meetingsArray.get(i).getTime()) 
						+ String.valueOf(index-1));
						
						roomMsg.setmTNumber(meetingsArray.get(i).getmT());
						roomMsg.setRoomNumber(meetingsArray.get(i).getRoomNumber());
						
						//Send to message to all clients and the requester of room.
						//Everyone associated to the meeting number should be aware of the room change,
						//incase they want to add theirselves later on
						sendToClient(new Packet(convertToBytes(roomMsg), 
								meetingsArray.get(i).getRequester(), packet.getPort()));
						for(int j = 0; j < meetingsArray.get(i).getTotalClients().size(); j++)
						{
							sendToClient(new Packet(convertToBytes(roomMsg), 
									meetingsArray.get(i).getTotalClients().get(j), packet.getPort()));
						}
					}
					else
					{
						//send cancel message 
						CancelMessage cancelMsg = new CancelMessage(meetingsArray.get(i).getmT(), 
								"Room under maintenance and no other rooms are available",
								meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime());
						sendToClient(new Packet(convertToBytes(cancelMsg), 
								meetingsArray.get(i).getRequester(), packet.getPort()));
						for(int j = 0; j < meetingsArray.get(i).getConfirmedClients().size(); j++)
						{
							sendToClient(new Packet(convertToBytes(cancelMsg), 
									meetingsArray.get(i).getConfirmedClients().get(j), packet.getPort()));
						}
						int index = 0;
						meetingsArray.get(i).setRoomNumberIndex(index);
						meetingsArray.get(i).setRoomNumber(null);
						room.delete(meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime(),
								meetingsArray.get(i).getRoomNumberIndex());
						meetingsArray.remove(i);
					}
				}
			}
			if(invalid)
			{
				System.out.println("Ignored Invalid Room Change Request");
				
			}
		}
	}
	
	//logic for checking if minimum is higher confirmed clients
	//also processes received accept/reject messages
	//depending on conditions sends confirm/cancel messages to clients, positive/negative response to requester
	public synchronized void checkSchedule(Packet packet) throws IOException, ClassNotFoundException
	{
		Object obj = convertToObject(packet);
		
		if(obj.getClass() == AcceptMessage.class)
		{
			AcceptMessage acceptMsg = new AcceptMessage();
			acceptMsg = (AcceptMessage) obj;
			for(int i = 0; i < meetingsArray.size(); i++)
			{
				if(acceptMsg.getmTNumber() == meetingsArray.get(i).getmT())
				{
					meetingsArray.get(i).incrementAcceptCounter();
					meetingsArray.get(i).incrementTotalCounter();
					meetingsArray.get(i).getConfirmedClients().add(packet.getAddr());
					if(meetingsArray.get(i).getTotalCounter() == 
							meetingsArray.get(i).getTotalClients().size())
					{
						if(meetingsArray.get(i).getAcceptCounter() >= meetingsArray.get(i).getMinimum())
						{
							int index = 0;
							index = room.add(meetingsArray.get(i).getDate(), 
									meetingsArray.get(i).getTime(), meetingsArray.get(i));
							
							ConfirmMessage confirmMsg = new ConfirmMessage(acceptMsg.getmTNumber(), 
									(String.valueOf(meetingsArray.get(i).getDate()) + 
											String.valueOf(meetingsArray.get(i).getTime()) 
												+ String.valueOf(index-1)), meetingsArray.get(i).getDate(),
									meetingsArray.get(i).getTime());
							
							meetingsArray.get(i).setRoomNumber(String.valueOf(meetingsArray.get(i).getDate()) + 
									String.valueOf(meetingsArray.get(i).getTime()) 
							+ String.valueOf(index-1));
							meetingsArray.get(i).setRoomNumberIndex(index-1);
							
							for(int j = 0; j < meetingsArray.get(i).getConfirmedClients().size(); j++)
							{
								sendToClient(new Packet(convertToBytes(confirmMsg), 
										meetingsArray.get(i).getConfirmedClients().get(j), packet.getPort()));
							}
							
							PositiveResponseToRequester posMsg = new PositiveResponseToRequester(
									meetingsArray.get(i).getrQ(),
									acceptMsg.getmTNumber(), confirmMsg.getRoomNumber(), 
									meetingsArray.get(i).getConfirmedClients());
							
							sendToClient(new Packet(convertToBytes(posMsg), 
									meetingsArray.get(i).getRequester(), packet.getPort()));
							
						}
						else
						{
							
							CancelMessage cancelMsg = new CancelMessage(acceptMsg.getmTNumber(), 
									"Accepted participants are lower than required minimum",
									meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime());
							
							for(int j = 0; j < meetingsArray.get(i).getConfirmedClients().size(); j++)
							{
								sendToClient(new Packet(convertToBytes(cancelMsg), 
										meetingsArray.get(i).getConfirmedClients().get(j), packet.getPort()));
							}
							NegativeResponseToRequester negMsg = new NegativeResponseToRequester(
									meetingsArray.get(i).getrQ(),
									meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime(), 
									meetingsArray.get(i).getMinimum(),
									meetingsArray.get(i).getConfirmedClients(),
									meetingsArray.get(i).getTopic());
							
							sendToClient(new Packet(convertToBytes(negMsg), 
									meetingsArray.get(i).getRequester(), packet.getPort()));
							meetingsArray.remove(i);
						}
					}
				}
			}
			
		}
		else if(obj.getClass() == RejectMessage.class)
		{
			RejectMessage rejectMsg = new RejectMessage();
			rejectMsg = (RejectMessage) obj;
			for(int i = 0; i < meetingsArray.size(); i++)
			{
				if(rejectMsg.getmTNumber() == meetingsArray.get(i).getmT())
				{
					meetingsArray.get(i).incrementTotalCounter();
					meetingsArray.get(i).getDeclinedClients().add(packet.getAddr());
					if(meetingsArray.get(i).getTotalCounter() == 
							meetingsArray.get(i).getTotalClients().size())
					{
						if(meetingsArray.get(i).getAcceptCounter() >= meetingsArray.get(i).getMinimum())
						{
							int index = 0;
							index = room.add(meetingsArray.get(i).getDate(), 
									meetingsArray.get(i).getTime(), meetingsArray.get(i));
							ConfirmMessage confirmMsg = new ConfirmMessage(rejectMsg.getmTNumber(), 
									(String.valueOf(meetingsArray.get(i).getDate()) + 
											String.valueOf(meetingsArray.get(i).getTime()) 
												+ String.valueOf(index)), meetingsArray.get(i).getDate(),
									meetingsArray.get(i).getTime());
							
							meetingsArray.get(i).setRoomNumber(String.valueOf(meetingsArray.get(i).getDate()) + 
									String.valueOf(meetingsArray.get(i).getTime()) 
							+ String.valueOf(index));
							meetingsArray.get(i).setRoomNumberIndex(index-1);
							
							for(int j = 0; j < meetingsArray.get(i).getConfirmedClients().size(); j++)
							{
								sendToClient(new Packet(convertToBytes(confirmMsg), 
										meetingsArray.get(i).getConfirmedClients().get(j), packet.getPort()));
							}
							PositiveResponseToRequester posMsg = new PositiveResponseToRequester(
									meetingsArray.get(i).getrQ(),
									rejectMsg.getmTNumber(), confirmMsg.getRoomNumber(), 
									meetingsArray.get(i).getConfirmedClients());
							
							sendToClient(new Packet(convertToBytes(posMsg), 
									meetingsArray.get(i).getRequester(), packet.getPort()));
						}
						else
						{

							CancelMessage cancelMsg = new CancelMessage(rejectMsg.getmTNumber(), 
									"Accepted participants are lower than required minimum",
									meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime());
							
							for(int j = 0; j < meetingsArray.get(i).getConfirmedClients().size(); j++)
							{
								sendToClient(new Packet(convertToBytes(cancelMsg), 
										meetingsArray.get(i).getConfirmedClients().get(j), packet.getPort()));
							}
							NegativeResponseToRequester negMsg = new NegativeResponseToRequester(
									meetingsArray.get(i).getrQ(),
									meetingsArray.get(i).getDate(), meetingsArray.get(i).getTime(), 
									meetingsArray.get(i).getMinimum(),
									meetingsArray.get(i).getConfirmedClients(),
									meetingsArray.get(i).getTopic());
							
							sendToClient(new Packet(convertToBytes(negMsg), 
									meetingsArray.get(i).getRequester(), packet.getPort()));
							meetingsArray.remove(i);
						}
					}
				}
			}
		}
		
		
	}
	
	//checks to see if any room is available for the date/time
	public synchronized void checkRoom(Packet packet) throws ClassNotFoundException, IOException
	{
		Object obj = convertToObject(packet);
		if(obj.getClass() == RequestMessage.class)
		{
			RequestMessage requestMsg = new RequestMessage();
			requestMsg = (RequestMessage) obj;
			this.mT = mT + 1;
			Meetings parts = new Meetings(packet.getAddr(), requestMsg.getListOfParticipants(),
					requestMsg.getMinimum(), mT, requestMsg.getDate(), requestMsg.getTime(),
					requestMsg.getTopic(), requestMsg.getRQ());
			meetingsArray.add(parts);
			if(room.checkRoomIsFree(requestMsg.getDate(), requestMsg.getTime()))
			{
				InviteMessage inviteMsg = new InviteMessage(mT, requestMsg.getDate(), requestMsg.getTime(), 
						requestMsg.getTopic(), parts.getRequester());
				for(int i = 0; i < parts.getTotalClients().size(); i++)
				{
					sendToClient(new Packet(convertToBytes(inviteMsg), parts.getTotalClients().get(i), 
							packet.getPort()));
				}
			}
			else
			{
				RoomUnavailableResponse unavailableMsg = new RoomUnavailableResponse(requestMsg.getRQ(), 
						"Room Unavailable");
				sendToClient(new Packet(convertToBytes(unavailableMsg), packet.getAddr(), 
						packet.getPort()));
			}
		}
		
	}
	
	//send messages to client
	public synchronized void sendToClient(Packet packet)
	{
		server.send(packet);
	}
	
	//convert object to bytes
	public synchronized byte[] convertToBytes(Object obj) throws IOException
	{
		ByteArrayOutputStream outStream1 = new ByteArrayOutputStream();
		ObjectOutput output1 = new ObjectOutputStream(outStream1);
		output1.writeObject(obj);
		output1.close();
		byte[] outputByte1 = outStream1.toByteArray();
		return outputByte1;
	}
	
	//convert bytes to object
	public synchronized Object convertToObject(Packet dgpacket) throws IOException, ClassNotFoundException
	{
		byte[] buffer = new byte[1024];
		buffer = dgpacket.getData();
		ObjectInputStream iStreamx = new ObjectInputStream(new ByteArrayInputStream(buffer));
        Object object = iStreamx.readObject();
        iStreamx.close();
		return object;
	}

	
	@SuppressWarnings("null")
	public synchronized void print(Object obj) //print functions depending on messages
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
			AcceptMessage msg = new AcceptMessage();
			msg = (AcceptMessage) obj;
			msg.print();
		}
		if(obj.getClass() == RejectMessage.class)
		{
			RejectMessage msg = new RejectMessage();
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

	//calls the test function
	public static void main(String args[]) throws IOException, InterruptedException 
    {
		ServerHandler s1 = new ServerHandler();
		s1.test();
    }

	
    
}
