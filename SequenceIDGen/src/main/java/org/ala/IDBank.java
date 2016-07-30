package org.ala;

import java.util.HashMap;
import java.util.Map;

import org.ala.core.ConfigUtil;


/**
 * @author Alagu Kumar
 *
 */
public class IDBank extends Thread {

	private static int rangeDif = ConfigUtil.getInstance().getRangeDif();
	private static int start = 1;

	
	/**
	 * Method to get range map that contain start and end integers,
	 * every time when it called it will generate next set of range sequence based on the range difference
	 * @return
	 */
	public static synchronized Map<String,Integer> getNextSeqRange(){
		Map<String,Integer>  rangeMap = new HashMap<String,Integer>();
		rangeMap.put("start", start);
		rangeMap.put("end", start + (rangeDif-1) );
		start = start + rangeDif;
		return rangeMap;
	}
}
