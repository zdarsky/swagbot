package dbot.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IMessage;

import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;

public class DelTimer implements Runnable {//TODO: vielleicht statt neuen Threads messages in list und �ber MainTimer l�schen
	private static final Logger LOGGER = LoggerFactory.getLogger("dbot.timer.DelTimer");
	private IMessage message = null;
	private int duration = 60000;
	
	public DelTimer(IMessage message, int duration) {
		this.message = message;
		this.duration = duration;
		Thread tDelTimer = new Thread(this, "DelTimer Thread");
		tDelTimer.start();
	}

	@Override
	public void run() {
		try {
			if (message != null) {
				Thread.sleep(duration);
				message.delete();
			} else {
				LOGGER.warn("tried to delete null-message");
			}
		} catch(MissingPermissionsException | RateLimitException | InterruptedException | DiscordException e) {
			LOGGER.error("Error while deleting message", e);
		}
	}
}
