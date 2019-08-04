package com.sumerge.program.repo;

import com.sumerge.program.entity.Group;
import com.sumerge.program.entity.User;
import com.sumerge.program.exception.*;

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
    public void addGroup(Group group, String mUsername) throws AuditLogException{
        em.persist(group);

        //Audit Log
        em.persist(UserRepository.createAuditLog(group,mUsername,"Add group","Groups",group.getGroupid()));
    }

    @Transactional
    public void deleteGroup(int id,String mUsername) throws AuditLogException,DefaultGroupException {
        Group group = em.find(Group.class,id);
        if(group==null)
            throw new NoSuchGroup("group does not exist");
        if(group.getGroupid()==1)
            throw new DefaultGroupException("Default Group can not be deleted!");
        em.remove(group);

        //Audit Log
        em.persist(UserRepository.createAuditLog(group,mUsername,"Delete Group","Groups",group.getGroupid()));
    }

    @Transactional
    public void moveUser(String username, int oldGroupId, int newGroupId,String mUsername) throws NoSuchGroup,NoSuchUser,DefaultAdminException,AuditLogException {
        removeUserFromGroup(username,oldGroupId,mUsername);
        addUserToGroup(username,newGroupId,mUsername);
    }

    @Transactional
    public void addUserToGroup(String username, int newGroupId,String mUsername) throws NoSuchUser {
        Group newGroup = em.find(Group.class,newGroupId);
        if(newGroup==null)
            throw new NoSuchGroup("Old group does not exist!");
        List<User> users =  em.createQuery("SELECT u FROM User u where u.username = :username")
                .setParameter("username", username).getResultList();
        if(users.size()==0)
            throw new NoSuchUser("user does not exist");
        User user = users.get(0);
        List<Group> userGroups = user.getGroups();
        userGroups.add(newGroup);
        user.setGroups(userGroups);
        //Audit Log
        em.persist(UserRepository.createAuditLog(user,mUsername,"Add User to Group","Users",user.getUserid()));
    }
    @Transactional
    public void removeUserFromGroup(String username, int oldGroupId, String mUsername) throws NoSuchUser,AuditLogException, DefaultAdminException, NoSuchGroup {
        Group oldGroup = em.find(Group.class,oldGroupId);
        if(oldGroup==null)
            throw new NoSuchGroup("group does not exist");

        List<User> users =  em.createQuery("SELECT u FROM User u where u.username = :username")
                .setParameter("username", username).getResultList();
        if(users.size()==0)
            throw new NoSuchUser("user does not exist");
        User user = users.get(0);
        if(user.getUserid()==1 && oldGroupId==1)
            throw new DefaultAdminException("Default Admin cannot be moved from the default group!");
        List<Group> userGroups = user.getGroups();
        userGroups.remove(oldGroup);
        user.setGroups(userGroups);

        //Audit Log
        em.persist(UserRepository.createAuditLog(user,mUsername,"Remove User from Group","Users",user.getUserid()));
    }
    public Group getSpecificGroup(int id)throws NoSuchGroup{
        Group group = em.find(Group.class,id);
        if(group==null)
            throw new NoSuchGroup("group does not exist");
        return group;
    }
    public List<Group> getGroups(){
        return em.createQuery("select g from Group g", Group.class).getResultList();
    }

}
