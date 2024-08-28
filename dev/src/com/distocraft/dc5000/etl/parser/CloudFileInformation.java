package com.distocraft.dc5000.etl.parser;

public class CloudFileInformation {
	  private int fileSize;
	  private String nodeType;
	  private String bucketName;
	  private String objectPath;
	  private String fileName;
	  
	public CloudFileInformation() {
		super();
	}
	
	String getFileName() {
		return fileName;
	}

	void setFileName(String fileName) {
		this.fileName = fileName;
	}

	

	int getFileSize() {
		return fileSize;
	}

	void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	String getNodeType() {
		return nodeType;
	}

	void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	String getBucketName() {
		return bucketName;
	}

	void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	String getObjectPath() {
		return objectPath;
	}

	void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
	}

	@Override
	public String toString() {
		return "CloudFileInformation [fileSize=" + fileSize + ", nodeType=" + nodeType + ", bucketName=" + bucketName
				+ ", objectPath=" + objectPath + ", fileName=" + fileName + "]";
	}
}
