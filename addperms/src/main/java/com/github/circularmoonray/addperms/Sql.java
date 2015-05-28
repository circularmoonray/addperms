package com.github.circularmoonray.addperms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//MySQL操作関数
public class Sql{

	private final String url, db, id, pw;

	private Connection con = null;
	private Statement stmt = null;
	private ResultSet result = null;
	public static String exc;
	private HashMap<String, String> commands;


	Sql(String url, String db, String id, String pw){
		this.url = url +  "/" + db;
		this.db = db;
		this.id = id;
		this.pw = pw;

		String command = "";
		commands = new HashMap<String, String>();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}

		//sql鯖への接続とdb作成
		connect(url, id, pw);

	}

	/**
	 * 接続関数
	 *
	 * @param url 接続先url
	 * @param id ユーザーID
	 * @param pw ユーザーPW
	 * @return
	 */
	private boolean connect(String url, String id, String pw){
		try {
			con = (Connection) DriverManager.getConnection(url, id, pw);
			stmt = con.createStatement();
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
		}
		return true;
	}

	/**
	 * テーブル作成
	 * 失敗時には変数excにエラーメッセージを格納
	 *
	 * @param table テーブル名
	 * @return 成否
	 */
	public boolean createTable(String table){
		String command = "";
		command = "create table " + table +
				"(id int auto_increment unique, " +
				"name varchar(30), " +
				"stone int default 0, " +
				"netherrack int default 0, " +
				"gravel int default 0, " +
				"dirt int default 0, " +
				"death int default 0, " +
				"break_dPickaxe int default 0, " +
				"use_seed int default 0, " +
				"uuid varchar(128) unique);";
		try{
			stmt.execute(command);
			return true;
		} catch (SQLException e) {
			exc = e.getMessage();
		}
		return false;
	}

	/**
	 * 複数のコマンドをセットします。引数が無い場合、NullPointerExceptionを返します
	 * @param key
	 * @param s
	 * @throws NullPointerException
	 */
	public void setCommands(String key, int stat)throws NullPointerException{
		if(key.isEmpty()){
			throw new NullPointerException("setCommands : NullException");
		}

		commands.put(key, String.valueOf(stat));
	}


	/**
	 * 複数のコマンドを出力します。引数かcommandsが無い場合、NullPointerExceptionを返します
	 * @param uuid
	 * @param s
	 * @throws NullPointerException
	 */
	public boolean insertCommands(String table, String uuid)throws NullPointerException{
		if((table.isEmpty() || uuid.isEmpty())){
			throw new NullPointerException("insertCommands : NullException");
		}else if(commands.isEmpty()){
			throw new NullPointerException("insertCommands : NotFoundCommands");
		}

		String commandf = "insert into " + table + "(";
		String commandm = "uuid) values(";
		String commandr = "'" + uuid + "')" + " on duplicate key update ";
		String command = "";

		//command:
		//insert into @table(@key..., uuid) values(@s..., '@struuid')
		// on duplicate key update @key=@s, ...
		for(Map.Entry<String, String> com : commands.entrySet()){

			//insert into @table(@key...,
			commandf += com.getKey()   + ",";
			//uuid) values(@s...,
			commandm += com.getValue() + ",";
			//'@struuid') on duplicate key update @key=@s, ...
			commandr += com.getKey() + "=" + com.getValue() + ",";
		}

		command = commandf + commandm + commandr.substring(0, commandr.length() - 1);

		commands.clear();
		return putCommand(command);
	}

	/**
	 * データの挿入・更新(int)
	 * 失敗時には変数excにエラーメッセージを格納
	 *
	 * @param table テーブル名
	 * @param key カラム名
	 * @param stat 挿入するデータ
	 * @param uuid キャラのuuid
	 * @return 成否
	 */
	public boolean insert(String table, String key, int stat, UUID uuid){
		String command = "";
 		String struuid = uuid.toString();
 		String s = String.valueOf(stat);

 		//command:
 		//insert into @table(@key, uuid) values(@stat, '@struuid')
 		// on duplicate key update @key=@stat
 		command = "insert into " +  table +
 				"(" + key + ", uuid) values(" +
 				s + ", '" + struuid + "')" +
 				" on duplicate key update " + key + "=" + s;

 		return putCommand(command);
	}

	/**
	 * データの挿入・更新(string)
	 * 失敗時には変数excにエラーメッセージを格納
	 *
	 * @param table テーブル名
	 * @param key カラム名
	 * @param s 挿入する文字列
	 * @param uuid キャラのuuid
	 * @return 成否
	 */
	public boolean insert(String table, String key, String s, UUID uuid){
		String command = "";
		String struuid = uuid.toString();

		//command:
		//insert into @table(@key, uuid) values('@s', '@struuid')
		// on duplicate key update @key='@s'
		command = "insert into " +  table +
				"(" + key + ", uuid) values('" +
				s + "', '" + struuid + "')" +
				" on duplicate key update " + key + "='" + s + "'";

		return putCommand(command);
	}

	/**
	 * コマンド出力関数
	 * @param command コマンド内容
	 * @return 成否
	 */
	private boolean putCommand(String command){
		try {
			stmt.executeUpdate(command);
			return true;
		} catch (SQLException e) {
			//接続エラーの場合は、再度接続後、コマンド実行
			exc = e.getMessage();
			connect(url, id, pw);
			try {
				stmt.executeUpdate(command);
				return true;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * コネクション切断処理
	 *
	 * @return 成否
	 */
	public boolean disconnect(){
	    if (con != null){
	    	try{
	    		stmt.close();
				con.close();
	    	}catch (SQLException e){
	    		e.printStackTrace();
	    		return false;
	    	}
	    }
	    return true;
	}

	public boolean createDB(String str){
		boolean re;
		if(str==null){
			return false;
		}

		try {
			re = stmt.execute("CREATE DATABASE IF NOT EXISTS " + str);
			return re;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public HashMap<> resultCommand(Config config, String command){
		int stone;

		try{

		}
		return result;
	}
}