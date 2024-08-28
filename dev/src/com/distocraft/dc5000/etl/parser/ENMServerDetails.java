package com.distocraft.dc5000.etl.parser;

/*
 * POJO for EnmServer Details.
 * 
 */

public class ENMServerDetails {

	private String ip;
	private String host;
	private String type;
	private String username;
	private String password;
	private String hostname;
	//cENM
	private String enmType;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getEnmType() {
		return enmType;
	}

	public void setEnmType(String enmType) {
		this.enmType = enmType;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}

