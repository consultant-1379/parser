/**
 * 
 */
package com.distocraft.dc5000.etl.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;



import ssc.rockfactory.RockFactory;

/**
 * @author zradpra
 *
 */

public class CloudSourceFile extends SourceFile {
	private Date lastDate = new Date();
	private ENMServerDetails eCache = null;
    private CloudFileInformation cloudFileInfo;
    private InputStream fisCloud=null;
    private RestClientInstance restClientInstanceCloud=null; 
    private Client clientCloud=null;  
    private File cENMFile;
	
	public CloudSourceFile(File file, int memoryConsumptionMB, Properties conf, RockFactory rf, ParseSession psession,
			ParserDebugger debugger, String useZip, Logger log,ENMServerDetails eCache,CloudFileInformation cldInfo, Client clientCloud,RestClientInstance restClientInstanceCloud) {
		super(file, memoryConsumptionMB, conf, rf, psession, debugger, useZip, log);
		this.eCache= eCache;
		cloudFileInfo=cldInfo;
		this.clientCloud=clientCloud; 
        this.restClientInstanceCloud=restClientInstanceCloud;
        cENMFile=file;
		
	} 
	public Date getLastDate() {
		return new Date(lastDate.getTime());
	}
	
	public void setLastDate(Date date) {
		this.lastDate = new Date(date.getTime());
	}

	/*
	  * To read the FAN interface file from cloud server
	  */
	    public InputStream readCloudFile() {
	        log.finest("Entered readCloudFile() method");
	        String bucketName=cloudFileInfo.getBucketName();
	        String objectPath=cloudFileInfo.getObjectPath();
	        String fileName=cloudFileInfo.getFileName();
	        String path="file/v1/files/ericsson/";
	        InputStream is=null;
	        try {
	        	if(clientCloud!=null) {
	              String hst="https://"+eCache.getHost();
	              log.log(Level.FINE, () -> "Host is : "+hst);
	              log.finer("Reading reference file data..");
	              Builder builder=clientCloud.target(hst)     
	                      .path(path+bucketName+"/"+objectPath+"/"+fileName)
	                      .request();
	              log.log(Level.FINER, () -> "Full path : "+hst+"/"+path+bucketName+"/"+objectPath+"/"+fileName);


	             Response getResponse=restClientInstanceCloud.setCookies(builder, log).get();
	              log.finest("GET request response status code: "+getResponse.getStatus());
	              log.finest("GET request response status code info: "+getResponse.getStatusInfo());
	              log.finest("Media type: "+getResponse.getMediaType());       
	              if(fisCloud!=null)
	                  log.finest("fisCloudObj is NOT null");
	              if(getResponse.getStatus()==200) {

	                 is=getResponse.readEntity(InputStream.class);



	                if(is!=null) {
	                	
	                	readGZIPCloudFile(fileName, is);

	                 } 
	                else {
	                    log.fine("fisCloud is null");
	                }
	             } 
	             else {
	                 log.warning("Unable read the files, response from server is: "+getResponse.getStatus()+" "+getResponse.getStatusInfo());
	             }
	              if(is!=null)
	              {
	            	  is.close();
	              }	          
		          
	              }else {
	            	  log.log(Level.INFO, () -> "client is null "+fileName);
	              }
	              
	        } catch (Exception e) {
	            log.info("Exception: "+e);
	        } 
	      return fisCloud;
	    }
	    
	  public void readGZIPCloudFile(String fileName, InputStream is) {
		  BufferedReader reader=null;
		  try {
	    	if(fileName.endsWith(".xml.gz")) {
                log.finest("file ends with .xml.gz");
                GZIPInputStream gis = new GZIPInputStream(is);
                reader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
                String data=null;
                
                StringBuilder sbf=new StringBuilder();
                  while((data=reader.readLine())!=null) {
                      
                       sbf.append(data);
                  }   
                  byte[] byts=sbf.toString().getBytes(StandardCharsets.UTF_8);
                  fisCloud=new ByteArrayInputStream(byts);  
                } 
              else {
            	  reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                  String data=null;
                    
                    StringBuilder sbf=new StringBuilder();
                      while((data=reader.readLine())!=null) {
                         
                           sbf.append(data);
                           sbf.append("\n");
                      }  
                      byte[] byts=sbf.toString().getBytes(StandardCharsets.UTF_8);
                      fisCloud=new ByteArrayInputStream(byts);
                }
		  } catch (Exception e) {
			  log.warning("Exception while appending data into fisCloud "+e);
		  }
	    }
	
