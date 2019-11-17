package Messeges;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class NegativeResponseToRequester implements Serializable
{
	private int rQNumber;
	private int date;
	private int time;
	private int minimum;
	private ArrayList<InetAddress> confirmedParticipants;
	private String topic;
	private int mTNumber;
	
	public NegativeResponseToRequester()
	{
		
	}
	
	public NegativeResponseToRequester(int rQ, int date, int time, int minimum, 
			ArrayList<InetAddress> confirmedClients, String topic, int mT) 
	{
		this.rQNumber = rQ;
		this.date = date;
		this.time = time;
		this.minimum = minimum;
		this.confirmedParticipants = confirmedClients;
		this.topic = topic;
		this.mTNumber = mT;
	}

	public int getRQ() {
		return rQNumber;
	}

	public void setRQ(int rQNumber) {
		this.rQNumber = rQNumber;
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

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public ArrayList<InetAddress> getConfirmedCLients() {
		return confirmedParticipants;
	}

	public void setConfirmedCLients(ArrayList<InetAddress> confirmedCLients) {
		this.confirmedParticipants = confirmedCLients;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void print()
	{
		System.out.print(" | Negative Reponse To Requester: | RQ: " + this.getRQ() + " | date: " + this.getDate() 
		  + " | time: " + this.getTime() + " | minimum: " + this.getMinimum()
		  + " | clients: ");
		this.printConfirmedParticipants();
		System.out.println("topic: "
		  + this.getTopic());
	}
	
	public void printConfirmedParticipants()
	{
		for (InetAddress number : this.confirmedParticipants) 
		{
		    System.out.print(number + " | ");
		}
	}

	public int getmTNumber() {
		return mTNumber;
	}

	public void setmTNumber(int mTNumber) {
		this.mTNumber = mTNumber;
	}
}
