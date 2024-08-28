package com.distocraft.dc5000.etl.binaryformatter;

import static org.junit.Assert.*;

import java.nio.ByteOrder;

import org.junit.Test;

/**
 * 
 * @author ejarsok
 *
 */

public class DatetimeFormatterTest {

	@Test
	public void testFormat1() {
		DatetimeFormatter dtf = new DatetimeFormatter();

		try {
			/* Calling the tested method */
			byte[] i = dtf.doFormat("", 0);

			String expected = "0,0,0,0,0,0,0,0,1";
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat1() failed");
		}
	}

	@Test
	public void testFormat2() {
		DatetimeFormatter dtf = new DatetimeFormatter();

		try {
			/* Calling the tested method */
			byte[] i = dtf.doFormat(null, 0);

			String expected = "0,0,0,0,0,0,0,0,1";
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat2() failed");
		}
	}

	@Test
	public void testFormat3() {
		DatetimeFormatter dtf = new DatetimeFormatter();

		try {
			/* Calling the tested method */
			byte[] i = dtf.doFormat("2008-10-09 10:20:30:40", 0);
		    String expected = null;
		      // Need this check for Jenkins
		      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){    	  
		    	  expected = "-128,23,-15,10,-111,53,-31,0,0";
		      }
		      else{
		    	  expected = "0,-31,53,-111,10,-15,23,-128,0";  
		      }			
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat3() failed");
		}
	}

	@Test
	public void testFormat4() {
		DatetimeFormatter dtf = new DatetimeFormatter();

		try {
			/* Calling the tested method */
			dtf.doFormat("text", 0);
			fail("Should not execute this line");

		} catch (Exception e) {
			// test passed
		}
	}
}
