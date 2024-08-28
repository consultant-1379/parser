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

public class FloatFormatterTest {

	@Test
	public void testFormat1() throws Exception{
		FloatFormatter ff = new FloatFormatter();
			/* Calling the tested method */
			byte[] i = ff.doFormat("8", 0);

		    String expected = null;
		    // Need this check for Jenkins
		    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
		        expected = "0,0,0,65,0";
		    } else {
		        expected = "65,0,0,0,0";
		    }
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);
	}

	@Test
	public void testFormat2() {
		FloatFormatter ff = new FloatFormatter();

		try {
			/* Calling the tested method */
			ff.doFormat("text", 0);
			fail("Should not execute this line");

		} catch (Exception e) {
			// test passed
		}
	}
}
