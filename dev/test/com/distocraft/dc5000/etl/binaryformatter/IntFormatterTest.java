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

public class IntFormatterTest {

	@Test
	public void testFormat1() {
		IntFormatter ifo = new IntFormatter();

		try {
			/* Calling the tested method */
			byte[] i = ifo.doFormat("", 0);

			String expected = "0,0,0,0,1";
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat1() failed");
		}
	}

	@Test
	public void testFormat2() {
		IntFormatter ifo = new IntFormatter();

		try {
			/* Calling the tested method */
			byte[] i = ifo.doFormat(null, 0);

			String expected = "0,0,0,0,1";
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat2() failed");
		}
	}

	@Test
	public void testFormat3() {
		IntFormatter ifo = new IntFormatter();

		try {
			/* Calling the tested method */
			byte[] i = ifo.doFormat("3", 0);

			String expected = null;
   	        // Need this check for Jenkins
		    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
		    	expected = "3,0,0,0,0";
		    } else {
		    	expected = "0,0,0,3,0";
		    }
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat2() failed");
		}
	}

	@Test
	public void testFormat4() {
		IntFormatter ifo = new IntFormatter();

		try {
			/* Calling the tested method */
			byte[] i = ifo.doFormat("-3", 0);

			String expected = null;
   	        // Need this check for Jenkins
		    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
		    	expected = "-3,-1,-1,-1,0";
		    } else {
		    	expected = "-1,-1,-1,-3,0";
		    }
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat4() failed");
		}
	}

	@Test
	public void testFormat5() {
		IntFormatter ifo = new IntFormatter();

		try {
			/* Calling the tested method */
			ifo.doFormat("text", 0);
			fail("Should not execute this line");

		} catch (Exception e) {

		}
	}
}
