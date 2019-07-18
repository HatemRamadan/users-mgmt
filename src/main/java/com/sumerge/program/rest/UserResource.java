package com.sumerge.program.rest;

import com.sumerge.program.user.entity.UserRepository;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

/**
 * @author Ahmed Anwar
 */
@Path("/")
@RequestScoped
public class UserResource
{
    @Context
    private SecurityContext securityContext;

    @EJB
    private UserRepository repo;

    @GET
    @Produces(APPLICATION_JSON)
    public Response get() {
        return securityContext.isUserInRole("admin")
                ? Response.ok().entity(repo.getAllUsers()).build()
                : Response.status(FORBIDDEN).build();
    }
}
