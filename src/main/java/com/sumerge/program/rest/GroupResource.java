package com.sumerge.program.rest;

import com.sumerge.program.MyJsonObject;
import com.sumerge.program.entity.Group;
import com.sumerge.program.repo.GroupRepository;

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


    @POST
    @Path("group")
    public Response addGroup(Group group){
        try {
            groupRepository.addGroup(group,securityContext.getUserPrincipal().toString());
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
    @Path("group/{id}")
    public Response deleteGroup(@PathParam("id") int id) {

        try {
            groupRepository.deleteGroup(id,securityContext.getUserPrincipal().toString());
            return Response.ok().
                    entity("Deleted").
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e.getMessage()).
                    build();
        }

    }

    @PUT
    @Path("move")
    public Response moveUser( MyJsonObject jsonObject){
        try {
            String mUsername = securityContext.getUserPrincipal().toString();
            if(jsonObject.getNewGroupID()!=-1&&jsonObject.getOldGroupID()!=-1)
                groupRepository.moveUser(jsonObject.getUsername(),jsonObject.getOldGroupID(),jsonObject.getNewGroupID(),mUsername);
            else if(jsonObject.getNewGroupID()!=-1)
                groupRepository.addUserToGroup(jsonObject.getUsername(),jsonObject.getNewGroupID(),mUsername);
            else if(jsonObject.getOldGroupID()!=-1)
                groupRepository.removeUserFromGroup(jsonObject.getUsername(),jsonObject.getOldGroupID(),mUsername);
            return Response.ok().
                    entity("SUCCESS").
                    build();
        } catch (Exception e) {
            return Response.serverError().
                    entity(e.getMessage()+"	"+e).
                    build();
        }
    }

}
