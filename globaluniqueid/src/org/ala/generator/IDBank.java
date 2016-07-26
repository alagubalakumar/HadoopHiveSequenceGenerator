package org.ala.generator;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.ala.server.ConfigUtil;

public class IDBank extends Thread {

	private static Stack<Map<String,Integer>> st = new Stack<>();
	private static int rangeDif = ConfigUtil.getInstance().getRangeDif();
	private static int start = 1;

	
	
	public static void genSeqRange(int rangeDif,int noOfSplits){
		Map<String,Integer> rangeMap = new HashMap<>();
		for(int i=1;i<rangeDif*noOfSplits;i++){
			rangeMap = new HashMap<>();
			rangeMap.put("start", i);
			rangeMap.put("end", i=i+(rangeDif-1));
			st.push(rangeMap);
		}
	}
	

	public static synchronized Map<String,Integer> getNextSeqRange(){
		Map<String,Integer>  rangeMap = new HashMap<>();
		rangeMap.put("start", start);
		rangeMap.put("end", start + (rangeDif-1) );
		start = start + rangeDif;
		return rangeMap;
	}
}
