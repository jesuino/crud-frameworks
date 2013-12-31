package org.jugvale.crudframeworks.resource;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import org.jugvale.crudframeworks.model.Framework;
import org.jugvale.crudframeworks.service.FrameworkService;

/**
 * REST Interface
 * 
 * @author william
 * 
 */
@Path(FrameworkResource.BASE_PATH)
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class FrameworkResource {

	static final String BASE_PATH = "frameworks";

	@EJB
	FrameworkService service;

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response save(Framework framework) {
		if (framework.getId() != 0)
			return Response
					.status(Status.BAD_REQUEST)
					.entity("Framework with id \""
							+ framework.getId()
							+ "\". ThIs method isn't used to update resource, use PUT for this.")
					.build();
		service.save(framework);

		// Will also add a header to inform the ID of the newly created
		return Response.created(
				UriBuilder.fromPath(BASE_PATH)
						.path(String.valueOf(framework.getId())).build())
				.build();
	}

	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(@PathParam("id") int id, Framework framework) {
		// Weird, probably trying to update a framework with the content of
		// other framework..
		if (id != framework.getId()) {
			return Response
					.status(Status.CONFLICT)
					.entity("Trying to update framework with id \"" + id
							+ "\", but with body of framework with id \""
							+ framework.getId()
							+ "\". Check your request body and request URI")
					.build();
		}

		checkIfFrameworkExists(id);
		service.update(framework);
		return Response.noContent().build();
	}

	@DELETE
	@Path("{id}")
	public Response remove(@PathParam("id") int id) {
		checkIfFrameworkExists(id);
		service.remove(id);
		return Response.noContent().build();
	}

	@GET
	@Path("{id}")
	public Framework retrieve(@PathParam("id") int id) {
		checkIfFrameworkExists(id);
		return service.retrieve(id);
	}

	@GET
	public List<Framework> retrieveAll() {
		return service.retrieveAll();
	}

	private void checkIfFrameworkExists(int id) {
		if (service.retrieve(id) == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Framework with ID \"" + id + "\" not found.")
					.build());

	}
}