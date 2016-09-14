package dbot.comm;

import static dbot.Poster.post;

import dbot.Database;
import dbot.UserData;
import sx.blah.discord.handle.obj.IUser;

public class FlipRoom extends Flip {
	private IUser uHost;
	private IUser uClient;
	private UserData dHost;
	private UserData dClient;
	private int pot;
	private int roomID;
	private String seite;
	private static int currentID = Database.getInstance().getServerData().getFlipRoomID();
	
	FlipRoom(IUser uHost, int bet, String seite, UserData dHost) {
		this.uHost = uHost;
		this.dHost = dHost;
		pot = bet;
		roomID = updateID();
		this.seite = seite;
	}
	
	
	void join(IUser uClient, UserData dClient) {
		this.uClient = uClient;
		this.dClient = dClient;
		roll();
	}
	
	private void roll() {//schlauer machen
		String flipSeite;
		if (Math.random() < 0.5) {
			flipSeite = "TOP";
		} else {
			flipSeite = "KEK";
		}
		if (seite.equals(flipSeite)) {
			post(uHost + " hat mit " + seite + " gegen " + uClient + " gewonnen und bekommt " + (pot * 2) + ":gem:!!");
			dHost.addGems(pot * 2);
		} else {
			post(uClient + " hat mit " + flipSeite + " gegen " + uHost + " gewonnen und bekommt " + (pot * 2) + ":gem:!!");
			dClient.addGems(pot * 2);
		}
	}
	
	private int updateID() {
		return currentID++;
	}//sollte dann ++currentID (nach serverData load-implementation)
	
	int getRoomID() {
		return roomID;
	}
	
	int getPot() {
		return pot;
	}
	
	IUser getHost() {
		return uHost;
	}

	UserData getHostData() {
		return dHost;
	}
	
	String getHostID() {
		return uHost.getID();
	}

	public static int getFlipRoomID() {
		return currentID;
	}
	
	@Override
	public String toString() {
		return "\nID�'" + roomID + "' Einsatz�'" + pot + "' Seite�'" + seite + "' Host�'" + uHost.getName() + "'";
	}
	
}
