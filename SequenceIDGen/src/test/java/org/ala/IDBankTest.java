package org.ala;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.ala.core.ConfigUtil;
import org.junit.BeforeClass;
import org.junit.Test;

public class IDBankTest {
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ConfigUtil.getInstance().setRangeDif(2000);
	}


	@Test
	public void testGetNextSeqRange() {
		Map<String,Integer> rangeMap = IDBank.getNextSeqRange();
	    assertNotNull(rangeMap);
	    assertTrue(rangeMap.get("start") == 1);
	    assertTrue(rangeMap.get("end") == 2000);
	}

}
