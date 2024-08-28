package com.distocraft.dc5000.etl.binaryformatter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author ejarsok
 */

public class BitFormatterTest {

  @Test
  public void testFormat1() throws Exception {
    BitFormatter bif = new BitFormatter();
    /* Calling the tested method */
    byte[] i = bif.doFormat("", 0);

    String expected = "0,0";
    String actual = i[0] + "," + i[1];

    assertEquals(expected, actual);

  }

  @Test
  public void testFormat2() throws Exception {
    BitFormatter bif = new BitFormatter();
    /* Calling the tested method */
    byte[] i = bif.doFormat(null, 0);

    String expected = "0,0";
    String actual = i[0] + "," + i[1];

    assertEquals(expected, actual);

  }

  @Test
  public void testFormat3() throws Exception {
    BitFormatter bif = new BitFormatter();
    /* Calling the tested method */
    byte[] i = bif.doFormat("10", 0);

    String expected = "1,0";
    String actual = i[0] + "," + i[1];

    assertEquals(expected, actual);
  }

  @Test
  public void testFormat4() {
    BitFormatter bif = new BitFormatter();
    try {
      /* Calling the tested method */
      bif.doFormat("text", 0);
      fail("Should not execute this line");

    } catch (Exception e) {
      // test passed
    }
  }
}