	/**
	   * Checks that all measurementFiles and sourceFile are closed. If open
	   * measurementFiles or sourceFile are found explicit close is performed.
	   */

	@Override
    void close() {
	    
		  closeMeasurementFiles();
	    
	    if (fisCloud != null) {
	      try {
	    	  fisCloud.close();
	       } catch (Exception e) {
	        log.warning("Error closing writer " + e);
	       } 
	     }
	   }
	 @Override
	 void closeMeasurementFiles() {
		  while (!measurementFiles.isEmpty()) {
		      final MeasurementFile mf = measurementFiles.remove(0);
		      try {
		        if (mf.isOpen()) {
		        	log.log(Level.FINEST, () -> "Found open measurementFile: " + mf);
		          mf.close();
		        }
		      } catch (Exception e) {
		        log.warning("Error closing MeasurementFile " + mf + " : " + e);
		      }
		    } 
	  }
	/**
	   * The way to read parser source file.
	   * 
	   * @return inputStream to file represented by this object
	   */
	 @Override
	  public InputStream getFileInputStream() throws Exception {
		 
		 InputStream fisCloud1= null;

		 fisCloud1=readCloudFile();
		  return fisCloud1; 		 
		  
	  }
	  @Override
	  public long fileSize() {
		    return cloudFileInfo.getFileSize();
		 }
	  @Override
		public String getName() {
			return cloudFileInfo.getFileName();
		}	

		/**
		 * Get the zip file name in which the zip entry is present
		 * 
		 * @return parentName
		 */
		public String getParentName() {
			return super.getName();
		}
		
		/**
		 * Get the size of the zip file in which the zip entry is present
		 * @return
		 */
		public long getParentSize() {
			return super.getSize();
		}

		@Override
		public long getSize() {
			return cloudFileInfo.getFileSize();
		}

		@Override
        public void move(final File tgtDir) throws Exception {
          log.fine("Moving file");


          String fileName = file.getName();
          String targetDir = tgtDir.toString();
          Path tgtPath = FileSystems.getDefault().getPath(targetDir, fileName);

          final File tgt = tgtPath.toFile();



          final boolean success = file.renameTo(tgt);



          if (success) {
            log.finest("The sourcefile was moved successfully: " + file.getName() + " ---> " + tgt.getPath());
            return;
          }
          moveViaCopyDelete(tgt);
          if(file.exists())
          {
          Files.move(file.toPath(), tgt.toPath(), StandardCopyOption.REPLACE_EXISTING);
          if(tgt.exists() && !file.exists())
          {
              log.finest("File "+file.getName()+" moved succesfully via move opearation to "+tgt.getPath());
          }
          else
          {
          log.info("File"+file.getName()+" could not be moved succesfully hence deleting the file");
          delete();
            }     
          }
        } 

      @Override
        public void moveViaCopyDelete(final File tgt) {        

               log.finest("Move via copy delete"+ file.getName()); 
               

              try(final InputStream in = new FileInputStream(cENMFile);
                  final OutputStream out = new FileOutputStream(tgt)) {

              final byte[] buf = new byte[1024];
              int len;
              while ((len = in.read(buf)) > 0) {
                  out.write(buf, 0, len);
            }

              delete();

              log.finer(" File " + file.getName() + " successfully moved via copy & delete to with CENM" + cENMFile.getName());

            }
            catch(Exception ex)
             {
                log.finest("could not move the file "+file.getName()+ " via copy and delete :"+ex.getMessage() );
             }  
            }
}

