package Messeges;

import java.io.Serializable;

public class RoomChangeMessage implements Serializable
{
	private int mTNumber;
	private String newRoomNumber;
	
	public RoomChangeMessage()
	{
		this.mTNumber = 0;
		this.newRoomNumber = null;
	}
	
	public RoomChangeMessage(int mT, String rN) 
	{
		this.mTNumber = mT;
		this.newRoomNumber = rN;
	}

	public int getmTNumber() {
		return mTNumber;
	}

	public void setmTNumber(int mTNumber) {
		this.mTNumber = mTNumber;
	}

	public String getRoomNumber() {
		return newRoomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.newRoomNumber = roomNumber;
	}

	public void print()
	{
		System.out.println(" | Room Change Message: | MT: " + this.getmTNumber() + " | Room Number: "
				+ this.getRoomNumber());
	}
}
