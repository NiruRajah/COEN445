package Messeges;

import java.io.Serializable;

public class WithdrawMessage implements Serializable
{
	private int mTNumber;
	
	public WithdrawMessage()
	{
		this.mTNumber = 0;
	}
	
	public WithdrawMessage(int mT) 
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
		System.out.println(" | Withdraw Message: | MT: " + this.getmTNumber());
	}
	
}
