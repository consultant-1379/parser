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

public class TimeFormatterTest {

	@Test
	public void testFormat1() {
		TimeFormatter tf = new TimeFormatter();

		try {
			/* Calling the tested method */
			byte[] i = tf.doFormat("", 0);

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
		TimeFormatter tf = new TimeFormatter();

		try {
			/* Calling the tested method */
			byte[] i = tf.doFormat(null, 0);

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
		TimeFormatter tf = new TimeFormatter();

		try {
			/* Calling the tested method */
			byte[] i = tf.doFormat("10:20:30", 0);

		    String expected = null;
		    // Need this check for Jenkins
		    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
		        expected = "-128,-73,20,-85,8,0,0,0,0";
		    } else {
		        expected = "0,0,0,8,-85,20,-73,-128,0";
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
		TimeFormatter tf = new TimeFormatter();

		try {
			/* Calling the tested method */
			tf.doFormat("text", 0);
			fail("Should not execute this line");

		} catch (Exception e) {
			// test passed
		}
	}
}
