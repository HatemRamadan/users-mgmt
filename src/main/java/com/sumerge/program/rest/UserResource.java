package com.sumerge.program.rest;

import com.sumerge.program.user.entity.User;
import com.sumerge.program.user.entity.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * @author Ahmed Anwar
 */
@Path("/")
@RequestScoped
public class UserResource
{
    private static final Logger LOGGER = Logger.getLogger(UserResource.class.getName());

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserRepository repo;

	@Context
	private HttpServletRequest request;

    @GET
    @Produces(APPLICATION_JSON)

	public Response get() {

		try {
			return Response.ok().
					entity(repo.getUsersNames()).
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}

    }
	@GET
	@Path("delete/{id}")
	@Produces(APPLICATION_JSON)

	public Response delete(@PathParam("id") int id) {

		try {
			repo.delete(id);
			return Response.ok().
					entity("Deleted").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}

	}

	@GET
	@Path("groups")
	@Produces(APPLICATION_JSON)

	public Response getGroups() {

		try {
			return Response.ok().
					entity(repo.getGroups()).
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}

	}

}