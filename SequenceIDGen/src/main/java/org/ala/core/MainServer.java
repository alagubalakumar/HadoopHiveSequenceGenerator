package org.ala.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.ala.servlets.IDRangeGeneratorServlet;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;



/**
 * @author Alagu Kumar
 *
 */
public class MainServer {

	private  static Server server ;
	private  static  ServletContextHandler handler;
	private  static Logger logger = Logger.getLogger(MainServer.class);
	private StartCommandLineValues cmdLineValues;
	private Thread monitor;

	
	/**
	 * Constructor of MainServer is used to initialize logger with dynamic log directory and also to parse command line arguments
	 * @param args
	 * @throws IOException
	 */
	public MainServer(String[] args) throws IOException{
		parseCmdLine(args);	
		ConfigUtil.getInstance().setRangeDif(getCmdLineValues().getRange());
	}

	
	/**
	 * Main method which will kick start the range generator service & monitor service 
	 * with the ports mentioned in arguments
	 * @param args
	 */
	public static void main(String[] args){
		try{
			MainServer mainServ = new MainServer(args);
			mainServ.startMonitorThread(mainServ.getCmdLineValues().getMonitorport());
			mainServ.startMainService(mainServ.getCmdLineValues().getPort());
		
		} catch (Exception e) {
			logger.error("Exception in Starting Server:"+e.getMessage());
		}
	}

	/**
	 * Method will start the monitor thread,which will listen to a socket on monitor port.
	 * @param port
	 */
	public  void startMonitorThread(int port){
		Thread monitor = new MonitorThread(port);
		monitor.start();
	}
	

	/**
	 * Method will start jetty server and add a range generator service. 
	 * @param port
	 * @throws Exception
	 */
	public  void startMainService(int port) throws Exception{
		logger.info("*** starting server in the port..{"+port+"}");
		server = new Server(port);	
		handler = new ServletContextHandler(server, "/range");
		handler.addServlet(IDRangeGeneratorServlet.class, "/");
		server.start();
		
	}
	
	/**
	 * Method will stop jetty server. 
	 * @throws Exception
	 */
	public  void stopMainService() throws Exception{
		server.stop();
	}



	public StartCommandLineValues getCmdLineValues() {
		return cmdLineValues;
	}

	

	public static Server getServer() {
		return server;
	}


	public static void setServer(Server server) {
		MainServer.server = server;
	}


	/**
	 * Method which will parse the args and create a command line values .
	 * If the mandatory args are found,this method will exit from JVM
	 * @param args
	 */
	public  void parseCmdLine(String[] args) {
		cmdLineValues = new StartCommandLineValues();
		CmdLineParser parser = new CmdLineParser(cmdLineValues);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			logger.error(e.getMessage());
			logger.error("java DotsMain [options...] arguments...");
			parser.printUsage(System.err);
			System.err.println();
			System.exit(1);
		}
	}
	
	
	public Thread getMonitor() {
		return monitor;
	}


	public void setMonitor(Thread monitor) {
		this.monitor = monitor;
	}


	/**
	 * Monitor Thread 
	 *  listen to a socket for a flag message and will kill the service by exiting from JVM
	 *
	 */
	private static class MonitorThread extends Thread {

		private ServerSocket socket;
		private int monitorport;

		public MonitorThread(int monitorport) {
			
			setDaemon(true);
			setName(ServerProperties.Name);
			this.monitorport = monitorport;
			try {
				socket = new ServerSocket(monitorport, 1, InetAddress.getByName(ServerProperties.IP));
			} catch(Exception e) {
				logger.error("exception in socket",e);
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			logger.info("*** running monitor thread..in the port{"+monitorport+"}");
			Socket accept;
			try {
				accept = socket.accept();
				BufferedReader reader = new BufferedReader(new InputStreamReader(accept.getInputStream()));
				String cmd = reader.readLine();
				if(cmd.equalsIgnoreCase("stop serviceidgenerator")){
					logger.info("*** stopping server");
					getServer().stop();
					accept.close();
					socket.close();
					System.exit(1);
				}
			} catch(Exception e) {
				logger.error("exception in monitor thread",e);
				throw new RuntimeException(e);
			}
		}
	}
}

