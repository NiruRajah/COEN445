package Messeges;

import java.io.Serializable;

public class CancelMessageFromRequester implements Serializable
{
	private int mTNumber;
	
	public CancelMessageFromRequester()
	{
		this.mTNumber = 0;
	}
	
	public CancelMessageFromRequester(int mT) 
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
		System.out.println(" | Cancel Message From Requester: | MT: " + this.getmTNumber());
	}

}
