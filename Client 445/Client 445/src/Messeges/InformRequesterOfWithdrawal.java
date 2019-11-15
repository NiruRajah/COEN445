package Messeges;

import java.io.Serializable;
import java.net.InetAddress;

public class InformRequesterOfWithdrawal implements Serializable
{
	private int mTNumber;
	private InetAddress iPAddress;
	
	public InformRequesterOfWithdrawal()
	{
		this.mTNumber = 0;
		this.iPAddress = null;
	}
	public InformRequesterOfWithdrawal(int mT, InetAddress iP) 
	{
		this.setmTNumber(mT);
		this.setiPAddress(iP);
	}

	public int getmTNumber() {
		return mTNumber;
	}

	public void setmTNumber(int mTNumber) {
		this.mTNumber = mTNumber;
	}

	public InetAddress getiPAddress() {
		return iPAddress;
	}

	public void setiPAddress(InetAddress iPAddress) {
		this.iPAddress = iPAddress;
	}
	
	public void print()
	{
		System.out.println(" | Inform Requester Of Withdrawal: | MT: " + this.getmTNumber() + " | IP: " 
				+ this.getiPAddress());
	}

}
