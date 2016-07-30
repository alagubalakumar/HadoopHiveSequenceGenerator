package org.ala.servlets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class IDRangeGeneratorServletTest {


	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void test() throws Exception {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		when(response.getWriter()).thenReturn(pw);
	
		IDRangeGeneratorServlet servlet = new IDRangeGeneratorServlet();
		servlet.doGet(request, response);
		String result = sw.getBuffer().toString().trim();

		JSONObject rangeMap = (JSONObject) JSONSerializer.toJSON(result);
		assertNotNull(rangeMap);
		assertTrue(Integer.valueOf(rangeMap.get("start").toString()) == 1);
		assertTrue(Integer.valueOf(rangeMap.get("end").toString()) == 1000);
		
	}
}