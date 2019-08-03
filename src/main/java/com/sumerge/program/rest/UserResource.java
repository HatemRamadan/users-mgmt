package com.sumerge.program.rest;

import com.sumerge.program.EmailRequest;
import com.sumerge.program.MyJsonObject;
import com.sumerge.program.entity.User;
import com.sumerge.program.repo.UserRepository;


import com.sumerge.program.repo.UuidRepository;
import org.apache.log4j.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
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
    private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @Context
    private SecurityContext securityContext;

    @EJB
    private UserRepository userRepository;

    @EJB
	private UuidRepository uuidRepository;

	@Context
	private HttpServletRequest request;

	@GET
	public Response get() {
		logger.debug("Entering Get users endpoint");
		try {
			if(securityContext.isUserInRole("user"))
			{
			return Response.ok().
					entity(userRepository.getUsersNames()).
					build();
			}
			return Response.ok().
					entity(userRepository.getAllUsers()).
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
				userRepository.updateName(username,jsonObject.getNewName(),mUsername);
			else if(jsonObject.getNewUsername()!=null)
				userRepository.updateUsername(username,jsonObject.getNewUsername(),mUsername);
			else if(jsonObject.getNewPassword()!=null)
				userRepository.resetPassword(username,jsonObject.getOldPassword(),jsonObject.getNewPassword(),mUsername);
			return Response.ok().
					entity("Updated").
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
			userRepository.addUser(user,securityContext.getUserPrincipal().toString());
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
			userRepository.deleteUser(username,securityContext.getUserPrincipal().toString());
			return Response.ok().
					entity("Deleted").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
	}

	@PUT
	@Path("user/{username}")
	public Response undoUserDeletion(@PathParam("username") String username) {
		try {
			userRepository.undoUserDeletion(username,securityContext.getUserPrincipal().toString());
			return Response.ok().
					entity("Deletion reverted").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e.getMessage()).
					build();
		}
	}

	@POST
	@Path("forgotPassword")
	public Response forgotPassword(MyJsonObject jsonObject) {
		try {
			String email = userRepository.getEmail(jsonObject.getUsername());
			String uuid = uuidRepository.getUUID(jsonObject.getUsername());
			EmailRequest.sendEmail(jsonObject.getUsername(),email,uuid);
			return Response.ok().
					entity("SENT").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e.getMessage()).
					build();
		}
	}

	@PUT
	@Path("resetPassword/{uuid}")
	public Response resetPassword(@PathParam("uuid") String uuid, MyJsonObject jsonObject) {
		try {
			if(uuidRepository.validateUuid(jsonObject.getUsername(),uuid))
				userRepository.resetForgottenPassword(jsonObject.getUsername(),jsonObject.getNewPassword());
			return Response.ok().
					entity("Password reset").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e.getMessage()).
					build();
		}
	}
}