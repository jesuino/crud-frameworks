package org.jugvale.crudframeworks.client.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.management.RuntimeErrorException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

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
	// TODO: Make this configurable
	private final String BASE_URI = "http://localhost:8080/crud-frameworks-rest/rs/frameworks/";

	ClientRequest clientRequestWithId;
	ClientRequest clientRequest;

	@PostConstruct
	public void init() {
		clientRequestWithId = new ClientRequest(BASE_URI + "{id}");
		clientRequest = new ClientRequest(BASE_URI);
	}

	public void add(Framework framework) {
		clientRequest.body(MediaType.APPLICATION_JSON, framework);
		doRequest(clientRequest, HttpPost.METHOD_NAME, null);
	}

	public Framework get(int id) {
		clientRequestWithId.pathParameter("id", id);
		return (Framework) doRequest(clientRequestWithId, HttpGet.METHOD_NAME,
				new GenericType<Framework>() {
				});
	}

	public void remove(int id) {
		clientRequestWithId.pathParameter("id", id);
		doRequest(clientRequestWithId, HttpDelete.METHOD_NAME, null);
	}

	public void update(Framework framework) {
		clientRequestWithId.pathParameter("id", framework.getId()).body(
				MediaType.APPLICATION_JSON, framework);
		doRequest(clientRequestWithId, HttpPut.METHOD_NAME, null);
	}

	@SuppressWarnings("unchecked")
	public List<Framework> getAll() {
		return (List<Framework>) doRequest(clientRequest, HttpGet.METHOD_NAME,
				new GenericType<List<Framework>>() {
				});
	}

	private Object doRequest(ClientRequest cr, String httpMethod,
			GenericType<?> returnType) {
		ClientResponse<?> r = null;
		Object serverResponseBody = null;
		try {
			r = cr.httpMethod(httpMethod);
			if (r.getStatus() >= Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
				handleError("Server is having a bad time with this request, try again later...");
			} else if (r.getStatus() == Status.NOT_FOUND.getStatusCode()) {
				handleError("Framework with ID "
						+ cr.getPathParameters().get("id") + " not found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			handleError("Unknown error: " + e.getMessage());
		}
		if (returnType != null)
			serverResponseBody = r.getEntity(returnType);
		cr.clear();
		r.releaseConnection();
		return serverResponseBody;
	}

	private void handleError(String message) {
		throw new RuntimeErrorException(new Error(message));
	}
}
