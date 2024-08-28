package com.distocraft.dc5000.etl.binaryformatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.nio.ByteOrder;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author ejarsok
 *
 */

public class NumDecFormatterTest {

  private static Method specialFormat;
  
  @BeforeClass
  public static void init() {
    try {
      /* initializing reflected method */
      specialFormat = NumDecFormatter.class.getDeclaredMethod("specialFormat", new Class[] {String.class, int.class, int.class});
      
      specialFormat.setAccessible(true);
      
    } catch (Exception e) {
      e.printStackTrace();
      fail("init() failed");
    }
  }
  
  @Test
  public void testFormat1() {
    NumDecFormatter ndf = new NumDecFormatter();
    
    try {
      /* Calling the tested method */
      ndf.doFormat("", 0);
      fail("Should not execute this line");
      
    } catch (Exception e) {
      // test passed
    }
  }
  
  @Test
  public void testFormat2() {
    NumDecFormatter ndf = new NumDecFormatter();
    
    try {
      /* Calling the tested method */
      byte[] i = ndf.doFormat("", 2, 0);

      String expected = "0,0,1";
      String actual = i[0] + "," + i[1] + "," + i[2];
      
      assertEquals(expected, actual);
      
    } catch (Exception e) {
      e.printStackTrace();
      fail("testFormat2() failed");
    }
  }
  
  @Test
  public void testFormat3() {
    NumDecFormatter ndf = new NumDecFormatter();
    
    try {
      /* Calling the tested method */
      byte[] i = ndf.doFormat(null, 7, 0);

      String expected = "0,0,0,0,1";
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4];
      
      assertEquals(expected, actual);
      
    } catch (Exception e) {
      e.printStackTrace();
      fail("testFormat3() failed");
    }
  }
  
  @Test
  public void testFormat4() {
    NumDecFormatter ndf = new NumDecFormatter();
    
    try {
      /* Calling the tested method */
      byte[] i = ndf.doFormat("1,2,3", 15, 5);

      String expected = null;
	        // Need this check for Jenkins
	  if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
	  	  expected = "-64,-44,1,0,0,0,0,0,0";
	  } else {
	  	  expected = "0,0,0,0,0,1,-44,-64,0";
	  }
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];
      
      assertEquals(expected, actual);
      
    } catch (Exception e) {
      e.printStackTrace();
      fail("testFormat4() failed");
    }
  }
  
  @Test
  public void testFormat5() {
    NumDecFormatter ndf = new NumDecFormatter();
    
    try {
      /* Calling the tested method */
      byte[] i = ndf.doFormat("-1,2,3", 15, 5);

      String expected = null;
      // Need this check for Jenkins
      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
    	  expected = "64,43,-2,-1,-1,-1,-1,-1,0";
      } else {
    	  expected = "-1,-1,-1,-1,-1,-2,43,64,0";
      }
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];
      
      assertEquals(expected, actual);
      
    } catch (Exception e) {
      e.printStackTrace();
      fail("testFormat5() failed");
    }
  }
  
  @Test
  public void testFormat6() {
    NumDecFormatter ndf = new NumDecFormatter();
      /* Calling the tested method */
    byte[] i = new byte[0];
    try {
      i = ndf.doFormat("1,2,3", 25, 5);
    } catch (Exception e) {
      fail(e.toString());
    }

    String expected = null;
    // Need this check for Jenkins
    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
  	    expected = "1,2,80,0,0,0,12,0,0,0,0,0,0,0,0,0,0";
    } else {
    	expected = "0,0,0,0,0,0,0,0,0,12,0,0,0,80,2,1,0";
    }
    String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8] + "," + i[9]
    		+ "," + i[10] + "," + i[11] + "," + i[12] + "," + i[13] + "," + i[14] + "," + i[15] + "," + i[16];
      
    assertEquals(expected, actual);
  }

  @Test
  public void testFormat7() {
    NumDecFormatter ndf = new NumDecFormatter();
      /* Calling the tested method */
    byte[] i = new byte[0];
    try {
      i = ndf.doFormat("88", 4, 2);
    } catch (Exception e) {
      fail(e.toString());
    }

    String expected = null;
    // Need this check for Jenkins
    if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
  	    expected = "96,34,0";
    } else {
  	    expected = "34,96,0";
    }
    String actual = i[0] + "," + i[1] + "," + i[2];

      assertEquals(expected, actual);

  }

  @Test
  public void testSpecialFormatStringIntInt() {
    NumDecFormatter ndf = new NumDecFormatter();
    
    try {
      /* Calling the tested method */
      //final byte[] i = (byte[]) specialFormat.invoke(ndf, new Object[] {"5000", 20, 0});
      final byte[] i = (byte[]) specialFormat.invoke(ndf, new Object[] {"0000000000000000000120000", 25, 5});
      
      String expected = null;
      // Need this check for Jenkins
      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
          expected = "1,2,79,7,-48,0,1,0,0,0,0,0,0,0,0,0,0";
      } else {
          expected = "0,0,0,0,0,0,0,0,0,1,0,-48,7,79,2,1,0";
      }
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8] + "," + i[9]
      		+ "," + i[10] + "," + i[11] + "," + i[12] + "," + i[13] + "," + i[14] + "," + i[15] + "," + i[16];
      
      assertEquals(expected, actual);
      
    } catch (Exception e) {
      e.printStackTrace();
      fail("testSpecialFormatStringIntInt() failed");
    }
  }
}
