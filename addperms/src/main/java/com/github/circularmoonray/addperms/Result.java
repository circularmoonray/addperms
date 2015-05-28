package com.github.circularmoonray.addperms;

import java.util.HashMap;

public class Result {
	HashMap<String, Integer> resultset;

	public Result(Config config){
		resultset = new HashMap<String, Integer>(config.getResultset());
	}

	//新たな値を古い値と掛けて挿入します
	public boolean calcPut(String key, int param){
		int i;
		if(resultset.containsKey(key)){
			i = resultset.get(key);
			resultset.put(key, i * param);
			return true;
		}else{
			return false;
		}
	}

	//resultsetのパラメーターの合計値を出します
	public int calcSum(){
		int i = 0;
		for (String key : resultset.keySet()) {
			if (resultset.get(key) >= 0) {
				i += resultset.get(key);
			}
		}
		return i;

	}
}
