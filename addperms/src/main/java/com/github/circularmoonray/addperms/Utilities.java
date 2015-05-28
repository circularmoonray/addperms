package com.github.circularmoonray.addperms;

import org.bukkit.entity.Player;

public class Utilities {

	public static void sendOPMessage(String str){
		AddPerms plugin = AddPerms.instance;

		for ( Player player : plugin.getServer().getOnlinePlayers() ) {
			if ( player.isOp()) {
				player.sendMessage(str);
			}
		}
	}

	public static void sendEveryMessage(String str){
		AddPerms plugin = AddPerms.instance;

		for ( Player player : plugin.getServer().getOnlinePlayers() ) {
			player.sendMessage(str);
		}
	}
}
