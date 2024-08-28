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

public class BigintFormatterTest {

  @Test
  public void testFormat1() {
    BigintFormatter bif = new BigintFormatter();
      /* Calling the tested method */
      byte[] i = bif.doFormat("10000000", 0);
      
      String expected = null;
      // Need this check for Jenkins
      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){    	  
    	  expected = "-128,-106,-104,0,0,0,0,0,0";
      }
      else{
    	  expected = "0,0,0,0,0,-104,-106,-128,0";  
      }
      
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];
      
      assertEquals(expected, actual);
  }
  
  @Test
  public void testFormat2() {
    BigintFormatter bif = new BigintFormatter();
      /* Calling the tested method */
      byte[] i = bif.doFormat("-10000000", 0);
      
      String expected = null;
      // Need this check for Jenkins
      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN){ 
    	  expected = "-128,105,103,-1,-1,-1,-1,-1,0";
      }
      else{
    	  expected = "-1,-1,-1,-1,-1,103,105,-128,0";
      }
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];
      
      assertEquals(expected, actual);

  }


  @Test
  public void testFormat3() {
    BigintFormatter bif = new BigintFormatter();
      /* Calling the tested method */
      byte[] i = bif.doFormat(null, 0);

      String expected = "0,0,0,0,0,0,0,0,1";
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];

      assertEquals(expected, actual);
  }

  @Test
  public void testFormat5() {
    BigintFormatter bif = new BigintFormatter();
      /* Calling the tested method */
      byte[] i = bif.doFormat("", 0);

      String expected = "0,0,0,0,0,0,0,0,1";
      String actual = i[0] + "," + i[1] + "," + i[2] + "," + i[3] + "," + i[4] + "," + i[5] + "," + i[6] + "," + i[7] + "," + i[8];

      assertEquals(expected, actual);
  }
  
  @Test
  public void testFormat4() {
    BigintFormatter bif = new BigintFormatter();
    
    try {
      /* Calling the tested method */
      bif.doFormat("text", 0);
      fail("Should not execute this line");
      
    } catch (Exception e) {
      // test passed
    }
  }
}
