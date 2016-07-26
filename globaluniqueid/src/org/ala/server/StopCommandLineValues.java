package org.ala.server;

import org.kohsuke.args4j.Option;

public class StopCommandLineValues {
	
	@Option(name = "-mp", aliases = { "--monitorport" }, required = true,
            usage = "monitor port which is used to stop the  server")
    private int monitorport;
	

	public int getMonitorport() {
		return monitorport;
	}

	public void setMonitorport(int monitorport) {
		this.monitorport = monitorport;
	}
}
