package dbot;

import dbot.timer.DelTimer;

//import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IChannel;

import sx.blah.discord.api.IDiscordClient;

import sx.blah.discord.util.HTTP429Exception;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;

import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.RequestBuffer.RequestFuture;

//import java.util.*;
import java.util.concurrent.*;

public class Poster {
	private static IDiscordClient bClient;
	private static IGuild guild;
	private static IChannel channel;
	//private IMessage message = null;
	
	public Poster() {//abfrage if bClient, guild == null??
	}
	
	public Poster(IDiscordClient bClient, IGuild guild, IChannel channel) {
		this.bClient = bClient;
		this.guild = guild;
		this.channel = channel;
	}
	
	public Future<IMessage> post(String s, int duration) {
		return RequestBuffer.request(() -> {
			try {
				IMessage message = new MessageBuilder(bClient).withChannel(channel).withContent(s).build();
				if (duration > 0) {//oder -1?
					new DelTimer(message, duration);
				}
				return message;
			} catch(MissingPermissionsException e) {
				System.out.println("MissingPermEX: Poster.post+dur");
			} catch(DiscordException e) {
				System.out.println("DiscordEX: Poster.post+dur");
			}
			return null;
		});
		//return null;//notwendig??
	}
	
	public Future<IMessage> post(String s) {
		//Future<IMessage> message;
		return RequestBuffer.request(() -> {
			try {
				IMessage message = new MessageBuilder(bClient).withChannel(channel).withContent(s).build();
				new DelTimer(message);
				return message;
			} catch(MissingPermissionsException e) {
				System.out.println("MissingPermEX: Poster.post");
			} catch(DiscordException e) {
				System.out.println("DiscordEX: Poster.post");
			}
			return null;
		});
		//return null;//notwendig??//TODO:return ist immer null, fail
	}
	
	public void del(IMessage message) {
		RequestBuffer.request(() -> {
			try {
				message.delete();
			} catch(MissingPermissionsException e) {
				System.out.println("MissingPermEX: Poster.del");
			}  catch(DiscordException e) {
				System.out.println("DiscordEX: Poster.del");
			}
		});
	}
	
	public void del(IMessage message, int duration) {
		new DelTimer(message, duration);
	}
	
	public Future<IMessage> edit(IMessage message, String s) {
		return RequestBuffer.request(() -> {
			try {
				message.edit(s);
				return message;
			} catch(MissingPermissionsException e) {
				System.out.println("MissingPermEX: Poster.edit");
			} catch(DiscordException e) {
				System.out.println("DiscordEX: Poster.edit");
			}
			return null;
		});
		//return null;
	}
	
}