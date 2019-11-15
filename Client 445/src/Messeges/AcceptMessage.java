package Messeges;

import java.io.Serializable;

public class AcceptMessage implements Serializable
{
	private int mTNumber;
	
	public AcceptMessage()
	{
		this.mTNumber = 0;
	}
	
	public AcceptMessage(int mT) 
	{
		this.mTNumber = mT;
	}


	public int getmTNumber() {
		return mTNumber;
	}


	public void setmTNumber(int mTNumber) {
		this.mTNumber = mTNumber;
	}
	
	public void print()
	{
		System.out.println(" | Accept Message: | MT: " + this.getmTNumber());
	}

}
