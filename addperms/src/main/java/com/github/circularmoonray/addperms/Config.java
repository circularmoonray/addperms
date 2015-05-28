package com.github.circularmoonray.addperms;

import static com.github.circularmoonray.addperms.Param.*;

import java.io.File;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private String db;
	private String id;
	private String pw;
	private String url;
	private HashMap<String, Integer> resultset;

	Config(){
		url = "jdbc:mysql://";
		resultset = new HashMap<String, Integer>();
	}

	public String getDB(){
		return db;
	}

	public String getID(){
		return id;
	}

	public String getPW(){
		return pw;
	}

	public String getURL(){
		return url;
	}

	public static Config loadConfig(){
		Config tconfig = new Config();

		//ファイル位置の取得
		File configFile = new File(AddPerms.instance.getDataFolder(), "config.yml");

		//存在しない場合はコピー
		if ( !configFile.exists() ) {
			AddPerms.instance.getConfig().options().copyDefaults(true);
			AddPerms.instance.saveConfig();
			AddPerms.instance.reloadConfig();
		}

		//コンフィグファイルの取得
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		tconfig.db = config.getString("db");
		tconfig.id = config.getString("id");
		tconfig.pw = config.getString("pw");

		tconfig.url += config.getString("host");
		if(!config.getString("port").isEmpty()){
			tconfig.url += ":" + config.getString("port");
		}

		for (String str : statlist) {
			tconfig.getResultset().put(str, config.getInt(str));
		}

		return tconfig;
	}

	public HashMap<String, Integer> getResultset() {
		return resultset;
	}
}
