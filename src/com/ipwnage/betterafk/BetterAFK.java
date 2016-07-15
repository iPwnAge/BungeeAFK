package com.ipwnage.betterafk;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BetterAFK  extends JavaPlugin implements Listener, PluginMessageListener {
	public static final Logger log = Logger.getLogger("Minecraft");
	AFKListeners events;
	HashMap<String, Long> playerActivityDB;
	HashMap<String, Boolean> playerAfkDB;
	public boolean RunningChecker;

	@Override
	public void onEnable() {
		events = new AFKListeners(this);
		playerActivityDB = new HashMap<String, Long>();
		playerAfkDB = new HashMap<String, Boolean>();
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
		this.getServer().getPluginManager().registerEvents(events, this);
		BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.runTaskTimerAsynchronously(this, new CheckForAFKRunnable(this), 0, 20L);
		playerActivityDB.put("activity_counter_123", getCurrentTime());
	}






	public void onPluginMessageReceived(String channel, Player player, byte[] message){
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		if (subchannel.equals("BetterAFK")) {
			String submessage = in.readUTF();
			getServer().broadcastMessage(ChatColor.GRAY + submessage);
		}
	}

	public void isNowAFK(String name) {


	}

	public Boolean isAFK(String name) {
		return playerAfkDB.get(name);
	}

	public void setAFK(String name, Boolean status) {
		if(playerAfkDB.containsKey(name) && !playerAfkDB.get(name) && status) {
			AFKBroadcast(name, true);
		} else if(playerAfkDB.containsKey(name) && playerAfkDB.get(name) && !status){
			AFKBroadcast(name, false);
			playerActivityDB.put(name, 0L);
		} else {
			playerActivityDB.put(name, 0L);
		}
		playerAfkDB.put(name, status);
	}


	private void AFKBroadcast(String name, boolean afk) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Forward");
		out.writeUTF("ONLINE");
		out.writeUTF("BetterAFK");
		String stream;
		if(afk) {
			stream = name + " is now AFK.";
		} else {
			stream = name + " has returned.";
		}

		byte[] data = stream.getBytes();
		out.writeShort(data.length);
		out.write(data);
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		getServer().broadcastMessage(ChatColor.GRAY + stream);
		player.sendPluginMessage(this, "BungeeCord", out.toByteArray());

	}


	public Long getCurrentTime() {
		return (System.currentTimeMillis() / 1000L);
	}

}
