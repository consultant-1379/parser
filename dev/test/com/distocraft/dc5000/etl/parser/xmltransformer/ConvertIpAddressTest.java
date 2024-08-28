package com.distocraft.dc5000.etl.parser.xmltransformer;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.distocraft.dc5000.etl.engine.main.engineadmincommands.InvalidArgumentsException;

public class ConvertIpAddressTest {

    private static final String SAMPLE_IPV4_SOURCE_VALUE = "10.10.10.17";

    private static final String SAMPLE_MALFORMED_IPV4_SOURCE_VALUE = "10.10.10";

    private static final String SAMPLE_IPV6_SOURCE_VALUE = "2001:0db8:85a3:0:0000:8a2e:370:7334";

    // Different sample IPv6 format is used to ensure :: is replaced with 0000
    private static final String DIFFERENT_SAMPLE_IPV6_SOURCE_VALUE = "2001:0db8:85a3::8a2e:0370:7334";

    // Different sample IPv6 format with :: at end
    private static final String SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_END_FOR_TWO_PARTS = "2001:0db8:85a3:8a2e:0370:7334::";

    private static final String SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_END_FOR_SEVERAL_PARTS = "2001::";

    private static final String SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_START_FOR_TWO_PARTS = "::2001:0db8:85a3:8a2e:0370:7334";

    private static final String SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_START_FOR_SEVERAL_PARTS = "::2001";

    //2001::7334 equivalent to 2001:0:0:0:0:0:0:7334
    private static final String DIFFERENT_SAMPLE_IPV6_SOURCE_VALUE_WITH_SEVERAL_MISSING_PARTS = "2001::7334";

    private static final String SAMPLE_MALFORMED_IPV6_SOURCE_VALUE = "2001:0:0000:8a2e:0370:7334";

    private static final String TEST_TARGET = "testTarget";

    private static final String TEST_SOURCE = "testSource";

    private static final String TEST_NAME = "testName";

    private static Field source;

    private static Field target;

    private static Field convertIpAddressName;

    @BeforeClass
    public static void init() {
        try {
            /* initializing reflected fields */
            source = ConvertIpAddress.class.getDeclaredField("source");
            target = ConvertIpAddress.class.getDeclaredField("target");
            convertIpAddressName = ConvertIpAddress.class.getDeclaredField("convertIpAddressName");

            source.setAccessible(true);
            target.setAccessible(true);
            convertIpAddressName.setAccessible(true);

        } catch (Exception e) {
            fail("init() failed");
        }
    }

    @Test
    public void checkThatDataIsEmptyWhenSourceIsNull() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();

