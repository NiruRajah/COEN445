package Messeges;

import java.io.Serializable;
import java.net.InetAddress;

public class InviteMessage implements Serializable
{
	private int mTNumber;
	private int date;
	private int time;
	private String topic;
	private InetAddress requesterIP;
	
	public InviteMessage()
	{
		this.mTNumber = 0;
		this.date = 0;
		this.time = 0;
		this.topic = null;
		this.requesterIP = null;
	}
	
	public InviteMessage(int mT, int date, int time, String topic, InetAddress inetAddress) 
	{
		this.mTNumber = mT;
		this.date = date;
		this.time = time;
		this.topic = topic;
		this.requesterIP = inetAddress;
	}


	public int getMT() {
		return mTNumber;
	}


	public void setMT(int mT) {
		mTNumber = mT;
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


	public String getTopic() {
		return topic;
	}


	public void setTopic(String topic) {
		this.topic = topic;
	}


	public InetAddress getRequesterIP() {
		return requesterIP;
	}


	public void setRequesterIP(InetAddress requesterIP) {
		this.requesterIP = requesterIP;
	}
	
	public void print()
	{
		System.out.println(" | Invite Message: | MT: " + this.getMT() + " | Date: "
				+ this.getDate() +" | Time: " + this.getTime() + " | Topic: " + this.getTopic()
				+ " | RequesterIP: " + this.getRequesterIP());
	}

}
