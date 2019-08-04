package com.sumerge.program.rest;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.sumerge.program.EmailRequest;
import com.sumerge.program.MyJsonObject;
import com.sumerge.program.entity.User;
import com.sumerge.program.exception.*;
import com.sumerge.program.repo.UserRepository;


import com.sumerge.program.repo.UuidRepository;
import org.apache.log4j.Logger;
import org.json.JSONException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

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
		} finally {
			logger.debug("Leaving Get users endpoint");
		}
	}

	@PUT
	@Path("update/{username}")
	public Response updateUser(@PathParam("username") String username, MyJsonObject jsonObject) throws NoSuchUser,MissingDataException,NoSuchAlgorithmException,UnsupportedEncodingException, IncorrectPassword {
		logger.debug("Entering update user endpoint");
		boolean isAdmin = securityContext.isUserInRole("admin");
		String mUsername = securityContext.getUserPrincipal().toString();
	    if(!isAdmin && !mUsername.equals(username))
			return Response.status(Response.Status.FORBIDDEN).build();

	    if(jsonObject.getNewName()!=null)
	    	userRepository.updateName(username,jsonObject.getNewName(),mUsername);
	    else if(jsonObject.getNewUsername()!=null)
	    	userRepository.updateUsername(username,jsonObject.getNewUsername(),mUsername);
	    else if(jsonObject.getNewPassword()!=null)
	    	userRepository.resetPassword(username,jsonObject.getOldPassword(),jsonObject.getNewPassword(),mUsername);
	    else
	    	throw new MissingDataException("Missing data");
		logger.debug("Leaving move user endpoint");
	    return Response.ok().
				entity("Updated").
				build();
	}

	@GET
	@Path("user/{username}")
	public Response getSpecificUserByUsername(@PathParam("username") String username) throws NoSuchUser{
		User user = userRepository.getSpecificUser(username);
		return Response.ok().entity(user).build();
	}

	@POST
	@Path("user")
	public Response addUser(User user) throws MissingDataException,UnsupportedEncodingException,NoSuchAlgorithmException, DatabaseException {
		logger.debug("Entering add user endpoint");
		if(user.getPassword()==null||user.getUsername()==null||user.getName()==null)
			throw new MissingDataException("Missing username or Password");
		userRepository.addUser(user,securityContext.getUserPrincipal().toString());
		logger.debug("Leaving add user endpoint");
		return Response.ok().entity("Added").build();
	}

	@DELETE
	@Path("user/{username}")
	public Response deleteUser(@PathParam("username") String username) throws AuditLogException,NoSuchUser, DefaultAdminException {
		logger.debug("Entering delete user endpoint");
		userRepository.deleteUser(username,securityContext.getUserPrincipal().toString());
		logger.debug("Leaving delete user endpoint");
		return Response.ok().
				entity("Deleted").
				build();
	}

	@PUT
	@Path("user/{username}")
	public Response undoUserDeletion(@PathParam("username") String username) throws NoSuchUser {
		logger.debug("Entering undo delete user endpoint");
		userRepository.undoUserDeletion(username,securityContext.getUserPrincipal().toString());
		logger.debug("Leaving undo delete user endpoint");
		return Response.ok().
				entity("Deletion reverted").
				build();
	}

	@POST
	@Path("forgotPassword")
	public Response forgotPassword(MyJsonObject jsonObject) throws NoSuchUser,AuditLogException, MailjetException, JSONException, MailjetSocketTimeoutException {
		logger.debug("Entering forgot password endpoint");
		String email = userRepository.getEmail(jsonObject.getUsername());
		String uuid = uuidRepository.getUUID(jsonObject.getUsername());
		EmailRequest.sendEmail(jsonObject.getUsername(),email,uuid);
		logger.debug("Leaving forgot password endpoint");
		return Response.ok().
				entity("Check your email").
				build();
	}

	@PUT
	@Path("resetPassword/{uuid}")
	public Response resetPassword(@PathParam("uuid") String uuid, MyJsonObject jsonObject) throws NoSuchUser,NoSuchAlgorithmException,UnsupportedEncodingException {
		logger.debug("Entering reset password endpoint");
		if(uuidRepository.validateUuid(jsonObject.getUsername(),uuid))
			userRepository.resetForgottenPassword(jsonObject.getUsername(),jsonObject.getNewPassword());
		logger.debug("Leaving reset password endpoint");
		return Response.ok().
				entity("Password reset").
				build();


	}
}