package com.distocraft.dc5000.etl.parser.xmltransformer;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author eromsza
 */

public class ROPTimeTest {

    private static final int DEFAULT_ROP = 15;

    private static final int MILLISECONDS_IN_MINUTE = 60000;

    private static final String SOURCE_TIME = "2013-07-26 02:43:21.123";

    private static final String START_ROP_TIME = "2013-07-25 18:30:00";

    private static final String END_ROP_TIME = "2013-07-25 18:45:00";

    private static Field source;

    private static Field target;

    private static Field name;

    private static Field rop;

    @BeforeClass
    public static void init() {
        try {
            /* initializing reflected fields */
            source = ROPTime.class.getDeclaredField("src");
            target = ROPTime.class.getDeclaredField("target");
            name = ROPTime.class.getDeclaredField("name");
            rop = ROPTime.class.getDeclaredField("rop");

            source.setAccessible(true);
            target.setAccessible(true);
            name.setAccessible(true);
            rop.setAccessible(true);

        } catch (Exception e) {
            e.printStackTrace();
            fail("init() failed");
        }
    }

    @Test
    public void testTransformNoSourceDefaultValues() {
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        final Map<String, String> data = new HashMap<String, String>();

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            final String actual = data.get("target");
            final Calendar calendar = GregorianCalendar.getInstance(TimeZone.getDefault());

            assertEquals(String.format("%1$tY-%1$tm-%1$td %1$TT", new Timestamp(
                    (System.currentTimeMillis() / (DEFAULT_ROP * MILLISECONDS_IN_MINUTE))
                            * (DEFAULT_ROP * MILLISECONDS_IN_MINUTE) + calendar.get(Calendar.ZONE_OFFSET))), actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testTransformIncorrectSourceDefaultValues() {
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        final Map<String, String> data = new HashMap<String, String>();
        data.put("source", "ERRORENOUS_TIMESTAMP");

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            assertNull(data.get("target"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testTransformNoSourceAsStartTimestamp() {
        final int ropToTest = 15;
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        props.setProperty("timezoneFixed", "PST");
        props.setProperty("rop", Integer.toString(ropToTest));
        final Map<String, String> data = new HashMap<String, String>();

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            final String actual = data.get("target");
            final Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("PST"));

            assertEquals(String.format("%1$tY-%1$tm-%1$td %1$TT", new Timestamp(
                    (System.currentTimeMillis() / (ropToTest * MILLISECONDS_IN_MINUTE))
                            * (ropToTest * MILLISECONDS_IN_MINUTE) + calendar.get(Calendar.ZONE_OFFSET))), actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testTransformNoSourceEndTime() {
        final int ropToTest = 15;
        final int ropDelta = 15;
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        props.setProperty("timezoneFixed", "PST");
        props.setProperty("rop", Integer.toString(ropToTest));
        props.setProperty("delta", Integer.toString(ropDelta));
        final Map<String, String> data = new HashMap<String, String>();

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            final String actual = data.get("target");
            final Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("PST"));

            assertEquals(String.format(
                    "%1$tY-%1$tm-%1$td %1$TT",
                    new Timestamp((System.currentTimeMillis() / (ropToTest * MILLISECONDS_IN_MINUTE))
                            * (ropToTest * MILLISECONDS_IN_MINUTE) + ropDelta * MILLISECONDS_IN_MINUTE
                            + calendar.get(Calendar.ZONE_OFFSET))), actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testTransformFixedTZAsStartTime() throws ParseException {
        final int ropToTest = 15;
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        props.setProperty("timezoneFixed", "PST");
        props.setProperty("rop", Integer.toString(ropToTest));
        final Map<String, String> data = new HashMap<String, String>();
        data.put("source", Timestamp.valueOf(SOURCE_TIME).toString());

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            final String actual = data.get("target");
            assertEquals(START_ROP_TIME, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testTransformTZinFieldAsStartTime() {
        final int ropToTest = 15;
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        props.setProperty("timezoneField", "TIMEZONE");
        props.setProperty("rop", Integer.toString(ropToTest));
        final Map<String, String> data = new HashMap<String, String>();
        data.put("source", Timestamp.valueOf(SOURCE_TIME).toString());
        data.put("TIMEZONE", "PST");

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            final String actual = data.get("target");
            assertEquals(START_ROP_TIME, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testTransformFixedTZAsEndTime() {
        final int ropToTest = 15;
        final int ropDelta = 15;
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        props.setProperty("timezoneFixed", "PST");
        props.setProperty("rop", Integer.toString(ropToTest));
        props.setProperty("delta", Integer.toString(ropDelta));
        final Map<String, String> data = new HashMap<String, String>();
        data.put("source", Timestamp.valueOf(SOURCE_TIME).toString());

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            final String actual = data.get("target");
            assertEquals(END_ROP_TIME, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testTransformTZinFieldAsEndTime() {
        final int ropToTest = 15;
        final int ropDelta = 15;
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();
        props.setProperty("timezoneField", "TIMEZONE");
        props.setProperty("rop", Integer.toString(ropToTest));
        props.setProperty("delta", Integer.toString(ropDelta));
        final Map<String, String> data = new HashMap<String, String>();
        data.put("source", Timestamp.valueOf(SOURCE_TIME).toString());
        data.put("TIMEZONE", "PST");

        try {
            ropsm.configure("name", "source", "target", props, null);
            ropsm.transform(data, null);

            final String actual = data.get("target");
            assertEquals(END_ROP_TIME, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testTransform() failed");
        }
    }

    @Test
    public void testConfigure() {
        final ROPTime ropsm = new ROPTime();
        final Properties props = new Properties();

        try {
            ropsm.configure("name", "source", "target", props, null);
            source.set(ropsm, "source");

            final String expected = "name,source,target";
            final String actual = name.get(ropsm) + "," + source.get(ropsm) + "," + target.get(ropsm);

            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail("testConfigure() failed");
        }
    }

    @Test
    public void testGetSource() {
        final ROPTime ropsm = new ROPTime();

        try {
            ropsm.configure("name", "source", "target", new Properties(), null);
            source.set(ropsm, "source1");

            assertEquals("source1", ropsm.getSource());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testGetSource() failed");
        }
    }

    @Test
    public void testGetTarget() {
        final ROPTime ropsm = new ROPTime();

        try {
            target.set(ropsm, "target1");

            assertEquals("target1", ropsm.getTarget());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testGetTarget() failed");
        }
    }

    @Test
    public void testGetName() {
        final ROPTime ropsm = new ROPTime();

        try {
            name.set(ropsm, "name");

            assertEquals("name", ropsm.getName());
        } catch (Exception e) {
            e.printStackTrace();
            fail("testGetName() failed");
        }
    }
}
