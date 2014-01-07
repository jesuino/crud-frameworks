package org.jugvale.crudframeworks.client.controller;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import javax.management.RuntimeErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.util.GenericType;
import org.jugvale.crudframeworks.client.business.Framework;

/**
 * REST Client for the Framework business class
 * 
 * @author william
 * 
 */
public class FrameworkClientService {

	private final String BASE_URI;

	public FrameworkClientService() {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(getClass().getResource(
					"/conf/crudframeworks.properties").getFile()));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("ERROR LOADING crudframeworks.properties file");
		}
		BASE_URI = p.getProperty("host");
	}

	public void add(Framework framework) {
		doRequest(createRequest().body(MediaType.APPLICATION_JSON, framework), HttpPost.METHOD_NAME, null);
	}

	public Framework get(int id) {
		return (Framework) doRequest(createRequest(id), HttpGet.METHOD_NAME,
				new GenericType<Framework>() {
				});
	}

	public void remove(int id) {
		doRequest(createRequest(id), HttpDelete.METHOD_NAME, null);
	}

	public void update(Framework framework) {
		doRequest(createRequest(framework.getId()).body(
				MediaType.APPLICATION_JSON, framework), HttpPut.METHOD_NAME, null);
	}

	@SuppressWarnings("unchecked")
	public List<Framework> getAll() {
		return (List<Framework>) doRequest(createRequest(), HttpGet.METHOD_NAME,
				new GenericType<List<Framework>>() {
				});
	}

	private Object doRequest(ClientRequest cr, String httpMethod,
			GenericType<?> returnType) {
		ClientResponse<?> r = null;
		Object serverResponseBody = null;
		try {
			System.out.println("PERFORM A "+ httpMethod + " ON " + cr.getUri());
			r = cr.httpMethod(httpMethod);
			// ??
			int status = r.getStatus();
			if ( status >= 500) {
				handleError("Server is having a bad time with this request, try again later...");
			} else if (status == 404) {
				handleError("Framework with ID "
						+ cr.getPathParameters().get("id") + " not found.");
				// if it's not a success code
			}else if (r.getStatus() >= 300){
				handleError("The server responded with an unexpected status: "+ r.getStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
			handleError("Unknown error: " + e.getMessage());
		}
		if (returnType != null)
			serverResponseBody = r.getEntity(returnType);
		cr.clear();
		return serverResponseBody;
	}

	private ClientRequest createRequest(int id) {
		return new ClientRequest(UriBuilder.fromPath(BASE_URI).path(String.valueOf(id)).build().toString());
	}
	private ClientRequest createRequest() {
			return new ClientRequest(BASE_URI);
	}

	private void handleError(String message) {
		throw new RuntimeErrorException(new Error(message));
	}
}