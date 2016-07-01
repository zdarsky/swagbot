package dbot.comm;

import dbot.Poster;

import sx.blah.discord.handle.obj.IUser;
import java.util.List;
import java.util.*;
import dbot.UserData;

public class Flip {
	
	private static List<FlipRoom> lRooms = new ArrayList<FlipRoom>();
	protected static Poster pos;
	
	public Flip(Poster pos) {
		this.pos = pos;
	}
	
	public void m(IUser author, String arg, String seite, UserData uData) {//unendlichen post mit edit einfuegen
		if (arg.equals("close")) {
			System.out.println("close");
			close(author, uData);
		}
		else if (!containsUser(author)) {
			try {
				int bet = Integer.parseInt(arg);
				if ((uData.getGems() < bet) || (bet < 1)) {
					pos.post("zu wenig :gem:");
					return;
				}
				if (seite.equals("top") || seite.equals("kek")) {
					uData.subGems(bet);
					open(author, bet, seite, uData);
				}
				else {
					double rnd = Math.random();
					if (rnd < 0.5) {
						uData.subGems(bet);
						open(author, bet, "top", uData);
					}
					else {
						uData.subGems(bet);
						open(author, bet, "kek", uData);
					}
				}
			} catch(Exception e) {
				System.out.println("parseerror (flip.m)");
				/*if (arg.equals("join")) {
					System.out.println("join versucht " + argid);
					join(author, argid);
					
					//FlipRoom fr = getRoomByID(argid);
					int testid = -1;
					try {
						testid = Integer.parseInt(argid);
					} catch(Exception ex) {
						System.out.println("deletefail" + ex);
					}
					lRooms.remove(getRoomIndexByID(testid));
					System.out.println("join durch");
				}*/
				
				
			}
			
		}
		
		else {
			pos.post("schon vorhanden");
		}
	}
	
	public void join(IUser author, String roomID, UserData uData) {
		try {
			System.out.println(roomID);
			int iRoomID = Integer.parseInt(roomID);
			FlipRoom gettedRoom = lRooms.get(getRoomIndexByID(iRoomID));
			if (gettedRoom != null) {
				if (uData.getGems() < gettedRoom.getPot()) {
					pos.post("zu wenig :gem:");
					return;
				}
				uData.subGems(gettedRoom.getPot());
				gettedRoom.join(author, uData);
				lRooms.remove(getRoomIndexByID(iRoomID));
				//pos.post(winner + " hat den Pot gewonnen!");
			}
			
			else {
				pos.post("Room-ID nicht gefunden.");
			}
		} catch(Exception e) {
			System.out.println("parseerror (flip.join)");
		}
	}
	
	private void open(IUser author, int bet, String seite, UserData uData) {
		FlipRoom fRoom = new FlipRoom(author, bet, seite, uData);
		pos.post(author + " hat neuen Raum um " + fRoom.getPot() + ":gem: geöffnet mit ID: " + fRoom.getRoomID() + " (" + seite.toUpperCase() + ")", 600000);//bestehenden Post posten(ggf. editieren)
		lRooms.add(fRoom);
	}
	
	public void close(IUser author, UserData uData) {
		for (int i = 0; i < lRooms.size(); i++) {
			if (lRooms.get(i).getHostID().equals(author.getID())) {
				System.out.println(lRooms.get(i).getPot());
				uData.addGems(lRooms.get(i).getPot());
				pos.post("closing room " + lRooms.get(i).getRoomID());
				lRooms.remove(i);
				
				System.out.println("removed room");
			}
		}
		//remove room(author)//FEHLT
	}
	
	private boolean containsUser(IUser user) {
		if (user == null) {
			throw new IllegalArgumentException("User darf nicht null sein!");//braucht return?
		}
		for (int i = 0; i < lRooms.size(); i++) {
				if (lRooms.get(i).getHostID().equals(user.getID())) {
					return true;
				}
			}
		return false;
	}
	
	private boolean containsRoom(int roomID) {
		for (int i = 0; i < lRooms.size(); i++) {
			if (lRooms.get(i).getRoomID() == roomID) {
				return true;
			}
		}
		return false;
	}
	
	private FlipRoom getRoomByID(int roomID) {
		for (int i = 0; i < lRooms.size(); i++) {
			if (lRooms.get(i).getRoomID() == roomID) {
				return lRooms.get(i);
			}
		}
		return null;
	}
	
	private int getRoomIndexByID(int roomID) {
		for (int i = 0; i < lRooms.size(); i++) {
			if (lRooms.get(i).getRoomID() == roomID) {
				return i;
			}
		}
		return -1;
	}
}
