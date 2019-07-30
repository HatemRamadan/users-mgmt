package com.sumerge.program.rest;

import com.sumerge.program.group.entity.Group;
import com.sumerge.program.user.entity.User;
import com.sumerge.program.user.entity.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
	@Path("test")
	public Response addUser(User user){
		try {
			repo.addUser(user);
			return Response.ok().
					entity("Added").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
	}

	@GET
	@Path("test2")
	public Response addUserToGroup(){
		try {
			repo.addUserToGroup("user",1);
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
	@Path("test3")
	public Response addGroup(Group group){
		try {
			repo.addGroup(group);
			return Response.ok().
					entity("Added").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
	}
	@GET
	@Path("test4")
	public Response moveFromTo(User user){
		try {
			repo.moveUser("user",1,2);
			return Response.ok().
					entity("Added").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}
	}

    @GET
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
	public Response delete(@PathParam("id") int id) {

		try {
			repo.deleteUser(id);
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
	@Path("editUsername")
	public Response editUsername(JsonObject jsonObject) {
		System.out.println(jsonObject.toString());
		try {
			repo.updateUsername(jsonObject.getString("oldUsername"),jsonObject.getString("newUsername"));
			return Response.ok().
					entity("Updated").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}

	}

	@POST
	@Path("editName")
	public Response editName(String username, String newName) {

		try {
			repo.updateName(username,newName);
			return Response.ok().
					entity("Updated").
					build();
		} catch (Exception e) {
			return Response.serverError().
					entity(e).
					build();
		}

	}

}