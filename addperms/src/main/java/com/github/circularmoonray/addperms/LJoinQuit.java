package com.github.circularmoonray.addperms;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LJoinQuit implements Listener {

	private AddPerms plugin;

	public LJoinQuit(AddPerms plugin){
		this.setPlugin(plugin);

	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){


	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();

	}

	//getter & setter
	public AddPerms getPlugin() {
		return plugin;
	}

	public void setPlugin(AddPerms plugin) {
		this.plugin = plugin;
	}

}
