package Rooms;

import java.io.Serializable;

public class Room implements Serializable
{
	
	private Meetings[][][] bookedRoom = new Meetings [8][25][2];

	public Room()
	{
		for(int i = 1; i <= 7; i++)
		{
			for(int j = 1; j <= 24; j++)
			{
				for(int z = 0; z < 2; z++)
				{
					
					this.bookedRoom[i][j][z] = new Meetings();
				}
			}
		}
	}
	
	public Room(Meetings[][][] request)
	{
		this.bookedRoom = request;
	}
	
	public Meetings[][][] getBookedRoom()
	{
		return this.bookedRoom;
	}
	
	public synchronized int add(int date, int time, Meetings r1)
	{
		if(this.bookedRoom[date][time][0].getRequester() == null)
		{
			this.bookedRoom[date][time][0] = r1;
			return 1;
		}
		else if(this.bookedRoom[date][time][1].getRequester() == null)
		{
			this.bookedRoom[date][time][1] = r1;
			return 2;
		}
		return 0;
	}
	
	public synchronized void delete(int date, int time, int index)
	{
		this.bookedRoom[date][time][index] = new Meetings();
	}
	
	public synchronized boolean checkRoomIsFree(int date, int time)
	{
		if(bookedRoom[date][time][0].getRequester() == null)
		{
			return true;
		}
		else if(bookedRoom[date][time][1].getRequester() == null)
		{
			return true;
		}
		return false;
	}
	public synchronized int changeRoom(int date, int time, int index, Meetings r1)
	{
		if(index == 0)
		{
			this.bookedRoom[date][time][1] = r1;
			this.bookedRoom[date][time][0] = new Meetings();
			return 2;
		}
		else if(index == 1)
		{
			this.bookedRoom[date][time][0] = r1;
			this.bookedRoom[date][time][1] =  new Meetings();
			return 1;
		}
	return 0;
	}
}
