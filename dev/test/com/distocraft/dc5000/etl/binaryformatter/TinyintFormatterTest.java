package com.distocraft.dc5000.etl.binaryformatter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author ejarsok
 *
 */

public class TinyintFormatterTest {

	@Test
	public void testFormat1() {
		TinyintFormatter tif = new TinyintFormatter();
		try {
			/* Calling the tested method */
			byte[] i = tif.doFormat("", 0);

			String expected = "0,1";
			String actual = i[0] + "," + i[1];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat1() failed");
		}
	}

	@Test
	public void testFormat2() {
		TinyintFormatter tif = new TinyintFormatter();
		try {
			/* Calling the tested method */
			byte[] i = tif.doFormat(null, 0);

			String expected = "0,1";
			String actual = i[0] + "," + i[1];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat2() failed");
		}
	}

	@Test
	public void testFormat3() {
		TinyintFormatter tif = new TinyintFormatter();
		try {
			/* Calling the tested method */
			byte[] i = tif.doFormat("10", 0);

			String expected = "10,0";
			String actual = i[0] + "," + i[1];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat3() failed");
		}
	}

	@Test
	public void testFormat4() {
		TinyintFormatter tif = new TinyintFormatter();
		try {
			/* Calling the tested method */
			tif.doFormat("text", 0);
			fail("Should not execute this line");

		} catch (Exception e) {
			// test passed
		}
	}
}
