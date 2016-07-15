package com.ipwnage.betterafk;

import java.util.Map.Entry;

public class CheckForAFKRunnable implements Runnable {

	private final BetterAFK plugin;

	public CheckForAFKRunnable(BetterAFK plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		if(plugin.RunningChecker) {
			return;
		} else {
			plugin.RunningChecker = true;
		}
		Long sinceLastCheck = plugin.getCurrentTime() - plugin.playerActivityDB.get("activity_counter_123");
		plugin.playerActivityDB.replaceAll((name, time) -> time + sinceLastCheck);
		for (Entry<String, Long> entry: plugin.playerActivityDB.entrySet()) {
		//	plugin.log.info("Checking player " + entry.getKey() + ", they're current time away is: " + entry.getValue());
		 //   plugin.log.info("Player is currently: " + plugin.isAFK(entry.getKey()));
			if(entry.getValue() > 300) {
				plugin.setAFK(entry.getKey(), true);
			}
		}
		plugin.playerActivityDB.put("activity_counter_123", plugin.getCurrentTime());
		plugin.RunningChecker = false;
	}

}
