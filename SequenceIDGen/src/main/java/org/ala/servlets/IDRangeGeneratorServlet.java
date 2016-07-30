package org.ala.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import net.sf.json.JSONSerializer;

import org.ala.IDBank;
import org.apache.log4j.Logger;
import org.eclipse.jetty.http.HttpStatus;



/**
 * @author Alagu Kumar
 * 
 * Service to get the range values in the json format ({start=1, end=2000})
 *
 */
public class IDRangeGeneratorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger  logger = Logger.getLogger(IDRangeGeneratorServlet.class);
	
	public void init() throws ServletException {
		logger.info("IDRangeGeneratorServlet initialized..");
    }


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
        
		resp.setContentType(MediaType.APPLICATION_JSON);
		resp.setStatus(HttpStatus.OK_200);
		resp.getWriter().println(JSONSerializer.toJSON(IDBank.getNextSeqRange()));
	}
}

