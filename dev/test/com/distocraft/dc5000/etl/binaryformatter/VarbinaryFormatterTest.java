package com.distocraft.dc5000.etl.binaryformatter;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 
 * @author ejarsok
 *
 */

public class VarbinaryFormatterTest {

	@Test
	public void testFormat1() throws Exception{
		VarbinaryFormatter vf = new VarbinaryFormatter();
			/* Calling the tested method */
			byte[] i = vf.doFormat("", 2);

			String expected = "0,0,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat2() throws Exception{
		VarbinaryFormatter vf = new VarbinaryFormatter();
			/* Calling the tested method */
			byte[] i = vf.doFormat(null, 2);

			String expected = "0,0,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat3() throws Exception{
		VarbinaryFormatter vf = new VarbinaryFormatter();
			/* Calling the tested method */
			byte[] i = vf.doFormat("text", 4);

			String expected = "116,101,120,116,0";
			String actual = i[0] + "," + i[1] + "," + i[2]+ "," + i[3]+ "," + i[4];

			assertEquals(expected, actual);

	}

	@Test
	public void testFormat4() {
		VarbinaryFormatter vf = new VarbinaryFormatter();

		try {
			/* Calling the tested method */
			byte[] i = vf.doFormat("t", 2);

			String expected = "116,0,0";
			String actual = i[0] + "," + i[1] + "," + i[2];

			assertEquals(expected, actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail("testFormat3() failed");
		} 
	}
}
