/**
 * 
 */
package org.ala.core;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Alagu Kumar
 *
 */
public class MainServerTest {

	
	/**
	 * Test method for {@link org.ala.core.MainServer#MainServer(java.lang.String[])}.
	 * @throws IOException 
	 */
	@Test
	public void testMainServer() throws IOException {
		String[] cmdLineArgs = new String[] {"--range","1000", "-sp", "7070", "-mp", "8089"};
		MainServer mainServ = new MainServer(cmdLineArgs);
		assertTrue(mainServ.getCmdLineValues().getMonitorport() == 8089);
		assertTrue(mainServ.getCmdLineValues().getPort() == 7070);
		assertTrue(mainServ.getCmdLineValues().getRange() == 1000);
	}

	/**
	 * Test method for {@link org.ala.core.MainServer#main(java.lang.String[])}.
	 * @throws Exception 
	 */
	@Test
	public void testMain() throws Exception {
		String[] cmdLineArgs = new String[] {"--range","1000", "-sp", "7070", "-mp", "8089"};
		MainServer mainServ = new MainServer(cmdLineArgs); 
		mainServ.startMainService(mainServ.getCmdLineValues().getPort());
		assertNotNull(MainServer.getServer());
		assertTrue(MainServer.getServer().isRunning());	
		MainServer.getServer().stop();
	
	}
	
	/**
	 * Test method for {@link org.ala.core.MainServer#startMonitorThread}.
	 * @throws Exception 
	 */
	@Test
	public void testStartMonitorThread() throws Exception {
		String[] cmdLineArgs = new String[] {"--range","1000", "-sp", "7070", "-mp", "8089"};
		String[] stopCmdLineArgs = new String[] { "-mp", "8089"};
		MainServer mainServ = new MainServer(cmdLineArgs); 
		mainServ.startMainService(mainServ.getCmdLineValues().getPort());
		mainServ.startMonitorThread(mainServ.getCmdLineValues().getMonitorport());
		assertNotNull(MainServer.getServer());
		assertTrue(MainServer.getServer().isRunning());	
		StopServer.main(stopCmdLineArgs);
		MainServer.getServer().stop();
	
	}


}
