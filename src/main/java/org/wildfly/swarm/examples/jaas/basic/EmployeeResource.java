package org.wildfly.swarm.examples.jaas.basic;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * @author Ken Finnigan
 */
@Path("/")
@RequestScoped
public class EmployeeResource {

    @PersistenceContext
    private EntityManager em;

    @Context
    private SecurityContext securityContext;

    @GET
    @Produces("application/json")
    public Employee[] get() {
        return securityContext.isUserInRole("admin")
                ? em.createNamedQuery("Employee.findAll", Employee.class).getResultList().toArray(new Employee[0])
                : new Employee[0];
    }
}
