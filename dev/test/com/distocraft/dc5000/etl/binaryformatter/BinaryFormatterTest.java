package com.distocraft.dc5000.etl.binaryformatter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author ejarsok
 *
 */

public class BinaryFormatterTest {

	@Test
	public void testFormat1() throws Exception{
		BinaryFormatter bf = new BinaryFormatter();
			/* Calling the tested method */
			byte[] i = bf.doFormat("", 2);

			String expected = "0,0,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat2() throws Exception {
		BinaryFormatter bf = new BinaryFormatter();
			/* Calling the tested method */
			byte[] i = bf.doFormat(null, 2);

			String expected = "0,0,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat3() throws Exception{
		BinaryFormatter bf = new BinaryFormatter();
			/* Calling the tested method */
			byte[] i = bf.doFormat("text", 4);

			String expected = "116,101,120,116,0";
			String actual = i[0] + "," + i[1] + "," + i[2]+ "," + i[3]+ "," + i[4];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat4() throws Exception {
		BinaryFormatter bf = new BinaryFormatter();
			/* Calling the tested method */
			byte[] i = bf.doFormat("t", 2);

			String expected = "116,0,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}
}
