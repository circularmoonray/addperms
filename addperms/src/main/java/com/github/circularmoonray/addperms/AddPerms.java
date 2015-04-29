package com.github.circularmoonray.addperms;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AddPerms extends JavaPlugin{
	public void onEnable(){
		getServer().getPluginManager().registerEvents(paramListener, paramPlugin);
	}

}
