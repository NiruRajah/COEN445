package Rooms;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;

public class Meetings implements Serializable
{
	private InetAddress requester;
	private ArrayList<InetAddress> totalClients = new ArrayList<InetAddress>();
	private ArrayList<InetAddress> confirmedClients = new ArrayList<InetAddress>();
	private ArrayList<InetAddress> declinedClients = new ArrayList<InetAddress>();
	private int minimum;
	private int acceptCounter;
	private int totalCounter;
	private int mT;
	private int date;
	private int time;
	private String topic;
	private int rQ;
	private String roomNumber;
	private int roomNumberIndex;
	
	public Meetings()
	{
		
	}
	
	public Meetings(InetAddress addr, ArrayList<InetAddress> connections, int minimum, int mT, int date, int time,
			String topic, int rQ)
	{
		this.requester = addr;
		this.totalClients = connections;
		this.minimum = minimum;
		this.mT = mT;
		this.date = date;
		this.time = time;
		this.topic = topic;
		this.setrQ(rQ);
		
	}
	
	public InetAddress getRequester()
	{
		return this.requester;
	}
	
	public ArrayList<InetAddress> getTotalClients()
	{
		return this.totalClients;
		
	}

	public int getMinimum() {
		return minimum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getAcceptCounter() {
		return acceptCounter;
	}

	public void incrementAcceptCounter() {
		this.acceptCounter++;;
	}
	public void decrementAcceptCounter() {
		this.acceptCounter--;;
	}

	public int getTotalCounter() {
		return totalCounter;
	}

	public void incrementTotalCounter() {
		this.totalCounter++;
	}
	public void decrementTotalCounter() {
		this.totalCounter--;
	}

	public int getmT() {
		return mT;
	}

	public void setmT(int mT) {
		this.mT = mT;
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

	public int getrQ() {
		return rQ;
	}

	public void setrQ(int rQ) {
		this.rQ = rQ;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public int getRoomNumberIndex() {
		return roomNumberIndex;
	}

	public void setRoomNumberIndex(int roomNumberIndex) {
		this.roomNumberIndex = roomNumberIndex;
	}

	public ArrayList<InetAddress> getConfirmedClients() {
		return confirmedClients;
	}

	public void setConfirmedClients(ArrayList<InetAddress> confirmedClients) {
		this.confirmedClients = confirmedClients;
	}

	public ArrayList<InetAddress> getDeclinedClients() {
		return declinedClients;
	}

	public void setDeclinedClients(ArrayList<InetAddress> declinedClients) {
		this.declinedClients = declinedClients;
	}

}
