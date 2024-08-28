package com.distocraft.dc5000.etl.binaryformatter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author ejarsok
 *
 */

public class CharFormatterTest {

	@Test
	public void testFormat1() throws Exception {
		CharFormatter cf = new CharFormatter();

			/* Calling the tested method */
			byte[] i = cf.doFormat("", 2);

			String expected = "32,32,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat2() throws Exception {
		CharFormatter cf = new CharFormatter();
			/* Calling the tested method */
			byte[] i = cf.doFormat(null, 2);

			String expected = "32,32,1";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat3() throws Exception{
		CharFormatter cf = new CharFormatter();

			/* Calling the tested method */
			byte[] i = cf.doFormat("text", 4);

			String expected = "116,101,120,116,0";
			String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat4() throws Exception {
		CharFormatter cf = new CharFormatter();
			/* Calling the tested method */
			byte[] i = cf.doFormat("t", 2);

			String expected = "116,32,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}
}