        try {
            target.set(convertIpAddr, TEST_TARGET);
            source.set(convertIpAddr, null);
            convertIpAddressName.set(convertIpAddr, TEST_NAME);
            /* Calling the tested method */
            convertIpAddr.transform(data, null);

            assertTrue(data.size() == 0);

        } catch (Exception e) {
            fail("checkThatDataIsEmptyWhenSourceIsNull() failed");
        }
    }

    @Test
    public void checkThatCorrectIPv4IsReturned() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_IPV4_SOURCE_VALUE);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x0a0a0a11", data.get(TEST_TARGET));
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv4IsReturned() failed");
        }
    }

    @Test
    public void checkThatExceptionIsThrownWhenIPv4Malformed() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_MALFORMED_IPV4_SOURCE_VALUE);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            fail("checkThatExceptionIsThrownWhenIPv4Malformed() failed");
        } catch (Exception e) {
            assertTrue(e instanceof InvalidArgumentsException);
            assertEquals(SAMPLE_MALFORMED_IPV4_SOURCE_VALUE + " is not a valid IPv4 or IPv6 IP address", e.getMessage());
        }
    }

    @Test
    public void checkThatCorrectIPv6IsReturned() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_IPV6_SOURCE_VALUE);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x20010db885a3000000008a2e03707334", data.get(TEST_TARGET));
            assertEquals(34, data.get(TEST_TARGET).length());
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv6IsReturned() failed");
        }
    }

    @Test
    public void checkThatCorrectIPv6IsReturnedWhenIPv6IsInDifferentCorrectFormat() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, DIFFERENT_SAMPLE_IPV6_SOURCE_VALUE);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x20010db885a3000000008a2e03707334", data.get(TEST_TARGET));
            assertEquals(34, data.get(TEST_TARGET).length());
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv6IsReturned() failed");
        }
    }

    @Test
    public void checkThatCorrectIPv6IsReturnedWhenIPv6HasColonsAtEnd() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_END_FOR_TWO_PARTS);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x20010db885a38a2e0370733400000000", data.get(TEST_TARGET));
            assertEquals(34, data.get(TEST_TARGET).length());
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv6IsReturned() failed");
        }
    }

    @Test
    public void checkThatCorrectIPv6IsReturnedWhenIPv6HasColonsAtEndForSeveralParts() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_END_FOR_SEVERAL_PARTS);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x20010000000000000000000000000000", data.get(TEST_TARGET));
            assertEquals(34, data.get(TEST_TARGET).length());
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv6IsReturned() failed");
        }
    }

    @Test
    public void checkThatCorrectIPv6IsReturnedWhenIPv6HasColonsAtStart() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_START_FOR_TWO_PARTS);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x0000000020010db885a38a2e03707334", data.get(TEST_TARGET));
            assertEquals(34, data.get(TEST_TARGET).length());
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv6IsReturned() failed");
        }
    }

    @Test
    public void checkThatCorrectIPv6IsReturnedWhenIPv6HasColonsAtStartForSeveralParts() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_IPV6_SOURCE_VALUE_WITH_COLONS_AT_START_FOR_SEVERAL_PARTS);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x00000000000000000000000000002001", data.get(TEST_TARGET));
            assertEquals(34, data.get(TEST_TARGET).length());
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv6IsReturned() failed");
        }
    }

    @Test
    public void checkThatCorrectIPv6IsReturnedWhenIPv6MissingSeveralPartBetweenColons() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, DIFFERENT_SAMPLE_IPV6_SOURCE_VALUE_WITH_SEVERAL_MISSING_PARTS);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            assertEquals("0x20010000000000000000000000007334", data.get(TEST_TARGET));
            assertEquals(34, data.get(TEST_TARGET).length());
            assertEquals(TEST_NAME, convertIpAddr.getName());
            assertEquals(TEST_TARGET, convertIpAddr.getTarget());
            assertEquals(TEST_SOURCE, convertIpAddr.getSource());
        } catch (Exception e) {
            fail("checkThatCorrectIPv6IsReturned() failed");
        }
    }

    @Test
    public void checkThatExceptionIsThrownWhenIPv6Malformed() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, SAMPLE_MALFORMED_IPV6_SOURCE_VALUE);
        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            fail("checkThatExceptionIsThrownWhenIPv6Malformed() failed");
        } catch (Exception e) {
            assertTrue(e instanceof InvalidArgumentsException);
            assertEquals(SAMPLE_MALFORMED_IPV6_SOURCE_VALUE + " is not a valid IPv4 or IPv6 IP address", e.getMessage());
        }
    }

    @Test
    public void checkThatExceptionIsThrownWhenSourceIsEmpty() {
        ConvertIpAddress convertIpAddr = new ConvertIpAddress();

        HashMap<String, String> data = new HashMap<String, String>();
        data.put(TEST_SOURCE, "");

        try {
            convertIpAddr.configure(TEST_NAME, TEST_SOURCE, TEST_TARGET, null, null);

            /* Calling the tested method */
            convertIpAddr.transform(data, null);
            fail("checkThatExceptionIsThrownWhenSourceIsEmpty() failed");
        } catch (Exception e) {
            assertTrue(e instanceof InvalidArgumentsException);
            assertEquals(" is not a valid IPv4 or IPv6 IP address", e.getMessage());
        }
    }
}
