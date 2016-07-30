package org.ala.core;


/**
 * @author Alagu Kumar
 * 
 * Singleton class which will holds the range diff
 *
 */
public class ConfigUtil {

	private static int rangeDif = 1000;
	private static ConfigUtil instance = null;
	
	public  int getRangeDif() {
		return rangeDif;
	}

	public  void setRangeDif(int rangeDif) {
		ConfigUtil.rangeDif = rangeDif;
	}
	
	public static ConfigUtil getInstance() {
		if(instance == null) {
			synchronized(ConfigUtil.class) {
				if(instance == null) {
					instance = new ConfigUtil();
				}
			}    
		}
		return instance;
	}

	private ConfigUtil() {
		
	}
}
