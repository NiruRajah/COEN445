package Messeges;

import java.io.Serializable;

public class CancelMessage implements Serializable
{
	private int mTNumber;
	private String reason;
	private int date;
	private int time;
	
	public CancelMessage()
	{
		this.mTNumber = 0;
		this.reason = null;
		this.setDate(0);
		this.setTime(0);
	}
	
	public CancelMessage(int mT, String reason, int date, int time) 
	{
		this.mTNumber = mT;
		this.reason = reason;
		this.setDate(date);
		this.setTime(time);
	}

	public int getmTNumber() {
		return mTNumber;
	}

	public void setmTNumber(int mTNumber) {
		this.mTNumber = mTNumber;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void print()
	{
		System.out.println(" | Cancel Message: | MT: " + this.getmTNumber() + " | Reason: " + this.getReason());
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
