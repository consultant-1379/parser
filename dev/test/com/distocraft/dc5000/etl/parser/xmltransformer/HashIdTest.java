package com.distocraft.dc5000.etl.parser.xmltransformer;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class HashIdTest {

  private static final String SAMPLE_SOURCE_VALUE = "1ONRM_RootMo_R:RNC03:RNC031135Ericsson";

  private static final String TEST_TARGET = "testTarget";

  private static final String TEST_SOURCE = "testSource";

  private static final String TEST_NAME = "testName";

  private static Field source;

  private static Field target;

  private static Field hashIdName;

  @BeforeClass
  public static void init() {
    try {
      /* initializing reflected fields */
      source = HashId.class.getDeclaredField("source");
      target = HashId.class.getDeclaredField("target");
      hashIdName = HashId.class.getDeclaredField("hashIdName");

      source.setAccessible(true);
      target.setAccessible(true);
      hashIdName.setAccessible(true);

    } catch (Exception e) {
      fail("init() failed");
    }
  }

  @Test
  public void checkThatDataIsEmptyWhenSourceIsNull() {
    HashId hashId = new HashId();

    HashMap<String, String> data = new HashMap<String, String>();

    try {
      target.set(hashId, TEST_TARGET);
      source.set(hashId, null);
      hashIdName.set(hashId, TEST_NAME);
      /* Calling the tested method */
      hashId.transform(data, null);

      assertTrue(data.size() == 0);

    } catch (Exception e) {
      fail("checkThatDataIsEmptyWhenSourceIsNull() failed");
    }
  }

  @Test
  public void checkThatDataIsEmptyWhenSourceIsEmpty() {
    HashId hashId = new HashId();

    HashMap<String, String> data = new HashMap<String, String>();
    data.put(TEST_SOURCE, "");

    try {
      hashId.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

      /* Calling the tested method */
      hashId.transform(data, null);
      assertEquals("6061155539545534980", data.get(TEST_TARGET));
      assertEquals(TEST_NAME, hashId.getName());
      assertEquals(TEST_TARGET, hashId.getTarget());
      assertEquals(TEST_SOURCE, hashId.getSource());
    } catch (Exception e) {
      fail("checkThatCorrectHashIdIsReturned() failed");
    }
  }

  @Test
  public void checkThatCorrectHashIdIsReturned() {
    HashId hashId = new HashId();

    HashMap<String, String> data = new HashMap<String, String>();
    data.put(TEST_SOURCE, SAMPLE_SOURCE_VALUE);

    try {
      hashId.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

      /* Calling the tested method */
      hashId.transform(data, null);
      assertEquals("3650468854192837135", data.get(TEST_TARGET));
      assertEquals(TEST_NAME, hashId.getName());
      assertEquals(TEST_TARGET, hashId.getTarget());
      assertEquals(TEST_SOURCE, hashId.getSource());
    } catch (Exception e) {
      fail("checkThatCorrectHashIdIsReturned() failed");
    }
  }
}
