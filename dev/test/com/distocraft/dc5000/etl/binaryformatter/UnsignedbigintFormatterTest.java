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

public class UnsignedbigintFormatterTest {

	@Test
	public void testFormat1() {
		UnsignedbigintFormatter ubif = new UnsignedbigintFormatter();

		try {
			/* Calling the tested method */
			byte[] i = ubif.doFormat("100", 0);

			String expected = null;
		    // Need this check for Jenkins
		    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
		        expected = "100,0,0,0,0,0,0,0,0";
		    } else {
		        expected = "0,0,0,0,0,0,0,100,0";
		    }
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat1() failed");
		}
	}

	@Test
	public void testFormat2() {
		UnsignedbigintFormatter ubif = new UnsignedbigintFormatter();

		try {
			/* Calling the tested method */
			ubif.doFormat("text", 0);
			fail("Should not execute this line");

		} catch (Exception e) {
			// test passed
		}
	}
}
