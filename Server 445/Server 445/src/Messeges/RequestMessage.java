package Messeges;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

import data.Connection;

@SuppressWarnings("serial")
public class RequestMessage implements Serializable
{
	private int rQNumber;
	private int date;
	private int time;
	private int minimum;
	private ArrayList<InetAddress> listOfParticipants;
	private String topic;
	
	public RequestMessage()
	{
		this.rQNumber = 0;
		this.date = 0;
		this.time = 0;
		this.minimum = 0;
		this.listOfParticipants = new ArrayList<InetAddress>();
		this.topic = null;
	}
	
	public RequestMessage(int rQ, int date, int time, int minimum, ArrayList<InetAddress> list, String topic)
	{
		this.rQNumber = rQ;
		this.date = date;
		this.time = time;
		this.minimum = minimum;
		this.listOfParticipants = list;
		this.topic = topic;
	}
	
	public int getRQ() {
		return rQNumber;
	}
	public void setRQ(int rQ) {
		rQNumber = rQ;
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
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void print()
	{
		System.out.print(" | Request Message: | RQ: " + this.getRQ() + " | date: " + this.getDate() 
		  + " | time: " + this.getTime() + " | minimum: " + this.getMinimum()
		  + " | participants: ");
		this.printListOfParticipants();
		System.out.println("topic: "
		  + this.getTopic());
	}
	
	public void printListOfParticipants()
	{
		for (int i = 0; i < this.listOfParticipants.size(); i++) 
		{
		    System.out.print(this.listOfParticipants.get(i) + " | ");
		}
	}

	public ArrayList<InetAddress> getListOfParticipants() {
		return listOfParticipants;
	}

	public void setListOfParticipants(ArrayList<InetAddress> listOfParticipants) {
		this.listOfParticipants = listOfParticipants;
	}
	
}
