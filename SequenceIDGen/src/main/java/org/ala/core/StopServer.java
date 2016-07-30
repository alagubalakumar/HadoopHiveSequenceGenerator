package org.ala.core;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author Alagu Kumar
 * 
 * Thread to stop main service,this will open a socket and write a command ,the port is already listening by a monitor thread
 * so that it will listen to the message and kills and exists from the system.
 *
 */
public class StopServer {
	
	public static StopCommandLineValues cmdLineValues;
	private  static Logger logger = Logger.getLogger(StopServer.class);
	
	public static void main(String[] args) throws Exception {
		parseCmdLine(args);
        Socket s = new Socket(InetAddress.getByName(ServerProperties.IP), cmdLineValues.getMonitorport());
        OutputStream out = s.getOutputStream();
        logger.info("*** sending stop request");
        out.write(("stop serviceidgenerator").getBytes());
        out.flush();
        s.close();
    }
	
	

	public static void parseCmdLine(String[] args) {
		cmdLineValues = new StopCommandLineValues();
		CmdLineParser parser = new CmdLineParser(cmdLineValues);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			logger.error(e.getMessage());
			parser.printUsage(System.err);
			System.err.println();
			System.exit(1);
		}
	}
}
