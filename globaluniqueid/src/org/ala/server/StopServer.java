package org.ala.server;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class StopServer {
	
	public static StopCommandLineValues cmdLineValues;
	
	public static void main(String[] args) throws Exception {
		parseCmdLine(args);
        Socket s = new Socket(InetAddress.getByName("127.0.0.1"), cmdLineValues.getMonitorport());
        OutputStream out = s.getOutputStream();
        System.out.println("*** sending stop request");
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
			System.err.println(e.getMessage());
			parser.printUsage(System.err);
			System.err.println();
			System.exit(1);
		}
		// use the getters
	}


}
