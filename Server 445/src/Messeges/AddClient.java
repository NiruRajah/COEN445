package Messeges;

import java.io.Serializable;

public class AddClient implements Serializable
{
	private int mTNumber;
	
	public AddClient()
	{
		
	}
	
	public AddClient(int mT) 
	{
		this.setmTNumber(mT);
	}

	public int getmTNumber() {
		return mTNumber;
	}

	public void setmTNumber(int mTNumber) {
		this.mTNumber = mTNumber;
	}
	
	public void print()
	{
		System.out.println(" | Add Client: | MT: " + this.getmTNumber());
	}

}
