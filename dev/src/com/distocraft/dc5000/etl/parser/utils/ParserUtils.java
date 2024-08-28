package com.distocraft.dc5000.etl.parser.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.distocraft.dc5000.etl.parser.ENMServerDetails;

public class ParserUtils {
	
	private ParserUtils() {
	    throw new IllegalStateException("Utility class");
	  }

	private static Set<String> flsEnabledSet;
	
	private static Map<String, String> enmShortHostNames;
	
	private static Logger log;
	
	//cENM implementation
	
	private static Map<String, ENMServerDetails> enmCache;//NOSONAR
   
	
	static {
		log = Logger.getLogger("etlengine.Engine"); 
			initFlsEnabledCache();	
	}
	
	public static boolean isFlsEnabled(String ossId) {
	
		
		if (flsEnabledSet != null) {
			return flsEnabledSet.contains(ossId);
		} else {
			return false;
		}
		
	}
	
	public static String getEnmShortHostName(String enmAlias) {
		
	
		
		if (enmShortHostNames != null) {
			return enmShortHostNames.get(enmAlias);
		}
		return null;
		
	}
	
	public static Map<String, ENMServerDetails> getCache() {			
	
		
		return new HashMap<>(enmCache);
	}
	
	
	public static void initFlsEnabledCache() {
		String del = "/";
		String flsConfPath = del+"eniq/installation/config/fls_conf";
		File flsConf = new File(flsConfPath);
		if (flsConf.exists()) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(
											new FileInputStream(flsConf), StandardCharsets.UTF_8));){
				flsEnabledSet = new HashSet<>();
				String ossId = null;
				while ((ossId = reader.readLine()) != null) {
					String ossIdCopy = ossId;
					log.log(Level.INFO, () -> "ossId is "+ossIdCopy);
					flsEnabledSet.add(ossId);
				}
				populateShortHostName();
			} catch (FileNotFoundException e) {
				log.info("/eniq/installation/config/fls_conf not found"+e);
			} catch (IOException e) {
				log.warning("unable to read FLS configuration files" +e);
			}
		} else {
			log.info("/eniq/installation/config/fls_conf not found");
		}
	}
	
	private static void populateShortHostName() throws IOException {
		enmCache = new HashMap<>();//NOSONAR
		 Map<String, String> ossRefMapEnm = new HashMap<>();//NOSONAR
		 Map<String, String> ossRefMap = new HashMap<>();//NOSONAR
		enmShortHostNames = new HashMap<>();
		String del = "/";
		String refPath = del+"eniq/sw/conf/.oss_ref_name_file";
		File ref = new File (refPath);
		
		String enmPath = del+"eniq/sw/conf/enmserverdetail";
		File enm = new File (enmPath);
		//cENM - property file for enmType declared.
		File enmTypeFile = new File ("/eniq/connectd/mount_info");//NOSONAR
		if(ref.exists()) {
			log.finest(".oss_ref_name_file file is exists");
		}
		if(enm.exists()) {
			log.finest("enmserverdetail server detail file exists");
		}
		try(BufferedReader inputOSS = new BufferedReader(new InputStreamReader(
				   new FileInputStream(ref), StandardCharsets.UTF_8));
				BufferedReader inputENM = new BufferedReader(new InputStreamReader(
						   new FileInputStream(enm), StandardCharsets.UTF_8));
				){
			String ossRefLine;
			while ((ossRefLine = inputOSS.readLine()) != null) {
				String ossRefLineCopy = ossRefLine;
				log.log(Level.FINEST, () -> "ossRef data is "+ossRefLineCopy);
				String[] odd = ossRefLine.split("\\s+");
				ossRefMap.put(odd[1], odd[0]);
				
				
				if(odd[0] != null && odd[1] != null) {
                    String enmTypeFileAlias = enmTypeFile+"/"+odd[0]+"/enm_type";
                    log.log(Level.INFO, () -> "enm_type file path is : "+enmTypeFile+"/"+odd[0]+"/enm_type");
                   
                    Path enmTypeFileAliasPath = FileSystems.getDefault().getPath(enmTypeFileAlias);
                    File f = enmTypeFileAliasPath.toFile();
                    
                    boolean isEnmTypeFileExists=f.exists();
                    log.log(Level.FINEST, () -> "isEnmTypeFileExists: "+isEnmTypeFileExists);
                    if(isEnmTypeFileExists) {                   
                    	 try (BufferedReader inEnmType = new BufferedReader(new InputStreamReader(
									new FileInputStream(f), StandardCharsets.UTF_8))){
                    	 // added for cENM - extracting the enmType from the property file.
                         String line2;
                      while ((line2 = inEnmType.readLine()) != null) {
                            ossRefMapEnm.put(odd[0], line2);   
                            log.log(Level.FINEST, () -> "ossRefMapEnm =>"+ossRefMapEnm);
                        }
                      }
                    }
                }
			}
			
			initializingEnmServerDetails(inputENM, ossRefMap, ossRefMapEnm);
					
		} catch (FileNotFoundException e) {
			log.info("Not found .oss_ref_name_file or enmserverdetail file "+e);
		}
	}	
	
	private static void initializingEnmServerDetails(BufferedReader inputENM, Map<String, String> ossRefMap,
								Map<String, String> ossRefMapEnm) {
		
		String enmLine;
		String enmShortHostName = null;
		String ossAlias = null;
		String ossIp = null;
		try {
			while ((enmLine = inputENM.readLine()) != null) {
				String [] tokens = enmLine.split("\\s+");
				ossIp = tokens[0];
				if (ossRefMap.containsKey(ossIp)) {
					ossAlias = ossRefMap.get(ossIp);
					if (flsEnabledSet.contains(ossAlias)) {
						enmShortHostName = tokens[1].split("\\.")[0];
						enmShortHostNames.put(ossAlias, enmShortHostName);
					}
				}
				
				//cENM, initialize the enm server details

				String ossId = null;
			    String ip = null;
				ip = tokens[0];
				ossId = ossRefMap.get(ip);
			   String  enmType=null;
			   enmType = ossRefMapEnm.get(ossId);
			    if(enmType==null) {
			   	  enmType="NONcENM"; 
			   	String  enmTypeCopy = enmType;
			   	log.log(Level.FINEST, () -> "enmType is: "+enmTypeCopy);           
			    }
			    String  enmTypeCopy = enmType;
			    log.log(Level.FINEST, () -> "enmType is: "+enmTypeCopy);
						
						if (ossId != null) {
							ENMServerDetails details = new ENMServerDetails();
							details.setIp(tokens[0]);
							details.setHost(tokens[1]);
							details.setType(tokens[2]);
							details.setUsername(tokens[3]);
							details.setPassword(tokens[4]);
							details.setHostname(tokens[5]);
							details.setEnmType(enmType);
							enmCache.put(ossId, details);
							log.log(Level.FINEST, () -> "ENM details: "+details.toString());
						}
						
			}
		} catch (IOException e) {
			log.info("Not found .oss_ref_name_file or enmserverdetail file "+e);
		}
	}
	}
