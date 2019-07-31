package com.sumerge.program.rest;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.sumerge.program.MyJsonObject;
import com.sumerge.program.group.entity.Group;
import com.sumerge.program.user.entity.User;
import com.sumerge.program.user.entity.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
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
	public Response get() {
		try {
			if(securityContext.isUserInRole("user"))
			{
			return Response.ok().
					entity(repo.getUsersNames()).
					build();
			}
			return Response.ok().
					entity(repo.getAllUsers()).
					build();

		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
	}

	@PUT
	@Path("update/{username}")
	public Response updateUser(@PathParam("username") String username, MyJsonObject jsonObject){
		boolean isAdmin = securityContext.isUserInRole("admin");
		String mUsername = securityContext.getUserPrincipal().toString();
	    if(!isAdmin && !mUsername.equals(username))
			return Response.status(Response.Status.FORBIDDEN).build();
		try {
			if(jsonObject.getNewName()!=null)
				repo.updateName(username,jsonObject.getNewName(),mUsername);
			else if(jsonObject.getNewUsername()!=null)
				repo.updateUsername(username,jsonObject.getNewUsername(),mUsername);
			else if(jsonObject.getNewPassword()!=null)
				repo.resetPassword(username,jsonObject.getOldPassword(),jsonObject.getNewPassword(),mUsername);
			return Response.ok().
					entity("Updated").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e.getMessage()+"	"+e).
					build();
		}
	}

	@PUT
	@Path("move")
	public Response moveUser( MyJsonObject jsonObject){
		try {
			String mUsername = securityContext.getUserPrincipal().toString();
			if(jsonObject.getNewGroupID()!=-1&&jsonObject.getOldGroupID()!=-1)
				repo.moveUser(jsonObject.getUsername(),jsonObject.getOldGroupID(),jsonObject.getNewGroupID(),mUsername);
			else if(jsonObject.getNewGroupID()!=-1)
				repo.addUserToGroup(jsonObject.getUsername(),jsonObject.getNewGroupID(),mUsername);
			else if(jsonObject.getOldGroupID()!=-1)
				repo.removeUserFromGroup(jsonObject.getUsername(),jsonObject.getOldGroupID(),mUsername);
			return Response.ok().
					entity("SUCCESS").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e.getMessage()+"	"+e).
					build();
		}
	}


	@POST
	@Path("user")
	public Response addUser(User user){
		try {
			repo.addUser(user,securityContext.getUserPrincipal().toString());
			return Response.ok().
					entity("Added").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
	}

	@POST
	@Path("group")
	public Response addGroup(Group group){
		try {
			repo.addGroup(group,securityContext.getUserPrincipal().toString());
			return Response.ok().
					entity("Added").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
	}


	@DELETE
	@Path("user/{username}")
	public Response deleteUser(@PathParam("username") String username) {

		try {
			repo.deleteUser(username,securityContext.getUserPrincipal().toString());
			return Response.ok().
					entity("Deleted").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}

	}

	@DELETE
	@Path("group/{id}")
	public Response deleteGroup(@PathParam("id") int id) {

		try {
			repo.deleteGroup(id,securityContext.getUserPrincipal().toString());
			return Response.ok().
					entity("Deleted").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e.getMessage()).
					build();
		}

	}


}