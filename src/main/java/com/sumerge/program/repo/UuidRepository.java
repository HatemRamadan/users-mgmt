package com.sumerge.program.repo;

import com.sumerge.program.entity.User;
import com.sumerge.program.entity.Uuid;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Stateless
public class UuidRepository {

    @PersistenceContext
    private EntityManager em;

    public String getUUID(String username) throws Exception{
        User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
                .setParameter("username", username).getSingleResult();
        if(user==null)
            throw new Exception("user does not exist");
        Uuid uuid = createUUID(username);
        em.persist(uuid);
        return uuid.getUuid();
    }
    public boolean validateUuid(String username,String uuid) throws Exception{
        Uuid actualUUID = (Uuid) em.createQuery("SELECT u FROM Uuid u where u.username = :username")
                .setParameter("username", username).getSingleResult();
        if(actualUUID==null)
            throw new Exception("user does not exist");
        if(actualUUID.getUuid().equals(uuid))
            return true;
        else
            return false;
    }
    private Uuid createUUID(String username){
        UUID uuid = UUID.randomUUID();
        return(new Uuid(username,uuid.toString()));
    }
}
