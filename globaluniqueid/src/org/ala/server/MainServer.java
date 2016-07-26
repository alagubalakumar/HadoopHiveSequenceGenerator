package org.ala.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.ala.servlet.IDRangeGeneratorServlet;

public class MainServer {

	private  int port;
	private  static int monitorport;
	private  static Server server ;
	private  static  ServletContextHandler handler;
	private  static Logger logger;
	public StartCommandLineValues cmdLineValues;

	public MainServer(String[] args) throws IOException{
		parseCmdLine(args);
		//port = Integer.valueOf(args[0]);
		port = cmdLineValues.getPort();
		//monitorport = Integer.valueOf(args[1]);
		monitorport = cmdLineValues.getMonitorport();
		File logDir = new File( System.getProperty("user.dir")+"/"+port+"-"+monitorport);
		if(!logDir.exists()){
			logDir.mkdir();
		}
		System.setProperty("globalseqlog.dir", logDir.getAbsolutePath());
		logger = Logger.getLogger(MainServer.class);
		//ConfigUtil.getInstance().setRangeDif(Integer.valueOf(args[2]));
		ConfigUtil.getInstance().setRangeDif(cmdLineValues.getRange());
	}

	public static void main(String[] args){
		try{

			MainServer mainServ = new MainServer(args);
			mainServ.startMonitorThread(mainServ.getMonitorPort());
			mainServ.startMainService(mainServ.getPort());
		}catch(Exception e){
			logger.error("Exception in starting the service",e);
		}
	}

	private  void startMonitorThread(int port){
		Thread monitor = new MonitorThread(port);
		monitor.start();
	}

	private  void startMainService(int port) throws Exception{
		logger.info("*** starting server in the port..{"+port+"}");
		server = new Server(port);	
		handler = new ServletContextHandler(server, "/range");
		handler.addServlet(IDRangeGeneratorServlet.class, "/");
		server.start();
		server.join();
	}


	public int getPort(){
		return port;
	}

	public int getMonitorPort(){
		return monitorport;
	}


	public  void parseCmdLine(String[] args) {
		cmdLineValues = new StartCommandLineValues();
		CmdLineParser parser = new CmdLineParser(cmdLineValues);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException e) {
			System.err.println(e.getMessage());
			System.err.println("java DotsMain [options...] arguments...");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();
			System.exit(1);
		}
		// use the getters
	}

	private static class MonitorThread extends Thread {

		private ServerSocket socket;

		public MonitorThread(int monitorport) {
			setDaemon(true);
			setName("StopMonitor");
			try {
				socket = new ServerSocket(monitorport, 1, InetAddress.getByName("127.0.0.1"));
			} catch(Exception e) {
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
					server.stop();
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
