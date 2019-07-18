package com.sumerge.program.rest;

import com.sumerge.program.user.entity.User;
import com.sumerge.program.user.entity.UserRepository;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

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

    @GET
    @Produces(APPLICATION_JSON)
    public Response get() {
		LOGGER.info("Entering get with user " + securityContext.getUserPrincipal().toString());
		try {
			return Response.ok().
					entity(repo.getAllUsers()).
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response post(User user) {
        LOGGER.info("Entering post with user " + securityContext.getUserPrincipal().toString());
		try {
			return Response.ok().
					entity(repo.addUser(user)).
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
    }
}
