package Messeges;

import java.io.Serializable;

public class ConfirmMessage implements Serializable
{
	private int mTNumber;
	private String roomNumber;
	private int date;
	private int time;
	
	public ConfirmMessage()
	{
		this.mTNumber = 0;
		this.roomNumber = null;
		this.setDate(0);
		this.setTime(0);
	}
	
	public ConfirmMessage(int mT, String rN, int date, int time) 
	{
		this.mTNumber = mT;
		this.roomNumber = rN;
		this.setDate(date);
		this.setTime(time);
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
	
	public void print()
	{
		System.out.println(" | Confirm Message: | MT: " + this.getmTNumber() + " | Room Number: "
				+ this.getRoomNumber());
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
