package com.distocraft.dc5000.etl.parserlink;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created on FEB 01, 2022
 * 
 * A property implementation that lies as static entity
 * 
 * @author zraosud
 */
public class StaticProperties {
	
	  private static Logger log;
	  
	  public static final String CONFIG_DIR = "/eniq/sw/conf";

	  private static final String PROPERTYFILENAME = "symboliclinkcreator.properties";



	  private static java.util.Properties props = null;
	  
	  public static final String DEFAULT_NO_RESPONSE_MONITOR_TIMEOUT = "180"; //In Minutes
	  public static final String DEFAULT_NO_RESPONSE_MONITOR_PERIOD ="5"; //In Minutes
	  public static final String DEFAULT_SYMBOLICLINK_FLSMAXIDTHREAD_INTERVAL = "10"; //In Minutes
	  public static final String DEFAULT_TEST_MODE = "false";
	  public static final String DEFAULT_REPEAT_COUNT_STRING = "1";
	  public static final String DEFAULT_REPEAT_COUNT_ML_STRING = "3";
	  
	  /** starts at one of the following execution time and then follows the sequence 
	   *  execution times - 7,17,27,37,47,57
	   *  e.g. if starting at 27th minute , the next execution will be at 37th and so on
	   * */
	  public static final int DEFAULT_MAX_ID_MONITOR_EXEC_TIME = 7;
	  //for cENM, BulkCM
	  public static final String DEFAULT_BULKCM_START_INTERVAL="0";
	  public static final String DEFAULT_BULKCM_DELAY_INTERVAL="24";
	  public static final String DEFAULT_JOB_POST_REQUEST_FILEFORMAT="3GPP";
	   

	  private StaticProperties(){ }

	  /**
	   * Returns the value of defined property
	   * 
	   * @param name
	   *          name of property
	   * @return value of property
	   * @throws NoSuchFieldException
	   *           is thrown if property is not defined
	   */
	  public static String getProperty(final String name) throws NoSuchFieldException {

	    if (props == null) {
	      throw new NullPointerException("StaticProperties class is not initialized"); //NOPMD
	    }
	      
	    final String value = props.getProperty(name);
	    if(value == null){
	      throw new NoSuchFieldException("Property " + name + " not defined in " + PROPERTYFILENAME);
	    } else {
	      return value;
	    }
	  }

	  /**
	   * Returns the value of defined property with default value.
	   * 
	   * @param name
	   *          name of property
	   * @param defaultValue
	   *          default value of property that is used if property is not defined
	   *          on config-file.
	   * @return value of property
	   */
	  public static String getProperty(final String name, final String defaultValue) {

	    if (props == null) {
	      throw new NullPointerException("StaticProperties class is not initialized"); //NOPMD
	    }
	      
	    return props.getProperty(name, defaultValue);
	  }

	  /**
	   * Reloads the configuration file
	   * 
	   * @throws IOException
	   *           thrown in case of failure
	   */
	  public static void reload() throws IOException {
		  
	    try {

	      final java.util.Properties nprops = new java.util.Properties();
	      final File confFile = getStaticFile();
	      try(FileInputStream fis = new FileInputStream(confFile);) {
	        
	        nprops.load(fis);
	      } 
	      
	      props = nprops;
	      


	    } catch (IOException e) {
	    	log.warning("Exception "+e);  
	    }

	  }
	  

	  private static File getStaticFile() throws IOException {

		String confDir = CONFIG_DIR;
	    if (confDir.length()<1) {
	      throw new NullPointerException("System property " + CONFIG_DIR + " must be defined"); //NOPMD
	    }
	    if (!confDir.endsWith(File.separator)) {
	      confDir += File.separator;
	    }
	    final File confFile = new File(confDir + PROPERTYFILENAME);
	    if (!confFile.exists() && !confFile.isFile() && !confFile.canRead()) {
	      throw new IOException("Unable to read configFile from " + confFile.getCanonicalPath());
	    }
	    return confFile;
	  }

	  public static void save() throws IOException {

	    final File confFile = getStaticFile();
	    try(FileOutputStream fos = new FileOutputStream(confFile);) {
	      
	      props.store(fos, "Saving static.properties");

	    } catch (Exception e) {

	    	log.warning("Exception "+e);
	    } 
	  }

	  /**
	   * Reloads the configuration file
	   * @param nprops To set
	   */
	  public static void giveProperties(final java.util.Properties nprops) {
	    props = (java.util.Properties) nprops.clone();
	  }

	   /**
	   * Updates the property name and value
	   *
	   * @param updatedName - Property name
	   * @param updatedValue - Property value
	    * @return TRUE is property set and saved, false otherwise
	   */
	  public static boolean setProperty(final String updatedName, final String updatedValue) {

	    if (props == null) {

	      return false;
	    }
	    // run the new save method here
	    try {
	      props.setProperty(updatedName, updatedValue);
	      save();
	      reload();
	      return true;
	    } catch (Exception e) {
	    	log.warning(" Exception "+e );
	      return false;
	    }
	  }
}
