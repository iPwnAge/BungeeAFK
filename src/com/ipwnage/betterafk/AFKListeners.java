package com.ipwnage.betterafk;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFKListeners implements Listener {
	BetterAFK plugin;

	public AFKListeners(BetterAFK plugin)  {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		plugin.setAFK(e.getPlayer().getName(), false);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		String playername = e.getPlayer().getName();
		plugin.playerActivityDB.put(playername, 0L);
		plugin.playerAfkDB.put(playername, false);
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		String playername = e.getPlayer().getName();
		plugin.playerActivityDB.remove(playername);
		plugin.playerAfkDB.remove(playername);
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		plugin.setAFK(e.getPlayer().getName(), false);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		plugin.setAFK(e.getPlayer().getName(), false);
	}

	@EventHandler
	public void onPlayerBedLeave(PlayerBedLeaveEvent e){
		plugin.setAFK(e.getPlayer().getName(), false);
	}
	


}

