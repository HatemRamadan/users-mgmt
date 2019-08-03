package com.sumerge.program.repo;

import com.sumerge.program.entity.Group;
import com.sumerge.program.entity.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class GroupRepository {
    private static final Logger LOGGER = Logger.getLogger(GroupRepository.class.getName());

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void addGroup(Group group, String mUsername){
        em.persist(group);

        //Audit Log
        em.persist(UserRepository.createAuditLog(group,mUsername,"Add group","Groups",group.getGroupid()));
    }

    @Transactional
    public void deleteGroup(int id,String mUsername) throws Exception {
        Group group = em.find(Group.class,id);
        if(group.getGroupid()==1)
            throw new Exception("Default Group can not be deleted!");
        em.remove(group);


        //Audit Log
        em.persist(UserRepository.createAuditLog(group,mUsername,"Delete Group","Groups",group.getGroupid()));
    }

    @Transactional
    public void moveUser(String username, int oldGroupId, int newGroupId,String mUsername) throws RuntimeException {
        removeUserFromGroup(username,oldGroupId,mUsername);
        addUserToGroup(username,newGroupId,mUsername);
    }

    @Transactional
    public void addUserToGroup(String username, int newGroupId,String mUsername){
        Group newGroup = em.find(Group.class,newGroupId);
        if(newGroup==null)
            throw new RuntimeException("Old group does not exist!");
        User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
                .setParameter("username", username).getSingleResult();
        if(user==null)
            throw new RuntimeException("user does not exist");
        List<Group> userGroups = user.getGroups();
        userGroups.add(newGroup);
        user.setGroups(userGroups);
        //Audit Log
        em.persist(UserRepository.createAuditLog(user,mUsername,"Add User to Group","Users",user.getUserid()));
    }
    @Transactional
    public void removeUserFromGroup(String username, int oldGroupId, String mUsername) throws RuntimeException{
        Group oldGroup = em.find(Group.class,oldGroupId);
        User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
                .setParameter("username", username).getSingleResult();
        if(user.getUserid()==1 && oldGroupId==1)
            throw new RuntimeException("Default Admin cannot be moved from the default group!");
        List<Group> userGroups = user.getGroups();
        userGroups.remove(oldGroup);
        user.setGroups(userGroups);
        //Audit Log
        em.persist(UserRepository.createAuditLog(user,mUsername,"Remove User from Group","Users",user.getUserid()));
    }

}
