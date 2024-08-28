package com.distocraft.dc5000.etl.binaryformatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.ByteOrder;

import org.junit.Test;

/**
 * 
 * @author ejarsok
 *
 */

public class DateFormatterTest {

	@Test
	public void testFormat1() throws Exception {
		DateFormatter df = new DateFormatter();

			/* Calling the tested method */
			byte[] i= df.doFormat("", 0);

			String expected = "0,0,0,0,1";
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat2() throws Exception {
		DateFormatter df = new DateFormatter();
			/* Calling the tested method */
			byte[] i= df.doFormat(null, 0);

			String expected = "0,0,0,0,1";
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat3() throws Exception{
		DateFormatter df = new DateFormatter();
			/* Calling the tested method */
			byte[] i= df.doFormat("2008-10-09", 0);			
		    String expected = null;
		      // Need this check for Jenkins
		      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){    	  
		    	  expected = "-7,49,11,0,0";
		      }
		      else{
		    	  expected = "0,11,49,-7,0";  
		      }
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);
	}

	@Test
	public void testFormat4() {
		DateFormatter df = new DateFormatter();

		try {
			/* Calling the tested method */
			df.doFormat("text", 0);
			fail("Should not execute this line");

		} catch (Exception e) {
			// test passed
		} 
	}
}
