package Messeges;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class PositiveResponseToRequester implements Serializable
{
	private int rQNumber;
	private int mTNumber;
	private String roomNumber;
	private ArrayList<InetAddress> confirmedParticipants;
	private int date;
	private int time;
	
	public PositiveResponseToRequester()
	{
		
	}
	
	public PositiveResponseToRequester(int rQ, int mT, String rN, ArrayList<InetAddress> confirmedClients,
			int date, int time) 
	{
		this.rQNumber = rQ;
		this.mTNumber = mT;
		this.roomNumber = rN;
		this.confirmedParticipants = confirmedClients;
		this.setDate(date);
		this.setTime(time);
	}

	public int getrQNumber() {
		return rQNumber;
	}

	public void setrQNumber(int rQNumber) {
		this.rQNumber = rQNumber;
	}

	public int getmTNumber() {
		return mTNumber;
	}

	public void setmTNumber(int mTNumber) {
		this.mTNumber = mTNumber;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public ArrayList<InetAddress> getConfirmedClients() {
		return confirmedParticipants;
	}

	public void setConfirmedClients(ArrayList<InetAddress> confirmedClients) {
		this.confirmedParticipants = confirmedClients;
	}
	
	public void print()
	{
		System.out.print(" | Positive Response To Requester: | RQ: " + this.getrQNumber() + " | MT: "
				+ this.getmTNumber() + " | Room Number: " + this.getRoomNumber() + " | Confirmed Participants: ");
		this.printConfirmedParticipants();
		System.out.println();
	}
	
	public void printConfirmedParticipants()
	{
		for (InetAddress number : this.confirmedParticipants) 
		{
		    System.out.print(number + " | ");
		}
		
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	

}
