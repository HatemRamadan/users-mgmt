package com.sumerge.program.rest;

import com.sumerge.program.MyJsonObject;
import com.sumerge.program.entity.Group;
import com.sumerge.program.exception.*;
import com.sumerge.program.repo.GroupRepository;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/")
@RequestScoped
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class GroupResource {
    @EJB
    private GroupRepository groupRepository;

    @Context
    private SecurityContext securityContext;

    private static final Logger logger = Logger.getLogger(UserResource.class.getName());

    @POST
    @Path("group")
    public Response addGroup(Group group) throws AuditLogException {
        logger.debug("Entering add group endpoint");
        groupRepository.addGroup(group,securityContext.getUserPrincipal().toString());
        logger.debug("Leaving add group endpoint");
        return Response.ok().
                entity("Added").
                build();
    }

    @DELETE
    @Path("group/{id}")
    public Response deleteGroup(@PathParam("id") int id) throws DefaultGroupException,AuditLogException, NoSuchGroup {
        logger.debug("Entering delete group endpoint");
        groupRepository.deleteGroup(id,securityContext.getUserPrincipal().toString());
        logger.debug("Leaving delete group endpoint");
        return Response.ok().
                entity("Deleted").
                build();
    }

    @PUT
    @Path("move")
    public Response moveUser( MyJsonObject jsonObject) throws DefaultAdminException, AuditLogException, NoSuchGroup, NoSuchUser {
        logger.debug("Entering move user endpoint");

        String mUsername = securityContext.getUserPrincipal().toString();
        if(jsonObject.getNewGroupID()!=-1&&jsonObject.getOldGroupID()!=-1)
            groupRepository.moveUser(jsonObject.getUsername(),jsonObject.getOldGroupID(),jsonObject.getNewGroupID(),mUsername);
        else if(jsonObject.getNewGroupID()!=-1)
            groupRepository.addUserToGroup(jsonObject.getUsername(),jsonObject.getNewGroupID(),mUsername);
        else if(jsonObject.getOldGroupID()!=-1)
            groupRepository.removeUserFromGroup(jsonObject.getUsername(),jsonObject.getOldGroupID(),mUsername);
        logger.debug("Leaving move user endpoint");
        return Response.ok().
                entity("SUCCESS").
                build();
    }

    @GET
    @Path("group/{id}")
    public Response getSpecificGroup(@PathParam("id") int id) throws  NoSuchGroup {
        logger.debug("Entering get specific group");
        Group group = groupRepository.getSpecificGroup(id);
        logger.debug("Leaving get specific group");
        return Response.ok().entity(group).build();
    }
    @GET
    @Path("groups")
    public Response getAllGroups(){
        logger.debug("Entering get specific group");
        return Response.ok().entity(groupRepository.getGroups()).build();
    }
}