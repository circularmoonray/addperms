package com.github.circularmoonray.addperms;

import java.util.HashMap;

import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class AddPerms extends JavaPlugin{
	public static AddPerms instance;

	private Config config;

	private HashMap<String, TabExecutor> commands;


	public void onEnable(){
		instance = this;

		// コンフィグのロード
		setConfig(Config.loadConfig());
		getLogger().info("config load completed");

		// コマンドの登録
		commands = new HashMap<String, TabExecutor>();
		commands.put("addperms", new CAddPerms(this));

		// リスナーの登録
		getServer().getPluginManager()
				.registerEvents(new LJoinQuit(this), this);
	}

	public Config getconfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

}
