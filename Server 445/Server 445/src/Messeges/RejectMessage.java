package Messeges;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RejectMessage implements Serializable
{
	private int mTNumber;
	
	public RejectMessage()
	{
		this.mTNumber = 0;
	}
	
	public RejectMessage(int mT) 
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
		System.out.println(" | Reject Message: | MT: " + this.getmTNumber());
	}
}
