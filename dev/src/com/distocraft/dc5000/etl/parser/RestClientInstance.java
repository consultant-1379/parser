package com.distocraft.dc5000.etl.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import ssc.rockfactory.RockFactory;

import com.distocraft.dc5000.etl.parserlink.StaticProperties;
import com.distocraft.dc5000.etl.rock.Meta_databases;
import com.ericsson.eniq.common.RemoteExecutor;
import com.ericsson.eniq.repository.DBUsersGet;
import com.jcraft.jsch.JSchException;

/**
 * RestClientInstance creates a Client instance and open the ENM session for
 * getting FLS Responses
 * 
 * @author zraosud
 * 
 */
public class RestClientInstance { 

	private Client client;
	private String host = null;
	private Boolean sessionCheck = false;
	private String userName = null;
	private String passWord = null;

	Logger log = null;
	RockFactory dwhdb;
	private PoolingHttpClientConnectionManager clientConnectionManager;
	private static int timeOut = 20000; // Value in milliseconds for Timeout

	private Map<String, NewCookie> sessionCookies;

	public RestClientInstance() {
		log.finest("Called RestClientInstance argless cnstrctr");
	}

	public RestClientInstance(CloudSourceFile task1) {
		//
	}
	public RestClientInstance(Main main) {
		//
	}

	public boolean sessionCheck() {
		return sessionCheck;
	}

	/*
	 * This function makes restClientInstance as null
	 */
	void refreshInstance() {
		this.closeSession();
		if (client != null) {
			client = null;
		}

	}

// For EQEV-66104	
	String del = "/";
	String serverXmlPath = del+"eniq/sw/runtime/tomcat/conf/server.xml";
	
