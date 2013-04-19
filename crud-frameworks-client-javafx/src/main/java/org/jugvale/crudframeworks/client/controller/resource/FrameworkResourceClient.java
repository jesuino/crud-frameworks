package org.jugvale.crudframeworks.client.controller.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jugvale.crudframeworks.client.business.Framework;

@Path("")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public interface FrameworkResourceClient {

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response save(Framework framework);

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(@PathParam("id") int id, Framework framework);

	@DELETE
	@Path("{id}")
	public Response remove(@PathParam("id") int id);

	@GET
	@Path("{id}")
	public Framework retrieve(@PathParam("id") int id);

	@GET
	public List<Framework> retrieveAll();

}
