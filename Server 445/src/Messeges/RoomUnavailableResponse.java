package Messeges;

import java.io.Serializable;

public class RoomUnavailableResponse implements Serializable
{
	private int rQNumber;
	private String unavailable;
	
	public RoomUnavailableResponse()
	{
		this.rQNumber = 0;
		this.unavailable = null;
	}
	
	public RoomUnavailableResponse(int rQ, String unavailable)
	{
		this.rQNumber = rQ;
		this.unavailable = unavailable;
	}
	
	public int getRQ() {
		return rQNumber;
	}
	public void setRQ(int rQ) {
		rQNumber = rQ;
	}
	public String getUnavailable() {
		return unavailable;
	}
	public void setUnavailable(String unavailable) {
		this.unavailable = unavailable;
	}
	
	public void print()
	{
		System.out.println(" | Room Unavailable Message: | RQ: " + this.getRQ()
		+ " | " + this.getUnavailable());
	}
}