	public String getSecurityProtocol() {
		String securityProtocol = null;
		
		Path serverFilePath = FileSystems.getDefault().getPath(serverXmlPath);
		File serverFile = serverFilePath.toFile();
		try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(
				new FileInputStream(serverFile), StandardCharsets.UTF_8));){
		String line = bufReader.readLine();
			
		while ((line = bufReader.readLine()) != null) {
			if (line.contains("sslEnabledProtocols")) {
				int startIndex = line.indexOf("\"", line.indexOf("sslEnabledProtocols="));
				int endIndex = line.indexOf("\"", startIndex+1);
				if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
					securityProtocol = line.substring(startIndex+1, endIndex);
					break;
				}
			}
		}
		}

		catch (IOException e) {
			log.info("Exception occured in getting the security protocol version:" + e);
		}
		return securityProtocol;
	}

	/**
	 * This function creates the client instance and returns the Client instance.
	 * 
	 * @param cache contains the details of ENMServerDetails class object.
	 * @param log   contains the Logger class instance.
	 * @return Returns the instance of Client registered with session cookies.
	 */
	public Client getClient(final ENMServerDetails cache, final Logger log) {

		if (client == null) {
			try {
				this.log = log;
				host = "https://" + cache.getHost();
				userName = cache.getUsername();
				final String Password_decrypt = cache.getPassword();
				byte[] dec = Base64.getMimeDecoder().decode(Password_decrypt);
				final String PASSWORD1 = new String(dec, StandardCharsets.UTF_8);
				passWord = PASSWORD1.trim();
				log.log(Level.FINEST, () -> "HOST and username:" + host + " " + userName);
				// To validate server CA certificate
				// In order to import server CA certificate
				// keytool -import -file cacert.pem -alias ENM -keystore
				// truststore.ts -storepass secret
				// And give the location of the keystore
				String keyStorePassValue = getValue("keyStorePassValue").trim();
				String securityProtocol = getSecurityProtocol();
				StaticProperties.reload();
				final SslConfigurator sslConfig = SslConfigurator.newInstance()
						.trustStoreFile(StaticProperties.getProperty("TRUSTSTORE_DIR",
								"/eniq/sw/runtime/jdk/jre/lib/security/truststore.ts"))
						.trustStorePassword(keyStorePassValue).securityProtocol(securityProtocol);
				log.log(Level.INFO, () -> "we are using " + securityProtocol + " securityProtocol.");
				final SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
						sslConfig.createSSLContext());
				final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
						.register("http", PlainConnectionSocketFactory.getSocketFactory())
						.register("https", sslSocketFactory).build();
				// Pooling HTTP Client Connection manager.
				// connections will be re-used from the pool
				// also can be used to enable
				// concurrent connections to the server and also
				// to keep a check on the number of connections
				//

				
				clientConnectionManager = new PoolingHttpClientConnectionManager(registry);
                clientConnectionManager.setMaxTotal(100);
                clientConnectionManager.setDefaultMaxPerRoute(50);
                final ClientConfig clientConfig = new ClientConfig();
                clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, clientConnectionManager);
                clientConfig.property(ClientProperties.READ_TIMEOUT, timeOut);
                clientConfig.property(ClientProperties.CONNECT_TIMEOUT, timeOut);

 

                clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, clientConnectionManager);
                clientConfig.property(ApacheClientProperties.DISABLE_COOKIES, false);
                clientConfig.connectorProvider(new ApacheConnectorProvider());
                client = ClientBuilder.newBuilder().withConfig(clientConfig).build();

			} catch (Exception e) {
				log.warning("Exception while creating client instance ::: " + e);
			}
		}
		  if(Boolean.FALSE.equals(sessionCheck) && client != null) {
			getSession(0);
		}
		if (client == null) {
			log.warning("Client object initialisation failed.");
		} else {
			log.finest("client instance near return " + client);
		}

		return client;
	}

	public boolean getSessionCheck() {
		return sessionCheck;
	}

	/**
	 * This function opens the ENM session for sending the REST request.
	 * 
	 * @param cache contains the details of ENMServerDetails class object.
	 * @param log   contains the Logger class instance.
	 * @return Returns the instance of Client registered with session cookies.
	 */
	void getSession(int c) {
		boolean getSessionAgain = false;
		if (c == 3) {
			log.warning("Session creation request sent to server three times but failed to create session");
			return;
		}

		try {
			log.info("login URL :  " + client.target(host).path("login"));
			final WebTarget target = client.target(host).path("login").queryParam("IDToken1", userName)
					.queryParam("IDToken2", passWord);

			final Response response = target.request(MediaType.WILDCARD_TYPE).post(Entity.json(""));

			sessionCookies = new HashMap<>();
			sessionCookies.putAll(response.getCookies());
			List<Cookie> cookieStoreCookies = ApacheConnectorProvider.getCookieStore(client).getCookies();
			log.log(Level.FINE, "Before clearing current cookie information : " + cookieStoreCookies);
			ApacheConnectorProvider.getCookieStore(client).clear();
			log.fine( "sessioncookies copied from login = " + sessionCookies);

			try {
				log.info("Login Response Status :" + response.getStatus() + " \n Login Response Status Information :"
						+ response.getStatusInfo());
				log.info("Login Response Headers :" + response.getHeaders());
				// if the response is client error 401 or any exception that can
				// be
				// successful by retrying,
				// send request for login again
				// and then send the request again for 2 more times
				if (response.getStatus() == 302) {
					sessionCheck = true;
					log.info("Session established...Response code:" + response.getStatus());

				} else {
					getSessionAgain = true;
					log.info("Failed to create session hence will re-try again.");
				}
			} finally { // closing response objects to release the consumed
						// resources
				
					response.close();
			}
		} catch (Exception e) {
			if (e.getCause() instanceof ConnectTimeoutException) {
				log.warning("TIMEOUT Exception while logging in :" + e.getMessage());
			} else {
				log.warning("Exception while logging in :" + e.getMessage());
			}
		}
		
		if(getSessionAgain) {
			getSession(c + 1);
		}

	}

	public String getValue(String command) {
		String output = "";
		try {
			
			String systemCommandString = "";
			final String user = "dcuser";
			final String service_name = "engine";
			List<Meta_databases> mdList = DBUsersGet.getMetaDatabases(user, service_name);
			if (mdList.isEmpty()) {
				mdList = DBUsersGet.getMetaDatabases(user, service_name);				
			}
			if (mdList.isEmpty()) { throw new NoSuchElementException(
					"Could not find an entry for " + user + ":" + service_name + " in engine! (was is added?)");
			}
			final String password = mdList.get(0).getPassword();
			systemCommandString = ". /eniq/home/dcuser; . ~/.profile; " + "cat /eniq/sw/conf/niq.ini |grep -i "
					+ command;
			output = RemoteExecutor.executeComand(user, password, service_name, systemCommandString);

			if (!output.contains("\n")) {
				output = output.substring(output.indexOf("=") + 1);
			} else {
				String[] outputArray = output.split("\n");
				boolean encryptionflag = false;
				for (String str : outputArray) {
					String key = str.substring(0, str.indexOf("=")).trim();
					String value = str.substring(str.indexOf("=") + 1).trim();
					if (key.contains("_Encrypted") && value.equalsIgnoreCase("Y")) {
						encryptionflag = true;
					} else if (key.equalsIgnoreCase(command)) {
						output = value;
					}
				}

				if (encryptionflag) {
					output = new String(Base64.getDecoder().decode(output.trim()), StandardCharsets.UTF_8);
				}
			}

			return output.trim();

		} catch (final JSchException e) {
			log.warning("JschException:" + e);
		} catch (final Exception e) {
			log.warning("Exception:" + e);
		}
		return output.trim();
	}

	public void closeSession() {
		try {
			if (Boolean.TRUE.equals(sessionCheck)) {
				log.finest("Closing the session.");
				log.finest("client instance" + client);

				try {
					int statusCode;
					String response = null;
					Builder builder;
					builder = client.target(host).path("logout").request("application/json");
					Response response3 = setCookies(builder, log).get();
					try {
						statusCode = response3.getStatus();
						response = response3.toString();
						log.fine("log out URL : \n  " + client.target(host).path("logout"));
						log.fine("log out response : " + response3 + " \n Response status :" + response3.getStatus()
								+ " \n Response Headers :" + response3.getHeaders());
					} finally {
						
							response3.close();
					}
					if (statusCode == 200) {
						sessionCheck = false;
						clientConnectionManager.shutdown();
						log.finest("1#Connection manager shut down");
						client.close();
							 client = null;
						 
						log.finest("Successfully logged out");
					} else {
						sessionCheck = true;
						String responseCopy = response;
						log.log(Level.WARNING, () -> "Error in closing the session..session exists and logout response from enm server:"
								+ responseCopy);
					}
				} catch (Exception e) {
					log.log(Level.INFO, "Exception while logging out : ", e);
				}

			} else {
				if (client != null) {
					clientConnectionManager.shutdown();
					log.finest("2#Connection manager shut down");
					client = null;
				}
			}
		} finally {
			sessionCookies.clear();
		}
	}

	/**
	 * This function will logout from the current session and login again if there
	 * is no response for a get query for more than 30 seconds.
	 * 
	 * @param client instance of the Client object
	 */
	void session() {
		log.info(
				"Closing the session as no response recieved for get query for 30 seconds,hence will skip query for remaining nodes");
		closeSession();
	}

	public Builder setCookies(Builder builder, Logger log) {

		sessionCookies.forEach((name, newCookie) -> {
			Date currentDate = new Date();
			if (newCookie.getMaxAge() != 0
					&& (newCookie.getExpiry() == null || !newCookie.getExpiry().before(currentDate))) {
				builder.cookie(newCookie.getName(), newCookie.getValue());
			}
		});
		if (log != null) {
			log.finest("sesioncookies set = " + sessionCookies);
		}

		return builder;
	}

	void clearCookieStore() {
		ApacheConnectorProvider.getCookieStore(client).clear();
	}

}
