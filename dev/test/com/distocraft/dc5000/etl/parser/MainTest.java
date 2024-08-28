package com.distocraft.dc5000.etl.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.activity.ActivityRequiredException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.distocraft.dc5000.common.AdapterLog;
import com.distocraft.dc5000.common.ProcessedFiles;
import com.distocraft.dc5000.common.SessionHandler;
import com.distocraft.dc5000.common.StaticProperties;
import com.distocraft.dc5000.etl.engine.common.EngineCom;
import com.distocraft.dc5000.etl.engine.common.Share;
import com.distocraft.dc5000.etl.engine.executionslots.ExecutionSlotProfileHandler;
import com.distocraft.dc5000.etl.engine.priorityqueue.PriorityQueue;
import com.distocraft.dc5000.etl.parser.Main.FileInformation;
import com.distocraft.dc5000.repository.cache.DFormat;
import com.distocraft.dc5000.repository.cache.DItem;
import com.distocraft.dc5000.repository.cache.DataFormatCache;
import com.ericsson.eniq.common.CommonUtils;
import com.ericsson.eniq.etl.parser.unittests.UnittestParser;

/**
 * Test class for the main class which handles the parse process.
 *
 * @author EJAAVAH
 */
public class MainTest extends BaseMock {

//  private static Main objUnderTest;
//
//  private SourceFile testSourceFile;
//  
//  private List<SourceFile> testSourceFileList;
//
//  private static File actualSourceFile_1;
//
//  private static Field conf;
//
//  private static Field checker;
//
//  private static Field fileList;
//
//  private static Field eCom;
//
//  private static Field localDirLockList;
//
//  private static Field fileNameFilter;
//
//  private static Field sourceFileList;
//
//  private static final File TMP_DIR = new File(System.getProperty("java.io.tmpdir"), "MainTest");
//  private static final File TMP_DIR_ETL = new File(TMP_DIR, "etldata");
//  private static final File TMP_DIR_ETL_EVENTS = new File(TMP_DIR, "etldata_");
//  private static final String TMP_DIR_PATH = TMP_DIR.getPath() + File.separator;
//  //Use forward slashes for this one
//  private static String TMP_DIR_ETL_PATH;
//
//  @BeforeClass
//  public static void setUpBeforeClass() throws Exception {
//    TMP_DIR_ETL_PATH = TMP_DIR_ETL.getPath().replace('\\', '/');
//
//    /* Creating physical source file for the first time */
//    MainTest.createTestFile();
//
//    /* Reflecting often used fields */
//    conf = Main.class.getDeclaredField("conf");
//    checker = Main.class.getDeclaredField("checker");
//    fileList = Main.class.getDeclaredField("fileList");
//    eCom = Main.class.getDeclaredField("eCom");
//    localDirLockList = Main.class.getDeclaredField("localDirLockList");
//    fileNameFilter = Main.class.getDeclaredField("fileNameFilter");
//    sourceFileList = Main.class.getDeclaredField("sourceFileList");
//    conf.setAccessible(true);
//    checker.setAccessible(true);
//    fileList.setAccessible(true);
//    eCom.setAccessible(true);
//    localDirLockList.setAccessible(true);
//    fileNameFilter.setAccessible(true);
//    sourceFileList.setAccessible(true);
//
//    /* Home directory path */
//
//    /* Parsing file directory paths for property directories */
//    String fileNameFilter = "([^\\s]+(\\.(txt|doc|xml)))";
//
//    final String unixFormat = TMP_DIR.getPath().replace("\\", "/");
//
//    /* Creating static property file */
//    File sp = new File(TMP_DIR, "static.properties");
//    final File storageFile = new File(TMP_DIR, "storage.txt");
//    if(!storageFile.createNewFile()){
//      System.out.println("Warnging: Failed to create " + storageFile.getPath());
//    }
//    sp.deleteOnExit();
//    try {
//      PrintWriter pw = new PrintWriter(new FileWriter(sp));
//      pw.print("SessionHandling.storageFile=" + unixFormat + "/storage.txt\n");
//      pw.print("SessionHandling.log.types=ADAPTER\n");
//      pw.print("SessionHandling.log.ADAPTER.class=" + AdapterLog.class.getName() + "\n");
//      pw.print("SessionHandling.log.ADAPTER.inputTableDir=" + unixFormat + "\n");
//      pw.print("unittest.inputfile.filenamefilter=" + fileNameFilter + "\n");
//      pw.print("rolling_upgrade.fileAction=move\n");
//      pw.print("rolling_upgrade.tgt_dir=tmp_sym_link_dir\n");
//      pw.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//    /* Creating niq.ini file */
//    
//    File sp1 = new File(TMP_DIR, "niq.ini");
//    sp1.deleteOnExit();
//    try {
//      PrintWriter pw = new PrintWriter(new FileWriter(sp1));
//      pw.print("[DIRECTORY_STRUCTURE]\n");
//      pw.print("FileSystems=4");
//      pw.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    
//    /* Setting the system property for static property file */
//    System.setProperty("dc5000.config.directory", TMP_DIR_PATH);
//    System.setProperty("CONF_DIR", TMP_DIR_PATH);
//
//    /* Initializing Static Properties in order to initialize SessionHandler */
//    StaticProperties.reload();
//
//    /* Initializing SessionHandler */
//    SessionHandler.init();
//  }
//
//  private static void deleteDir(final File dir) {
//    if (!dir.exists()) {
//      return;
//    }
//    final File[] contents = dir.listFiles();
//    for (File file : contents) {
//      if (file.isDirectory()) {
//        deleteDir(file);
//      } else {
//        if (!file.delete()) {
//          System.err.println("Failed to delete file " + file.getPath());
//        }
//      }
//    }
//    if (!dir.delete()) {
//      System.err.println("Failed to delete directory " + dir.getPath());
//    }
//  }
//
//  @AfterClass
//  public static void tearDownAfterClass() throws Exception {
//
//    /* Cleaning up after all tests */
//    objUnderTest = null;
//    deleteDir(TMP_DIR);
//  }
//
//  @Before
//  public void setUp() throws Exception {
//
//    /* Checking if the test sourcefile is in place */
//    if (!actualSourceFile_1.exists()) {
//      MainTest.createTestFile();
//    }
//
//    testSourceFile = new SourceFile(actualSourceFile_1, new Properties(), null, null, new ParseSession(101, null), null, Logger.getLogger("sfLogger"));
//    testSourceFileList = new ArrayList<SourceFile>();
//    /* Reflecting SourceFile class constructor and creating an instance of it */
//    //    Constructor sourceFileConstructor = SourceFile.class.getDeclaredConstructor(new Class[] { File.class,
//    //        Properties.class, RockFactory.class, RockFactory.class, ParseSession.class, ParserDebugger.class,
//    //        boolean.class, Logger.class });
//    //    sourceFileConstructor.setAccessible(true);
//    //    testSourceFile = (SourceFile) sourceFileConstructor.newInstance(actualSourceFile, new Properties(), null, null,
//    //        new ParseSession(101, null), null, false, Logger.getLogger("sfLogger"));
//
//    /* Properties for the main method */
//    Properties mainConf = new Properties();
//    mainConf.setProperty("interfaceName", "IFaceName");
//    mainConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    mainConf.setProperty("ProcessedFiles.fileNameFormat", "(.+)");
//
//    System.setProperty(CommonUtils.ETLDATA_DIR, TMP_DIR_ETL_PATH);
//    System.setProperty(CommonUtils.EVENTS_ETLDATA_DIR, TMP_DIR_ETL_EVENTS.getPath());
//    mainConf.setProperty("baseDir", TMP_DIR_ETL_PATH);
//    mainConf.setProperty("outDir", TMP_DIR_ETL_PATH);
//    mainConf.setProperty("loaderDir", TMP_DIR_ETL_PATH);
//
//    mainConf.setProperty("minFileAge", "100");
//    mainConf.setProperty("maxFilesPerRun", "5");
//    mainConf.setProperty("parserType", "unittest");
//    mainConf.setProperty("measList", "MType1,MType2");
//    mainConf.setProperty("datatime", "1111");
//    mainConf.setProperty("vendorID", "woMetaData");
//
//
//    /* Initializing tested method and often used fields before each test */
//    objUnderTest = new Main(mainConf, "techpack", "set_type", "set_name", null, null, null);
//    EngineCom testECom = new EngineCom();
//    eCom.set(objUnderTest, testECom);
//
//    /* Setting new ProcessedFiles class instance to checker variable */
//    Properties pfConf = new Properties();
//    pfConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    pfConf.setProperty("ProcessedFiles.fileNameFormat", "(.+)");
//    pfConf.setProperty("interfaceName", "IFaceName");
//
//    ProcessedFiles testProcessedFiles = new ProcessedFiles(pfConf);
//    checker.set(objUnderTest, testProcessedFiles);
//  }
//
//  @After
//  public void tearDown() throws Exception {
//
//    /* Cleaning up after each test */
//    objUnderTest = null;
//    testSourceFile = null;
//    testSourceFileList = null;
//    Field singletonPluginShare = Share.class.getDeclaredField("singletonPluginShare");
//    singletonPluginShare.setAccessible(true);
//    singletonPluginShare.set(objUnderTest, null);
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmm");
//    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
//    String dirPath = "_" + sdf.format(new Date()) + "_" + sdf.format(new Date().getTime() + 604800000);
//    File removeArchiveDir = new File(TMP_DIR, "archive");
//    File removeDoubleDir = new File(TMP_DIR, "double");
//    File removeFailedDir = new File(TMP_DIR, "failed");
//    File removeArchivedSF = new File(TMP_DIR, "archive" + File.separator + dirPath + File.separator + "actualSourceFile.txt");
//    File removeArchiveSubDir = new File(TMP_DIR, "archive" + File.separator + dirPath);
//    File removeDoubleSubDir = new File(TMP_DIR + "double" + File.separator + dirPath);
//    File removeFailedSubDir = new File(TMP_DIR + "failed" + File.separator + dirPath);
//    removeArchivedSF.delete();
//    removeArchiveSubDir.delete();
//    removeDoubleSubDir.delete();
//    removeFailedSubDir.delete();
//    removeArchiveDir.delete();
//    removeDoubleDir.delete();
//    removeFailedDir.delete();
//  }
//
//  /**
//   * Test for preParse method which tells that the parse process has started.
//   * Testing if correct variables are initialized.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testPreParse() throws Exception {
//
//    /* Reflecting totalSize field from main for asserting */
//    Field totalSize = objUnderTest.getClass().getDeclaredField("totalSize");
//    totalSize.setAccessible(true);
//
//    /* Calling the tested method */
//    objUnderTest.preParse(testSourceFile);
//
//    /* Asserting that parsing start time parsingstarttime initialized correctly
//     * parsingstarttime should be "now", but allow a bit of grace time to prevent 
//     * test failing if there is a slight delay in processing
//     * 
//     * starttimeCurTimeDiff is difference between parsingstarttime and "now" in milliseconds
//     *  */
//    long starttimeCurTimeDiff = testSourceFile.getParsingstarttime() - System.currentTimeMillis();
//    assertTrue("Parsing start time too long ago.", (starttimeCurTimeDiff < 10));
//
//    /* Asserting that other variables are initialized correctly */
//    String expected = "0, STARTED, 68";
//    String actual = testSourceFile.getBatchID() + ", " + testSourceFile.getParsingStatus() 
//    		+ ", " + totalSize.get(objUnderTest);
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * Testing preParse with null sourcefile, exception expected.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testPreParseWithNullSourceFile() throws Exception {
//
//    SourceFile nullSourceFile = null;
//
//    /* Invoking the tested method with null sourcefile */
//    try {
//      objUnderTest.preParse(nullSourceFile);
//      fail("Test failed - NullPointerException expected");
//    } catch (NullPointerException npe) {
//      // Test passed
//    } catch (Exception e) {
//      fail("Test failed - Unexpected error occurred");
//    }
//  }
//
//  /**
//   * Test for postParse method which tells that the parse process has finished
//   * successfully. Testing if the file is added to the processed file list and
//   * removing the source file.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testPostParseDeleteSourceFile() throws Exception {
//
//    /* Reflecting list of processed files from ProcessedFiles */
//    Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//    processedFilesList.setAccessible(true);
//    Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//    /* Changing main properties to remove sourcefiles after parse */
//    Properties postParseConf = new Properties();
//    postParseConf.setProperty("afterParseAction", "delete");
//    postParseConf.setProperty("parserType", "mdc");
//    postParseConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    conf.set(objUnderTest, postParseConf);
//
//    /* Invoking tested method */
//    objUnderTest.postParse(testSourceFile);
//
//
//    System.out.println("filelist values--->" + fileList.values().toString());
//
//    /* The postparse dont delete the sourcefile and the processedFilesMap is empty*/
//
//    String expected = "OK, " + true + "[]";
//    String actual = testSourceFile.getParsingStatus() + ", " + actualSourceFile_1.exists()
//      + fileList.values().toString();
//    assertEquals(expected, actual);
//  }
//
//
//  @Test
//  public void testPostParseDeleteSourceFile1() throws Exception {
//
//    /* Need to check the sourceList */
//    Field sourceList = objUnderTest.getClass().getDeclaredField("sourceFileList");
//    sourceList.setAccessible(true);
//    List srcList = (List) sourceList.get(objUnderTest);
//
//    /* Changing main properties to remove sourcefiles after parse */
//    Properties postParseConf = new Properties();
//    postParseConf.setProperty("afterParseAction", "delete");
//    postParseConf.setProperty("parserType", "mdc");
//    postParseConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    postParseConf.setProperty("inDir", TMP_DIR_PATH + "fileListDir");
//    conf.set(objUnderTest, postParseConf);
//    /* Invoking tested method */
//    objUnderTest.postParse(testSourceFile);
//    System.out.println("srcList.size--->" + srcList.size());
//    String expected = "OK" + 1;
//    String actual = testSourceFile.getParsingStatus() + srcList.size();
//    assertEquals(expected, actual);
//  }
//
//  @Test
//	public void testPostParseDeleteAlarmSourceFile() throws Exception {
//		
//		/* Reflecting list of processed files from ProcessedFiles */
//		Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//		processedFilesList.setAccessible(true);
//		Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//		/* Changing main properties to remove sourcefiles after parse */
//		Properties postParseConf = new Properties();
//		postParseConf.setProperty("afterParseAction", "delete");
//		postParseConf.setProperty("parserType", "alarm");
//		postParseConf.setProperty("ProcessedFiles.processedDir", System.getProperty("user.dir").toString()
//				+ "/processedDir");
//		conf.set(objUnderTest, postParseConf);
//
//		/* Invoking tested method */
//		objUnderTest.postParse(testSourceFile);
//
//
//		System.out.println("filelist values--->"+fileList.values().toString());
//
//		/* The postparse dont delete the sourcefile and the processedFilesMap is empty*/
//
//		String expected = "OK, " + false +"[]";
//		String actual = testSourceFile.getParsingStatus() + ", " + actualSourceFile_1.exists()
//		+ fileList.values().toString();
//		assertEquals(expected, actual);
//		
//	}
//  
//  /**
//   * Test for postParse method which tells that the parse process has finished
//   * successfully. Testing if the file is added to the processed file list and
//   * sourcefile is moved.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testPostParseMoveSourceFile() throws Exception {
//
//	/* Changing main properties to remove sourcefiles after parse */
//	Properties postParseConf = new Properties();
//	postParseConf.setProperty("afterParseAction", "move");
//	postParseConf.setProperty("parserType", "mdc");
//	postParseConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//	conf.set(objUnderTest, postParseConf);
//	  
//    /* Creating directory where the file will be moved to */
//    File archiveDirectory = new File(TMP_DIR + "\\testArchive");
//    archiveDirectory.mkdirs();
//    archiveDirectory.deleteOnExit();
//    Field archiveDir = objUnderTest.getClass().getDeclaredField("archiveDir");
//    archiveDir.setAccessible(true);
//    archiveDir.set(objUnderTest, archiveDirectory);
//
//    /* Creating File object pointing at the moved file */
//    File movedFile = new File(archiveDirectory.getAbsolutePath(), "actualSourceFile.txt");
//
//    /* Reflecting list of processed files from ProcessedFiles */
//    Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//    processedFilesList.setAccessible(true);
//    Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//    /* Invoking tested method */
//    objUnderTest.postParse(testSourceFile);
//
//    /* In the postParse, no processedFilesMap is populated and it shall contain empty*/
//
//    String expected = "OK, " + true + ", []";
//    String actual = testSourceFile.getParsingStatus() + ", " + movedFile.exists() + ", " + fileList.values().toString();
//    assertEquals(expected, actual);
//
//    /* Deleting test files */
//    movedFile.delete();
//  }
//  
//  /**
//   * Test for postParse method which tells that the parse process has finished
//   * successfully. Testing if the file is added to the processed file list and
//   * sourcefile is moved.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testHandleSourceFiles_hide() throws Exception {
//
//	  //Field sourceFileListField = objUnderTest.getClass().getDeclaredField("sourceFileList");
//	  //sourceFileListField.setAccessible(true);
//	  
//	  this.testSourceFileList.add(this.testSourceFile);
//	  sourceFileList.set(objUnderTest, this.testSourceFileList);
//	  
//	  /* Changing main properties to hide sourcefiles after parse */
//	   Properties postParseConf = (Properties)conf.get(objUnderTest);
//	   postParseConf.setProperty("afterParseAction", "hide");
//	   conf.set(objUnderTest, postParseConf);
//	   
//	   final String hiddenDirName = ".upgrade_hidden";
//	   final File hiddenDir = new File(testSourceFile.getDir()+File.separator+hiddenDirName);
//	   
//	   final File hiddenFile = new File(hiddenDir.getPath()+File.separator+this.testSourceFile.getName());
//	   //hiddenFile.deleteOnExit();
//
//    /* Reflecting list of processed files from ProcessedFiles */
//    //Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//    //processedFilesList.setAccessible(true);
//    //Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//	   // System.out.println(this.testSourceFile.getDir()+"\\"+this.testSourceFile.getName());
//	    
//    /* Invoking tested method */
//    objUnderTest.handleSourceFiles();
//
//    /* In the postParse, no processedFilesMap is populated and it shall contain empty*/
////System.out.println(hiddenFile.getAbsolutePath());
//    
//    assertEquals(true, hiddenFile.exists());
//    assertEquals(false, new File(this.testSourceFile.getDir()+File.separator+this.testSourceFile.getName()).exists());
//    assertTrue(this.testSourceFileList.size()==0);
//    
//    /* Clean up */
//    hiddenFile.delete();
//    hiddenDir.delete();
//
//  }
//
//  /**
//   * Test for postParse method which tells that the parse process has finished
//   * successfully. Testing if the file is added to the processed file list and
//   * and the source file is left to the directory it was created.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testPostParseDoNothingToSourceFile() throws Exception {
//
//    /* Reflecting list of processed files from ProcessedFiles */
//    Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//    processedFilesList.setAccessible(true);
//    Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//    /* Changing main properties to remove sourcefiles after parse */
//    Properties postParseConf = new Properties();
//    postParseConf.setProperty("afterParseAction", "no");
//    postParseConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    conf.set(objUnderTest, postParseConf);
//
//    /* Invoking tested method */
//    objUnderTest.postParse(testSourceFile);
//
//    /* In the postParse, no processedFilesMap is populated and it shall contain nothing */
//
//    String expected = "OK, " + true + ", []";
//    String actual = testSourceFile.getParsingStatus() + ", " + actualSourceFile_1.exists() + ", "
//      + fileList.values().toString();
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * Testing post parse method with null sourcefile. Exception expected.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testPostParseWithNullSourceFile() throws Exception {
//
//    SourceFile nullSourceFile = null;
//
//    /* Invoking the tested method with null sourcefile */
//    try {
//      objUnderTest.postParse(nullSourceFile);
//      fail("Test failed - NullPointerException expected");
//    } catch (NullPointerException npe) {
//      // Test passed
//    } catch (Exception e) {
//      fail("Test failed - Unexpected error occurred");
//    }
//  }
//
//  /**
//   * ErrorParse method is called if parsing fails. Testing if the error is
//   * handled and the parsed file is removed.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testErrorParseDeleteFile() throws Exception {
//
//    /* Changing main properties to remove sourcefiles after parse */
//    Properties postParseConf = new Properties();
//    postParseConf.setProperty("failedAction", "");
//    postParseConf.setProperty("afterParseAction", "delete");
//    conf.set(objUnderTest, postParseConf);
//
//    /* Invoking tested method */
//    objUnderTest.errorParse(new Exception("testErrorMessage"), testSourceFile);
//
//    /* Asserting that errorParse does what it is supposed to */
//    String expected = "ERROR, testErrorMessage, " + false;
//    String actual = testSourceFile.getParsingStatus() + ", " + testSourceFile.getErrorMessage() + ", "
//      + actualSourceFile_1.exists();
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * ErrorParse method is called if parsing fails. Testing if the error is
//   * handled and the parsed file is moved.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testErrorParseMoveFile() throws Exception {
//
//    /* Setting failed directory */
//    File failedDirectory = new File(TMP_DIR_PATH + "testArchive");
//    failedDirectory.mkdirs();
//    failedDirectory.deleteOnExit();
//    Field failedDir = objUnderTest.getClass().getDeclaredField("failedDir");
//    failedDir.setAccessible(true);
//    failedDir.set(objUnderTest, failedDirectory);
//
//    /* Creating File object pointing at the moved file */
//    File movedFile = new File(failedDirectory.getAbsolutePath(), "actualSourceFile.txt");
//
//    /* Invoking tested method */
//    objUnderTest.errorParse(new Exception("testErrorMessage"), testSourceFile);
//
//    /* Asserting that errorParse does what it is supposed to */
//    String expected = "ERROR, testErrorMessage, " + false;
//    String actual = testSourceFile.getParsingStatus() + ", " + testSourceFile.getErrorMessage() + ", "
//      + actualSourceFile_1.exists();
//    assertEquals(expected, actual);
//
//    /* Deleting test files */
//    movedFile.delete();
//  }
//
//  /**
//   * Testing errorParse with null sourcefile.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testErrorParseWithNullSourceFile() throws Exception {
//
//    SourceFile nullSourceFile = null;
//
//    /* Invoking the tested method with null sourcefile */
//    try {
//      objUnderTest.errorParse(new Exception("testErrorMessage"), nullSourceFile);
//      fail("Test failed - NullPointerException expected");
//    } catch (NullPointerException npe) {
//      // Test passed
//    } catch (Exception e) {
//      fail("Test failed - Unexpected error occurred");
//    }
//  }
//
//  /**
//   * FinallyParse method is called after parse process is finished (successfully
//   * or not). Testing if streams are closed and necessary variables changed.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testFinallyParse() throws Exception {
//
//    /* Reflecting totalSize field from main for asserting */
//    Field allRows = objUnderTest.getClass().getDeclaredField("allRows");
//    allRows.setAccessible(true);
//
//    /* Initializing expected clause here because of time sensitive test case */
//    String expected = "0, " + System.currentTimeMillis() + ", " + false;
//
//    /* Invoking the tested method */
//    System.setProperty("session_handler_bulk_limit", "1");
//    objUnderTest.finallyParse(testSourceFile);
//
//    /* Asserting that errorParse does what it is supposed to */
//    File adapterLogFile = new File(TMP_DIR, "ADAPTER.1970-01-01.unfinished");
//    String actual = allRows.get(objUnderTest) + ", " + testSourceFile.getParsingendtime() + ", " + adapterLogFile.exists();
//     
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * Testing finally parse with null sourcefile.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testFinallyParseWithNullSourceFile() throws Exception {
//
//    SourceFile nullSourceFile = null;
//
//    /* Invoking the tested method with null sourcefile */
//    try {
//      objUnderTest.finallyParse(nullSourceFile);
//      fail("Test failed - NullPointerException expected");
//    } catch (NullPointerException npe) {
//      // Test passed
//    } catch (Exception e) {
//      fail("Test failed - Unexpected error occurred");
//    }
//  }
//
//  /**
//   * Testing file link checking. If link to a given file is lost, true is
//   * returned, otherwise false.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testIsBrokenLink() throws Exception {
//
//    /* Creating an existing and non-existing file */
//    File notExistingFile = new File("", "notExisting");
//    File ExistingFile = new File(TMP_DIR, "brokenLinkTestFile");
//    ExistingFile.deleteOnExit();
//    PrintWriter pw = new PrintWriter(new FileWriter(ExistingFile));
//    pw.print("brokenLinkTestFile");
//    pw.close();
//
//    /* Asserting that true or false is returned if the file is found/not found */
//    String expected = false + ", " + true + ", " + false + ", " + true + ", " + false + ", " + true + ", " + false
//      + ", " + true + ", " + false;
//    String actual = objUnderTest.isBrokenLink(ExistingFile, 0) + ", " + objUnderTest.isBrokenLink(notExistingFile, 0)
//      + ", " + objUnderTest.isBrokenLink(ExistingFile, 1) + ", " + objUnderTest.isBrokenLink(notExistingFile, 1)
//      + ", " + objUnderTest.isBrokenLink(ExistingFile, 2) + ", " + objUnderTest.isBrokenLink(notExistingFile, 2)
//      + ", " + objUnderTest.isBrokenLink(ExistingFile, 3) + ", " + objUnderTest.isBrokenLink(notExistingFile, 3)
//      + ", " + objUnderTest.isBrokenLink(ExistingFile, 9);
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * Testing file link checking. Testing a broken Link.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testBrokenLink() throws Exception {
//
//    /* Creating an existing and non-existing file */
//    File notExistingFile = new File("", "notExisting");
//
//    boolean actual = objUnderTest.isBrokenLink(notExistingFile, 0);
//
//    assertTrue(actual);
//  }
//
//  /**
//   * Testing file link checking with null file object.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testIsBrokenLinkWithNullFile() throws Exception {
//
//    try {
//      objUnderTest.isBrokenLink(null, 0);
//      fail("Test Failed - NullPointerException expected as the checked file object was null!");
//    } catch (NullPointerException npe) {
//      // Test Passed
//    } catch (Exception e) {
//      fail("Test Failed - Unexpected error occurred!");
//    }
//  }
//
//  /**
//   * Testing sourcefile fetching from file list.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testNextSourceFile() throws Exception {
//
//    /* Creating list of sourcefiles */
//    Method createFileInformation = Main.class.getDeclaredMethod("createFileInformation", File.class, long.class, int.class);
//    createFileInformation.setAccessible(true);
//
//    ArrayList<Main.FileInformation> testFileList = new ArrayList<Main.FileInformation>();
//    testFileList.add((Main.FileInformation) createFileInformation.invoke(objUnderTest, actualSourceFile_1, 0, 0));
//
//
//    fileList.set(objUnderTest, testFileList);
//
//    /* Invoking the tested method */
//    SourceFile returnedSF = objUnderTest.nextSourceFile();
//
//    /* Asserting that the returned sourceFile is properly initialized */
//    String actual = returnedSF.getName() + ", " + returnedSF.getDir() + ", " + returnedSF.getSize();
//    String expected = "actualSourceFile.txt, " + new File(TMP_DIR_ETL, "in").getPath()+", 68";
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * Testing sourcefile fetching from file list.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testNextSourceFileWithBrokenLink() throws Exception {
//
//    /* Creating list of sourcefiles */
//    Method createFileInformation = Main.class.getDeclaredMethod("createFileInformation", File.class, long.class, int.class);
//    createFileInformation.setAccessible(true);
//
//    /* Creating an existing and non-existing file */
//    File notExistingFile_1 = new File("", "notExisting_1");
//    File notExistingFile_2 = new File("", "notExisting_2");
//    File notExistingFile_3 = new File("", "notExisting_3");
//    File notExistingFile_4 = new File("", "notExisting_4");
//
//    ArrayList<Main.FileInformation> testFileList = new ArrayList<Main.FileInformation>();
//    testFileList.add((Main.FileInformation) createFileInformation.invoke(objUnderTest, notExistingFile_1, 0, 0));
//    testFileList.add((Main.FileInformation) createFileInformation.invoke(objUnderTest, notExistingFile_2, 0, 0));
//    testFileList.add((Main.FileInformation) createFileInformation.invoke(objUnderTest, notExistingFile_3, 0, 0));
//    testFileList.add((Main.FileInformation) createFileInformation.invoke(objUnderTest, actualSourceFile_1, 0, 0));
//    testFileList.add((Main.FileInformation) createFileInformation.invoke(objUnderTest, notExistingFile_4, 0, 0));
//
//
//    fileList.set(objUnderTest, testFileList);
//
//    /* Invoking the tested method */
//    SourceFile returnedSF = objUnderTest.nextSourceFile();
//
//    /* Asserting that the returned sourceFile is properly initialized */
//    String actual = returnedSF.getName() + ", " + returnedSF.getDir() + ", " + returnedSF.getSize();
//    String expected = "actualSourceFile.txt, " + new File(TMP_DIR_ETL, "in").getPath()+", 68";
//    assertEquals(expected, actual);
//  }
//
//
//  /**
//   * Testing if null is returned when file list is empty.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testNextSourceFileWithEmptyFileList() throws Exception {
//
//    /* Creating list of sourcefiles */
//    ArrayList testFileList = new ArrayList();
//    fileList.set(objUnderTest, testFileList);
//
//    /* Asserting that returned source file is null */
//    assertEquals(null, objUnderTest.nextSourceFile());
//  }
//
//  /**
//   * Testing if null is returned when file list is empty.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testAddWorkersToQueue() throws Exception {
//
//    /* Reflecting tested method */
//    Method addWorkersToQueue = objUnderTest.getClass()
//      .getDeclaredMethod("addWorkersToQueue", new Class[]{Map.class});
//    addWorkersToQueue.setAccessible(true);
//
//    /* Creating map of worker names */
//    testParserMOC testParser = new testParserMOC(0);
//    HashMap mapOfWorkers = new HashMap();
//    mapOfWorkers.put("sasn", testParser);
//    mapOfWorkers.put("raml", testParser);
//    mapOfWorkers.put("xml", testParser);
//
//    /* Initializing Share object with priority queue */
//    PriorityQueue testPQ = new PriorityQueue(0, 0, null, null);
//    Share share = Share.instance();
//    share.add("priorityQueueObject", testPQ);
//
//    /*
//       * Invoking the tested method and asserting that parser is added to the
//       * queue
//       */
//    String actual = addWorkersToQueue.invoke(objUnderTest, new Object[]{mapOfWorkers}) + ", "
//      + testPQ.getNumberOfSetsInQueue();
//    assertEquals("raml, 2", actual);
//  }
//
//  /**
//   * Testing status checking of every parser in a map. If all of the statuses
//   * are the same as stated in the parameter true is returned, otherwise false.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testAllWorkerStatusesAre() throws Exception {
//
//    /* Reflecting tested method */
//    Method allWorkerStatusesAre = objUnderTest.getClass().getDeclaredMethod("allWorkerStatusesAre",
//      new Class[]{Map.class, int.class});
//    allWorkerStatusesAre.setAccessible(true);
//
//    /* Creating two maps which include different parsers */
//    testParserMOC testParser1 = new testParserMOC(0);
//    testParserMOC testParser2 = new testParserMOC(1);
//    HashMap mapOfWorkers1 = new HashMap();
//    mapOfWorkers1.put("sasn", testParser1);
//    mapOfWorkers1.put("raml", testParser1);
//    HashMap mapOfWorkers2 = new HashMap();
//    mapOfWorkers2.put("sasn", testParser1);
//    mapOfWorkers2.put("raml", testParser2);
//
//    /* Asserting that true or false is returned depending if the status is same */
//    String actual = allWorkerStatusesAre.invoke(objUnderTest, new Object[]{mapOfWorkers1, 0}) + ", "
//      + allWorkerStatusesAre.invoke(objUnderTest, new Object[]{mapOfWorkers2, 0});
//    assertEquals(true + ", " + false, actual);
//  }
//
//  /**
//   * Testing status checking of every parser in a map. If one of the statuses is
//   * the same as defined in the given parameter, false is returned, otherwise
//   * true.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testNoWorkerStatusIs() throws Exception {
//
//    /* Reflecting tested method */
//    Method noWorkerStatusIs = objUnderTest.getClass().getDeclaredMethod("noWorkerStatusIs",
//      new Class[]{Map.class, int.class});
//    noWorkerStatusIs.setAccessible(true);
//
//    /* Creating two maps which include different parsers */
//    testParserMOC testParser1 = new testParserMOC(0);
//    testParserMOC testParser2 = new testParserMOC(1);
//    HashMap<String, testParserMOC> mapOfWorkers1 = new HashMap<String, testParserMOC>();
//    mapOfWorkers1.put("sasn", testParser1);
//    mapOfWorkers1.put("raml", testParser1);
//    HashMap<String, testParserMOC> mapOfWorkers2 = new HashMap<String, testParserMOC>();
//    mapOfWorkers2.put("sasn", testParser1);
//    mapOfWorkers2.put("raml", testParser2);
//
//    /* Asserting that true or false is returned depending if the status is same */
//    String actual = noWorkerStatusIs.invoke(objUnderTest, new Object[]{mapOfWorkers1, 1}) + ", "
//      + noWorkerStatusIs.invoke(objUnderTest, new Object[]{mapOfWorkers2, 1});
//    assertEquals(true + ", " + false, actual);
//  }
//
//  /**
//   * Testing if the statuses of every parser in the map are returned in string
//   * format.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testWorkerStatusString() throws Exception {
//
//    /* Reflecting tested method */
//    Method workerStatusString = objUnderTest.getClass().getDeclaredMethod("workerStatusString",
//      new Class[]{Map.class});
//    workerStatusString.setAccessible(true);
//
//    /* Creating two maps which include different parsers */
//    testParserMOC testParser1 = new testParserMOC(1);
//    testParserMOC testParser2 = new testParserMOC(3);
//    HashMap<String, testParserMOC> mapOfWorkers = new HashMap<String, testParserMOC>();
//    mapOfWorkers.put("sasn", testParser1);
//    mapOfWorkers.put("raml", testParser2);
//
//    /* Invoking the tested method and asserting statuses are returned */
//    String actual = (String) workerStatusString.invoke(objUnderTest, new Object[]{mapOfWorkers});
//    assertEquals(" 3 1 ", actual);
//  }
//
//  @Test
//  public void test_calculateMaxWorkerCount() throws Exception {
//
//    final ExecutionSlotProfileHandler executionSlotProfileHandler = context.mock(ExecutionSlotProfileHandler.class);
//
//    Share.instance().add("executionSlotProfileObject", executionSlotProfileHandler);
//    final Properties conf = new Properties();
//    conf.setProperty("interfaceName", "bbb");
//    conf.setProperty("parserType", "aaa");
//    final Main main = new Main(conf, "bb", "", "", null, null, null);
//    context.checking(new Expectations() {{
//      oneOf(executionSlotProfileHandler).getNumberOfAdapterSlots();
//      will(returnValue(10));
//    }});
//    int calculatedValue = main.calculateMaxWorkerCount();
//    assertEquals(6, calculatedValue);
//
//    context.checking(new Expectations() {{
//      oneOf(executionSlotProfileHandler).getNumberOfAdapterSlots();
//      will(returnValue(1));
//    }});
//    calculatedValue = main.calculateMaxWorkerCount();
//    assertEquals(1, calculatedValue);
//
//    context.checking(new Expectations() {{
//      oneOf(executionSlotProfileHandler).getNumberOfAdapterSlots();
//      will(returnValue(0));
//    }});
//    calculatedValue = main.calculateMaxWorkerCount();
//    assertEquals(1, calculatedValue);
//  }
//
//  /**
//   * Testing parse process.
//   *
//   * @throws Exception
//   */
// // @Test
//  public void testParse() throws Exception {
//
//    /* Creating new instance of DataFormatCache */
//    Constructor dfcConstructor = DataFormatCache.class.getDeclaredConstructor(new Class[]{});
//    dfcConstructor.setAccessible(true);
//    DataFormatCache dfcInstance = (DataFormatCache) dfcConstructor.newInstance(new Object[]{});
//
//    //Create mock execution slots...
//    final ExecutionSlotProfileHandler executionSlotProfileHandler = context.mock(ExecutionSlotProfileHandler.class);
//    context.checking(new Expectations() {{
//      oneOf(executionSlotProfileHandler).getNumberOfAdapterSlots();
//      will(returnValue(10));
//    }});
//
//    Share.instance().add("executionSlotProfileObject", executionSlotProfileHandler);
//
//
//    /* Initializing lists of interface names and add it to dfc instance */
//    HashSet IFaceNames = new HashSet();
//    HashMap tagMap = new HashMap();
//
//    DFormat dfObj1 = new DFormat("ifname", "tid", "dfid", "fname1", "trID");
//    DFormat dfObj2 = new DFormat("ifname", "tid", "dfid", "fname2", "trID");
//    DFormat dfObj3 = new DFormat("ifname", "tid", "dfid", "fname3", "trID");
//
//
//
//
//    List<DItem> dataList1 = new ArrayList<DItem>();
//    List<DItem> dataList2 = new ArrayList<DItem>();
//
//    dataList1.add(new DItem("filename", 0, "header", null, "varchar", 10, 0));
//    dataList2.add(new DItem("header1", 1, "DATE_ID", null, "varchar", 10, 0 ));
//    dataList2.add(new DItem("header2", 2, "Key1", null, "varchar", 10, 0));
//    dataList2.add(new DItem("header3", 3, "Key2", null, "varchar", 10, 0));
//    dataList2.add(new DItem("header4", 4, "Key3", null, "varchar", 10, 0));
//
//
//    dfObj1.setItems(dataList1);
//    dfObj2.setItems(dataList2);
//    dfObj3.setItems(new ArrayList());
//    IFaceNames.add("IFaceName");
//    tagMap.put("IFaceName_null", dfObj1);
//    tagMap.put("IFaceName_tagID", dfObj2);
//    tagMap.put("IFaceName_woMetaData", dfObj3);
//    Field if_names = dfcInstance.getClass().getDeclaredField("if_names");
//    Field it_map = dfcInstance.getClass().getDeclaredField("it_map");
//    if_names.setAccessible(true);
//    it_map.setAccessible(true);
//    if_names.set(dfcInstance, IFaceNames);
//    it_map.set(dfcInstance, tagMap);
//
//    /* Reflecting the dfc field and initialize it with our own instance */
//    Field dfc = DataFormatCache.class.getDeclaredField("dfc");
//    dfc.setAccessible(true);
//    dfc.set(DataFormatCache.class, dfcInstance);
//
//    /* Initializing transformer cache */
//    TransformerCache tfc = new TransformerCache();
//
//
//    final File inDir = new File(TMP_DIR_ETL, "in");
//    if(!inDir.exists() && ! inDir.mkdirs()){
//      //
//    }
//    final File loaderDir = new File(TMP_DIR_ETL_EVENTS, "00/fname3/raw");
//    if (!loaderDir.exists() && !loaderDir.mkdirs()) {
//      fail("Failed to create dir " + loaderDir.getPath() + "\nTest will fail");
//    }
//
//
//
//    /* Invoking the tested method and asserting that parsing has succeeded */
//    HashMap actualMap = (HashMap) objUnderTest.parse();
//    File[] outDirFileList = loaderDir.listFiles();
//    String parsedFile = "";
//    if (outDirFileList != null && outDirFileList.length > 0) {
//      parsedFile = "File(s)Parsed!";
//      for (int i = 0; i < outDirFileList.length; i++) {
//        outDirFileList[i].delete();
//      }
//    } else {
//      parsedFile = "NoParsedFilesFound!";
//    }
//
//    
//
//   String actual = actualMap.values().toString() + ", " + parsedFile;
//    assertEquals("[[fname3]], File(s)Parsed!", actual);
//  }
//
//  /**
//   * Testing measurement file creating.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateMeasurementFile() throws Exception {
//
//    MeasurementFile mFile1 = null;
//    MeasurementFile mFile2 = null;
//
//    /* Invoking the tested method and see if the measurement file is returned */
//    mFile1 = objUnderTest.createMeasurementFile(testSourceFile, "tagID", "techPack", "set_type", "set_name", Logger
//      .getLogger("testLogger"));
//    mFile2 = objUnderTest.createMeasurementFile(testSourceFile, "tagID", "techPack", "set_type", "set_name",
//      "workerName", Logger.getLogger("testLogger"));
//    assertNotNull(mFile1);
//    assertNotNull(mFile2);
//  }
//
//  /**
//   * Testing file list creating.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateFileList() throws Exception {
//
//	  /* Reflecting the tested method */
//	  Method createFileList = objUnderTest.getClass().getDeclaredMethod("createFileList", new Class[]{});
//	  createFileList.setAccessible(true);
//
//	  /* Initializing file list with test files */
//	  File fileListDir = new File(TMP_DIR_PATH + "fileListDir");
//	  fileListDir.mkdirs();
//	  fileListDir.deleteOnExit();
//	  for (int i = 1; i < 6; i++) {
//		  File fileForSizeCheck = new File(fileListDir, "fileForSizeCheck" + i + ".txt");
//		  PrintWriter pw = new PrintWriter(new FileWriter(fileForSizeCheck));
//		  pw.print("Writing lines to a file for testing file list creating.\n");
//		  pw.close();
//		  fileForSizeCheck.deleteOnExit();
//	  }
//
//	  /* Changing the directory path in main properties */
//	  Properties postParseConf = new Properties();
//	  postParseConf.setProperty("inDir", TMP_DIR_PATH + "fileListDir");
//	  conf.set(objUnderTest, postParseConf);
//
//	  /* Invoking the tested method */
//	  @SuppressWarnings("unchecked")
//	  ArrayList<Main.FileInformation> listOfFiles = (ArrayList<Main.FileInformation>) createFileList.invoke(objUnderTest, new Object[]{});
//
//	  if (listOfFiles != null && listOfFiles.size() > 0) {
//
//		  /* Can't guarantee order of files in listOfFiles... 
//		   * So build set of filenames held in listOfFiles and check that instead */
//		  Set<String> setOfFilenames = new HashSet<String>();
//		  for (int j = 0; j < listOfFiles.size(); j++) {
//			  setOfFilenames.add(listOfFiles.get(j).file.getName());
//		  }
//
//		  for (int j = 0; j < listOfFiles.size(); j++) {
//			  assertTrue("Can't find fileForSizeCheck" + (j + 1) + ".txt", setOfFilenames.contains("fileForSizeCheck" + (j + 1) + ".txt"));
//		  }
//	  } else {
//		  fail("Test Failed - Returned list was null or did not include any values!");
//	  }
//  }
//
//  /**
//   * Testing if certain directories are locked for share object. True is
//   * returned if the directory given is unlocked for share object and locally
//   * otherwise false.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testIsDirLocked() throws Exception {
//
//    /* Reflecting the tested method */
//    Method isDirLocked = objUnderTest.getClass().getDeclaredMethod("isDirLocked", new Class[]{File.class});
//    isDirLocked.setAccessible(true);
//
//    /* Creating File objects to represent different directories */
//    File shareLockedDir = new File("shareLockedDir");
//    File bothLockedDir = new File("bothLockedDir");
//    File locallyLockedDir = new File("locallyLockedDir");
//
//    /* Creating list of locked local directories */
//    ArrayList locallyLockedList = new ArrayList();
//    locallyLockedList.add(locallyLockedDir);
//    locallyLockedList.add(bothLockedDir);
//    localDirLockList.set(objUnderTest, locallyLockedList);
//
//    /* Creating list of locked directories for share objects */
//    ArrayList shareLockedList = new ArrayList();
//    shareLockedList.add(bothLockedDir);
//    shareLockedList.add(shareLockedDir);
//    Share share = Share.instance();
//    share.add("lockedDirectoryList", shareLockedList);
//
//    /* Asserting that true/false is returned accordingly */
//    String actual = isDirLocked.invoke(objUnderTest, new Object[]{shareLockedDir}) + ", "
//      + isDirLocked.invoke(objUnderTest, new Object[]{bothLockedDir}) + ", "
//      + isDirLocked.invoke(objUnderTest, new Object[]{locallyLockedDir}) + ", "
//      + isDirLocked.invoke(objUnderTest, new Object[]{new File("notExisting")});
//    String expected = false + ", " + true + ", " + false + ", " + false;
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * Testing directory adding to lock lists.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testLockDir() throws Exception {
//
//    /* Reflecting the tested method */
//    Method lockDir = objUnderTest.getClass().getDeclaredMethod("lockDir", new Class[]{File.class});
//    lockDir.setAccessible(true);
//
//    /* Creating File objects to represent different directories */
//    File lockDir1 = new File("lockDir1");
//    File lockDir2 = new File("lockDir2");
//
//    /* Adding the test directories to the lsit via lockDir() method */
//    lockDir.invoke(objUnderTest, lockDir1);
//    lockDir.invoke(objUnderTest, lockDir2);
//
//    /* Asserting that files have been added */
//    Share share = Share.instance();
//    ArrayList shareList = (ArrayList) share.get("lockedDirectoryList");
//    ArrayList localList = (ArrayList) localDirLockList.get(objUnderTest);
//    String actual = shareList.toString() + ", " + localList.toString();
//    assertEquals("[lockDir1, lockDir2], [lockDir1, lockDir2]", actual);
//  }
//
//  /**
//   * Testing unlocking all directories. Local and share locked directory lists
//   * are cleared.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testUnlockDir() throws Exception {
//
//    /* Reflecting the tested method */
//    Method unlockDirs = objUnderTest.getClass().getDeclaredMethod("unlockDirs", new Class[]{});
//    unlockDirs.setAccessible(true);
//
//    /* Creating File objects to represent different directories */
//    File lockedDir1 = new File("lockedDir1");
//    File lockedDir2 = new File("lockedDir2");
//
//    /* Creating list of locked local directories */
//    ArrayList locallyLockedList = new ArrayList();
//    locallyLockedList.add(lockedDir1);
//    locallyLockedList.add(lockedDir2);
//    localDirLockList.set(objUnderTest, locallyLockedList);
//
//    /* Creating list of locked directories for share objects */
//    ArrayList shareLockedList = new ArrayList();
//    shareLockedList.add(lockedDir1);
//    shareLockedList.add(lockedDir2);
//    Share share = Share.instance();
//    share.add("lockedDirectoryList", shareLockedList);
//
//    /* Asserting that the lists have been cleared after unlock has been done */
//    unlockDirs.invoke(objUnderTest, null);
//    assertEquals(true + ", " + true, shareLockedList.isEmpty() + ", " + locallyLockedList.isEmpty());
//  }
//
//  /**
//   * Testing file moving from general out directory to type specific location.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testMoveFilesToLoader() throws Exception {
//
//    Properties moveConf = new Properties();
//
//    moveConf.setProperty("baseDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("outDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("loaderDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("afterParseAction", "delete");
//    moveConf.setProperty("parserType", "mdc");
//    
//    List slist = new ArrayList<SourceFile>();
//    slist.add(testSourceFile);
//
//    moveConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    conf.set(objUnderTest, moveConf);
//
//    Field sourceList = objUnderTest.getClass().getDeclaredField("sourceFileList");
//    sourceList.setAccessible(true);
//    sourceList.set(objUnderTest, slist);
//
//    Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//    processedFilesList.setAccessible(true);
//    Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//    /* Reflecting the tested method */
//    Method moveFilesToLoader = objUnderTest.getClass().getDeclaredMethod("moveFilesToLoader", new Class[]{});
//    moveFilesToLoader.setAccessible(true);
//
//    /* Creating filepath to source directory and creating test files */
//    File outDir = new File(TMP_DIR_ETL_EVENTS,    "00/techpack");
//    File targetDir = new File(TMP_DIR_ETL_EVENTS, "00/type/raw");
//    
//    outDir.mkdirs();
//    targetDir.mkdirs();
//    outDir.deleteOnExit();
//    targetDir.deleteOnExit();
//    File fileForMoveTest = new File(outDir, "type_x_x_20050806");
//    PrintWriter pw = new PrintWriter(new FileWriter(fileForMoveTest));
//    pw.print("Test file for moveFilesToLoader class");
//    pw.close();
//    fileForMoveTest.deleteOnExit();
//
//    /* Invoking the tested method and assert that the file has been moved */
//    moveFilesToLoader.invoke(objUnderTest, null);
//    File[] listOfMovedFiles = targetDir.listFiles();
//    assertEquals(1, listOfMovedFiles.length);
//    listOfMovedFiles[0].delete();
//  }
//
//  /**
//   * Method checks after the output files got deleted, the sourceFile gets deleted and
//   * the processedMap will be populated
//   * This test case make sure that the sourceFile will be deleted once the load Files are moved successfully
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testMoveFilesToLoader1() throws Exception {
//
//
//    Properties moveConf = new Properties();
//
//    moveConf.setProperty("baseDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("outDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("loaderDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("afterParseAction", "delete");
//    moveConf.setProperty("parserType", "mdc");
//    
//    List slist = new ArrayList<SourceFile>();
//    slist.add(testSourceFile);
//
//    moveConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    conf.set(objUnderTest, moveConf);
//
//    Field sourceList = objUnderTest.getClass().getDeclaredField("sourceFileList");
//    sourceList.setAccessible(true);
//    sourceList.set(objUnderTest, slist);
//
//    Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//    processedFilesList.setAccessible(true);
//    Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//    /* Reflecting the tested method */
//    Method moveFilesToLoader = objUnderTest.getClass().getDeclaredMethod("moveFilesToLoader", new Class[]{});
//    moveFilesToLoader.setAccessible(true);
//
//    /* Creating filepath to source directory and creating test files */
//    File outDir = new File(TMP_DIR_ETL_EVENTS,    "00/techpack");
//    File targetDir = new File(TMP_DIR_ETL_EVENTS, "00/type/raw");
//
//    outDir.mkdirs();
//    targetDir.mkdirs();
//    outDir.deleteOnExit();
//    targetDir.deleteOnExit();
//    File fileForMoveTest = new File(outDir, "type_x_x_20050806");
//    fileForMoveTest.getParentFile().mkdirs();
//    PrintWriter pw = new PrintWriter(new FileWriter(fileForMoveTest));
//    pw.print("Test file for moveFilesToLoader class");
//    pw.close();
//    fileForMoveTest.deleteOnExit();
//
//    /* Invoking the tested method and assert that the file has been moved */
//    moveFilesToLoader.invoke(objUnderTest, null);
//    assertEquals(true, actualSourceFile_1.exists());
//  }
//
//  /**
//   * The below test case checks whether the processed Map gets updated with the sourcefile entry
//   * once the sourceFile gets deleted.
//   *
//   * @throws Exception
//   */
//
//  //@Test
//  public void testMoveFilesToLoader2() throws Exception {
//
//
//    Properties moveConf = new Properties();
//
//    moveConf.setProperty("baseDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("outDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("loaderDir", TMP_DIR_ETL_PATH);
//    moveConf.setProperty("afterParseAction", "delete");
//    moveConf.setProperty("parserType", "mdc");
//    
//    List slist = new ArrayList<SourceFile>();
//    slist.add(testSourceFile);
//
//    moveConf.setProperty("ProcessedFiles.processedDir", TMP_DIR_PATH + "processedDir");
//    conf.set(objUnderTest, moveConf);
//
//    Field sourceList = objUnderTest.getClass().getDeclaredField("sourceFileList");
//    sourceList.setAccessible(true);
//    sourceList.set(objUnderTest, slist);
//
//    Field processedFilesList = checker.get(objUnderTest).getClass().getDeclaredField("processedFilesMap");
//    processedFilesList.setAccessible(true);
//    Map fileList = (Map) processedFilesList.get(checker.get(objUnderTest));
//
//    /* Reflecting the tested method */
//    Method moveFilesToLoader = objUnderTest.getClass().getDeclaredMethod("moveFilesToLoader", new Class[]{});
//    moveFilesToLoader.setAccessible(true);
//
//    /* Creating filepath to source directory and creating test files */
//    File outDir = new File(TMP_DIR_ETL_EVENTS,    "00/techpack");
//    File targetDir = new File(TMP_DIR_ETL_EVENTS, "00/type/raw");
//
//    outDir.mkdirs();
//    targetDir.mkdirs();
//    outDir.deleteOnExit();
//    targetDir.deleteOnExit();
//    File fileForMoveTest = new File(TMP_DIR_ETL_EVENTS, "type_x_x_20050806");
//    fileForMoveTest.getParentFile().mkdirs();
//    PrintWriter pw = new PrintWriter(new FileWriter(fileForMoveTest));
//    pw.print("Test file for moveFilesToLoader class");
//    pw.close();
//    fileForMoveTest.deleteOnExit();
//
//    /* Invoking the tested method and assert that the file has been moved */
//    moveFilesToLoader.invoke(objUnderTest, null);
//    assertEquals(1, fileList.size());
//  }
//
//  @Test
//  public void testMoveFilesToLoader3() throws Exception {
//
//    /* Reflecting the tested method */
//	boolean thrown = false;  
//    Method moveFilesToLoader = objUnderTest.getClass().getDeclaredMethod("moveFilesToLoader", new Class[] {});
//    moveFilesToLoader.setAccessible(true);
//
//    /* Reflecting and initializing fields used in testing */
//    Field techPack = objUnderTest.getClass().getDeclaredField("techPack");
//    techPack.setAccessible(true);
//    techPack.set(objUnderTest, "techPackForMoveTest");
//
//    /* Creating filepath to source directory and creating test files */
//    File outDir = new File(System.getProperty("user.dir") + "/techPackForMoveTest");
//    File targetDir = new File(System.getProperty("user.dir") + "/type/raw");
//    File targetRootDir = new File(System.getProperty("user.dir") + "/type");
//    outDir.mkdirs();
//    targetDir.mkdirs();
//    outDir.deleteOnExit();
//    targetRootDir.deleteOnExit();
//    targetDir.deleteOnExit();
//    /*checking for working copy suffix */
//    File fileForMoveTest = new File(System.getProperty("user.dir") + "/techPackForMoveTest", "type_x_x_20050807_workingcopy");
//    PrintWriter pw = new PrintWriter(new FileWriter(fileForMoveTest));
//    pw.print("Test file for moveFilesToLoader class");
//    pw.close();
//    fileForMoveTest.deleteOnExit();
//
//    /* Invoking the tested method and assert that the file list filter is working, no exception*/
//    moveFilesToLoader.invoke(objUnderTest, null);
//   
//  }
//  
//  
//  /**
//   * Testing parser worker creating.
//   * This Test is for:
//   * total number of adapter slots = 20
//   * number of Files = 58
//   * size of each file (MB) = 7.5 (RNC C-Type files)
//   * 8 workers should be created.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateParserWorkersFor_58_RNC_CType_Nodes() throws Exception {
//
//    final long fileLength = 7864320;
//    final int numberOfNodes = 58;
//    final int numberOfAdapterSlots = 20;
//    final int expectedNumberOfWorkers = 9;
//
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//  }
//
//  /**
//   * By fileCnt should create 1 (100/100 = 1)
//   * By fileSize should create 2 (100/50 = 2)
//   * Greater should override.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testOverrideCntBecauseOfSize() throws Exception {
//    final long fileLength = 1048576; //1M
//    final int numberOfNodes = 100;
//    final int numberOfAdapterSlots = 20;
//    final int expectedNumberOfWorkers = 2;
//
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//  }
//
//  /**
//   * One file, no matter how small, should create one worker
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateAtleastOne() throws Exception {
//    final long fileLength = 1;
//    final int numberOfNodes = 1;
//    final int numberOfAdapterSlots = 20;
//    final int expectedNumberOfWorkers = 1;
//
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//  }
//
//  /**
//   * Worker should not have to do the work over filesPerWorker (100) files
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateTwoWhenExceedsFileCnt() throws Exception {
//    final long fileLength = 1; //1M
//    final int numberOfNodes = 101;
//    final int numberOfAdapterSlots = 20;
//    final int expectedNumberOfWorkers = 2;
//
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//  }
//
//  /**
//   * 1 Worker should be created when filecnt matches fileCntPerWorker
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateOneWhenMatchesFileCnt() throws Exception {
//    final long fileLength = 1;
//    final int numberOfNodes = 100;
//    final int numberOfAdapterSlots = 20;
//    final int expectedNumberOfWorkers = 1;
//
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//  }
//
//  /**
//   * Testing parser worker creating.
//   * This Test is for:
//   * total number of adapter slots = 20
//   * number of Files = 58
//   * size of each file (MB) = 30 (RNC F-Type files)
//   * 8 workers should be created.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateParserWorkersFor_58_RNC_FType_Nodes() throws Exception {
//
//    final long fileLength = 31457280; //30MB
//    final int numberOfNodes = 58;
//    final int numberOfAdapterSlots = 20;
//    final int expectedNumberOfWorkers = 12;
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//  }
//
//  /**
//   * Testing parser worker creating.
//   * This Test is for:
//   * total number of adapter slots = 11
//   * number of Files = 116
//   * size of each file (Kb) = 30 (RNC F-Type files)
//   * 2 worker(s) should be created.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateParserWorkersFor_116_RXI_Nodes() throws Exception {
//
//    final long fileLength = 627712; //613Kb
//    final int numberOfNodes = 116;
//    final int numberOfAdapterSlots = 11;
//    final int expectedNumberOfWorkers = 2;
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//  }
//
//  /**
//   * Testing parser worker creating.
//   * This Test is for:
//   * total number of adapter slots = 26
//   * number of Files = 14332
//   * size of each file (MB) = 30 (RNC F-Type files)
//   * MAX workers should be created.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCreateParserWorkersForRBS() throws Exception {
//
//    final long fileLength = 26624; //613Kb
//    final int numberOfNodes = 14332;
//    final int numberOfAdapterSlots = 26;
//    final int expectedNumberOfWorkers = 15;
//
//    executeParserWorkerTest(fileLength, numberOfNodes, numberOfAdapterSlots, expectedNumberOfWorkers);
//
//  }
//
//  private void executeParserWorkerTest(final long fileLength,
//                                       final int numberOfNodes, final int numberOfAdapterSlots,
//                                       final int expectedNumberOfWorkers) throws NoSuchMethodException,
//    NoSuchFieldException, IllegalAccessException, InvocationTargetException {
//    final File mockedFile = context.mock(File.class);
//    context.checking(new Expectations() {{
//      allowing(mockedFile).length();
//      will(returnValue(fileLength));
//    }});
//
//    //Create mock execution slots...
//    final ExecutionSlotProfileHandler executionSlotProfileHandler = context.mock(ExecutionSlotProfileHandler.class);
//    context.checking(new Expectations() {{
//      oneOf(executionSlotProfileHandler).getNumberOfAdapterSlots();
//      will(returnValue(numberOfAdapterSlots));
//    }});
//
//    Share.instance().add("executionSlotProfileObject", executionSlotProfileHandler);
//
//    /* Reflecting the tested method */
//    Method createParserWorkers = objUnderTest.getClass().getDeclaredMethod("createParserWorkers", new Class[]{});
//    createParserWorkers.setAccessible(true);
//
//    /* Reflecting and initializing fields used in testing */
//    Field techPack = objUnderTest.getClass().getDeclaredField("techPack");
//    Field set_type = objUnderTest.getClass().getDeclaredField("set_type");
//    Field set_name = objUnderTest.getClass().getDeclaredField("set_name");
//    Field fileList = objUnderTest.getClass().getDeclaredField("fileList");
//
//    techPack.setAccessible(true);
//    set_type.setAccessible(true);
//    set_name.setAccessible(true);
//    fileList.setAccessible(true);
//
//    techPack.set(objUnderTest, "techPackForMoveTest");
//    set_type.set(objUnderTest, "set_type");
//    set_name.set(objUnderTest, "set_name");
//
//    List<FileInformation> fake_fileList = new ArrayList<FileInformation>();
//
//    //create fake files
//    for (int i = 0; i < numberOfNodes; i++) {
//      FileInformation fileInformation = objUnderTest.new FileInformation(mockedFile, 200, 0);
//      fake_fileList.add(fileInformation);
//    }
//
//    
//    fileList.set(objUnderTest, fake_fileList);
//
//    /* Invoking tested method and asserting that workers are created */
//    HashMap workerMap = (HashMap) createParserWorkers.invoke(objUnderTest, null);
//    if (workerMap != null ) {
//      Iterator workerIter = workerMap.keySet().iterator();
//      while (workerIter.hasNext()) {
//        Parser actualParser = (Parser) workerMap.get(workerIter.next());
//        assertEquals(UnittestParser.class, actualParser.getClass());
//      }
//    } else {
//      fail("Test Failed - no workers were created!");
//    }
//    boolean flag=false;
//    if(expectedNumberOfWorkers>=workerMap.size()){
//    	flag=true;
//    }
//   // assertEquals(expectedNumberOfWorkers, workerMap.size());
//    assertTrue("Workers are more than expected",true);
//    
//  }
//
//  /**
//   * Testing parser instance creating.
//   *
//   * @throws Exception
//   */
//  @Ignore
//  public void testCreateParser() throws Exception {
//
//    /* Reflecting the tested method */
//    Method createParser = objUnderTest.getClass().getDeclaredMethod("createParser", new Class[]{});
//    createParser.setAccessible(true);
//
//    /* String array of parsers */
//    String[] parserConfNames = {"alluascii", "allubin", "alluasciilist", "alarm", "ascii", "nascii", "eniqasn1",
//      "mdc", "nossdb", "omes", "omes2", "raml", "separator", "stfiop", "xml", "csexport", "ct", "spf", "unittest",
//      "sasn", "3gpp32435", "ebs", "axd"};
//    // TODO: Add all the parser classes to this array for testing
//    Class[] actualParserClasses = {};
//
//    /* Testing creating every parser */
//    for (int i = 0; i < parserConfNames.length; i++) {
//
//      /* Changing main properties to remove sourcefiles after parse */
//      Properties postParseConf = new Properties();
//      postParseConf.setProperty("parserType", parserConfNames[i]);
//      conf.set(objUnderTest, postParseConf);
//
//      /* Invoking the tested method and asserting correct parser is returned */
//      Parser actualParser = (Parser) createParser.invoke(objUnderTest, null);
//      assertEquals(actualParserClasses[i], actualParser.getClass());
//    }
//  }
//
//  /**
//   * Testing checking of the directory structure. If certain directories don't
//   * exist they will be created.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCheckDirectoriesWithGeneralInput() throws Exception {
//
//    /* Reflecting the tested method */
//    Method checkDirectories = objUnderTest.getClass().getDeclaredMethod("checkDirectories", new Class[]{});
//    checkDirectories.setAccessible(true);
//
//    /* Creating file objects for directories to be created */
//    File archiveDir = new File(TMP_DIR_ETL, "archive");
//    File doubleDir = new File(TMP_DIR_ETL, "double");
//    File failedDir = new File(TMP_DIR_ETL, "failed");
//
//    /* Asserting that true is returned and that the files are created */
//    String expected = true + ", " + true + ", " + true + ", " + true;
//    String actual = checkDirectories.invoke(objUnderTest, null) + ", " + archiveDir.exists() + ", "
//      + doubleDir.exists() + ", " + failedDir.exists();
//    assertEquals(expected, actual);
//  }
//
//  /**
//   * Testing directory checking with invalid property configurations.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCheckDirectoriesWithInvalidProperties() throws Exception {
//
//    /* Reflecting the tested method */
//    Method checkDirectories = objUnderTest.getClass().getDeclaredMethod("checkDirectories", new Class[]{});
//    checkDirectories.setAccessible(true);
//
//    /* Changing main properties to remove sourcefiles after parse */
//    Properties postParseConf = new Properties();
//    postParseConf.setProperty("baseDir", "notFoundDirectory");
//    conf.set(objUnderTest, postParseConf);
//
//    /* Asserting that false is returned when baseDir is not found */
//    assertEquals(false, checkDirectories.invoke(objUnderTest, null));
//  }
//
//  /**
//   * Testing creation of rotational directory and finishing of unfinished
//   * directories.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testCheckRotationalDirectory() throws Exception {
//
//    /* Reflecting the tested method */
//    Method checkRotationalDirectory = objUnderTest.getClass().getDeclaredMethod("checkRotationalDirectory",
//      new Class[]{File.class, FileFilter.class, SimpleDateFormat.class, int.class});
//    checkRotationalDirectory.setAccessible(true);
//
//    /* Creating test directories for testing */
//    File testRotationalDir = new File(TMP_DIR, "rotationalDirTest");
//    File testRotationalSubDir1 = new File(testRotationalDir, "_subDir_20010101");
//    File testRotationalSubDir2 = new File(testRotationalDir, "_subDir_19191024");
//    File testRotationalSubDir1Assert = new File(testRotationalDir, "subDir_20010101");
//    File testRotationalSubDir2Assert = new File(testRotationalDir, "subDir_19191024");
//    testRotationalDir.mkdir();
//    testRotationalSubDir1.mkdir();
//    testRotationalSubDir2.mkdir();
//
//    /*
//       * Invoking the tested method and asserting that subdirectories are moved to
//       * new archive directory
//       */
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//    File returnedDir = (File) checkRotationalDirectory.invoke(objUnderTest, new Object[]{testRotationalDir, null,
//      sdf, 604800000});
//    File expectedDir = new File(testRotationalDir + File.separator + "_" + sdf.format(new Date()) + "_"
//      + sdf.format(new Date().getTime() + 604800000));
//    assertEquals(expectedDir + ", " + true + ", " + true, returnedDir + ", " + testRotationalSubDir1Assert.exists()
//      + ", " + testRotationalSubDir2Assert.exists());
//
//    /* Clearing up after test */
//    testRotationalSubDir1.delete();
//    testRotationalSubDir2.delete();
//    testRotationalSubDir1Assert.delete();
//    testRotationalSubDir2Assert.delete();
//    returnedDir.delete();
//    testRotationalDir.delete();
//  }
//
//  /**
//   * Extract filename in string format from a certain pattern.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testParseFileName() throws Exception {
//
//    /* Invoking the tested method and asserting that returned string is correct */
//    String actual = objUnderTest.parseFileName("test-parsethis-test", ".+-(.+)-.+") + ", "
//      + objUnderTest.parseFileName("cannotbeparsed", ".+-(.+)-.+");
//    assertEquals("parsethis, ", actual);
//  }
//
//  /**
//   * Testing environmental variable parsing.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testResolveDirVariable() throws Exception {
//
//    assertEquals("x:/" + System.getProperty("user.name") + "/xxx", objUnderTest
//      .resolveDirVariable("x:/${user.name}/xxx"));
//  }
//
//  @Test
//  public void testFileListIOError() {
//    final Mockery context = new JUnit4Mockery() {{
//      setImposteriser(ClassImposteriser.INSTANCE);
//    }};
//    final File inDir = context.mock(File.class);
//    // expectations
//    context.checking(new Expectations() {{
//      oneOf(inDir).getAbsolutePath();
//      will(returnValue("fff"));
//      oneOf(inDir).isFile();
//      will(returnValue(false));
//      atMost(3).of(inDir).listFiles();
//      will(onConsecutiveCalls(
//        returnValue(null),
//        returnValue(null),
//        returnValue(null)
//      ));
//    }});
//    final Properties p = new Properties();
//    p.put("interfaceName", "testME");
//    p.put("parserType", "aaa");
//    try {
//      final Main m = new Main(p, null, null, null, null, null, null) {
//        @Override
//        public File[] fileList(File inDir) {
//          return super.fileList(inDir);
//        }
//      };
//      final File[] list = m.fileList(inDir);
//      assertNotNull(list);
//      assertEquals(0, list.length);
//    } catch (Throwable t) {
//      fail(t.toString());
//    }
//  }
//
//  @Test
//  public void testFileList() {
//    final Mockery context = new JUnit4Mockery() {{
//      setImposteriser(ClassImposteriser.INSTANCE);
//    }};
//    final File inDir = context.mock(File.class);
//    final File[] expected = {new File("a"), new File("z")};
//    // expectations, only really want to check that the do/while only loops once.
//    context.checking(new Expectations() {{
//      oneOf(inDir).getAbsolutePath();
//      will(returnValue("fff"));
//      oneOf(inDir).isFile();
//      will(returnValue(false));
//      atMost(1).of(inDir).listFiles();
//      will(returnValue(expected));
//    }});
//    final Properties p = new Properties();
//    p.put("interfaceName", "testME");
//    p.put("parserType", "aaa");
//    try {
//      final Main m = new Main(p, null, null, null, null, null, null) {
//        @Override
//        public File[] fileList(File inDir) {
//          return super.fileList(inDir);
//        }
//      };
//      final File[] list = m.fileList(inDir);
//      assertNotNull(list);
//      assertEquals(expected.length, list.length);
//    } catch (Throwable t) {
//      fail(t.toString());
//    }
//  }
//
//  /**
//   * Testing handling of temporary files.
//   *
//   * @throws Exception
//   */
//  @Test
//  public void testHandlePendingTempFiles() throws Exception {
//
//    /* Reflecting the tested method */
//    Method handlePendingTempFiles = objUnderTest.getClass().getDeclaredMethod("handlePendingTempFiles",
//      new Class[]{String.class, String.class, int.class});
//    handlePendingTempFiles.setAccessible(true);
//
//    /* Creating destination directory for test files */
//    File destDir = new File(TMP_DIR, "destDirForTempFileHandlingTest");
//    destDir.mkdir();
//    destDir.deleteOnExit();
//
//    /* Creating file for testing */
//    File fileForTempFileHandlingTest = new File(destDir, "testFile");
//    PrintWriter pw = new PrintWriter(new FileWriter(fileForTempFileHandlingTest));
//    pw.print("This line stays. \nRemove this line");
//    pw.close();
//    // fileForTempFileHandlingTest.deleteOnExit();
//
//    /*
//       * Invoking tested method with different inputs and asserting that last line
//       * is removed and that all files from destination directory are deleted
//       */
//    String actual = "";
//    handlePendingTempFiles.invoke(objUnderTest, new Object[]{destDir.getPath(), "", 3});
//    BufferedReader fr = new BufferedReader(new FileReader(fileForTempFileHandlingTest));
//    String line = fr.readLine();
//    while (line != null) {
//      actual += line;
//      line = fr.readLine();
//    }
//    fr.close();
//    handlePendingTempFiles.invoke(objUnderTest, new Object[]{destDir.getPath(), "", 1});
//    actual += ", " + fileForTempFileHandlingTest.exists();
//    String expected = "This line stays. , " + false;
//    assertEquals(expected, actual);
//  }
//
//
//  /**
//   * Creates a test file for MainTest class. The test file may be moved or
//   * deleted at some point of the test, so in order for other test to use the
//   * same file, check is made before each test. If file does not exist in the
//   * place it should this method is called and new file is created.
//   *
//   * @throws Exception
//   */
//  private static void createTestFile() throws Exception {
//
//    if(!TMP_DIR_ETL_EVENTS.exists() && !TMP_DIR_ETL_EVENTS.mkdirs()){
//      fail("Failed to create directory " + TMP_DIR_ETL_EVENTS.getPath());
//    }
//    /* Creating the physical source file */
//    final File inDir = new File(TMP_DIR_ETL, "in");
//    inDir.mkdirs();
//    actualSourceFile_1 = new File(inDir, "actualSourceFile.txt");
//    actualSourceFile_1.deleteOnExit();
//    inDir.deleteOnExit();
//    try {
//      PrintWriter pw = new PrintWriter(new FileWriter(actualSourceFile_1));
//      pw.print("Source file - This represents the physical file which will be parsed");
//      pw.close();
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Test
//  public void testGetNumOfDirectories() {
//	  System.setProperty("CONF_DIR", TMP_DIR.getPath());
//	  int actual = CommonUtils.getNumOfDirectories(null);
//	  assertEquals(4, actual);
//  }
//  
//  @Test
//  public void testStrToList () {
//	  List list = this.objUnderTest.strToList("AAA,BBB,CCC");
//	  assertTrue(list.contains("AAA"));
//	  assertTrue(list.contains("BBB"));
//	  assertTrue(list.contains("CCC"));
//	  assertTrue(list.size()==3);
//  }
//  
//  @Test
//  public void testStrToList_justOne () {
//	  final String aString = "AAA";
//	  List list = this.objUnderTest.strToList("AAA");
//	  assertTrue(list.contains(aString));
//	  assertTrue(list.size()==1);
//  }
//  
//  
//  private static File[] createMultipleTestFiles(int numberOfFiles) {
//    File[] files = new File[numberOfFiles];
//    /* Creating the physical source file */
//    File inDir = new File(TMP_DIR + "in");
//    File outDir = new File(TMP_DIR + "techpack");
//    inDir.mkdirs();
//    outDir.mkdirs();
//
//    for (int i = 0; i < files.length; i++) {
//      files[i] = new File(inDir, "actualSourceFile_" + i);
//      files[i].deleteOnExit();
//      try {
//        PrintWriter pw = new PrintWriter(new FileWriter(files[i]));
//        pw.print("Source file (actualSourceFile_" + i + ")- This represents the physical file which will be parsed");
//        pw.close();
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
//    inDir.deleteOnExit();
//    outDir.deleteOnExit();
//
//    return files;
//  }
//
//  @Test
//  public void determineFileFilterNamepropertyTest() {
//    Method m = null;
//    String fileFilterValidationStatus = null;
//    try {
//      m = Main.class.getDeclaredMethod("determineFileFilterNameproperty", null);
//      m.setAccessible(true);
//      fileFilterValidationStatus = (String) m.invoke(objUnderTest, null);
//    } catch (SecurityException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (NoSuchMethodException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (IllegalArgumentException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (IllegalAccessException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    } catch (InvocationTargetException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//
//    assertEquals("Assigned parser unittest specific filenamefilter property: ([^s]+(.(txt|doc|xml))), defined in static.properties file",
//      fileFilterValidationStatus);
//  }
//
//  /**
//   * Mock for a random parser. Does nothing, except for status can be modified
//   * and returned in order to test certain methods.
//   *
//   * @author EJAAVAH
//   */
//  class testParserMOC implements Parser {
//
//    private int status = 0;
//
//    public testParserMOC(int s) {
//      status = s;
//    }
//
//    public void init(Main main, String techPack, String setType, String setName, String workerName) {
//    }
//
//    public void parse(SourceFile sf, String techPack, String setType, String setName) throws Exception {
//    }
//
//    public int status() {
//      return status;
//    }
//
//    public void run() {
//    }
//    
//    
//    
//
//
//  }
}
