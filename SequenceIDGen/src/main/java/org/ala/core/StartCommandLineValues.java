package org.ala.core;

import org.kohsuke.args4j.Option;

public class StartCommandLineValues {

	@Option(name = "-sp", aliases = { "--serverport" }, required = true,
            usage = "port of the server")
    private int port;
	
	@Option(name = "-r", aliases = { "--range" }, required = true,
            usage = "range difference between start and end ")
    private int range;
	
	@Option(name = "-mp", aliases = { "--monitorport" }, required = true,
            usage = "monitor port which is used to stop the  server")
    private int monitorport;
	

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getMonitorport() {
		return monitorport;
	}

	public void setMonitorport(int monitorport) {
		this.monitorport = monitorport;
	}
}
