package com.sumerge.program.user.entity;

import com.sumerge.program.group.entity.Group;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;

import static java.util.logging.Level.SEVERE;


@Stateless
public class UserRepository {

	private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

	@PersistenceContext
	private EntityManager em;

	public List<User> getUsersNames(){
		return em.createQuery("select u.name,u.username from User u where u.deleted=false",User.class).getResultList();
	}

	public void updateUsername(String oldUsername,String newUsername){
		System.out.println("old:	"+oldUsername);
		System.out.println("new	:"+newUsername);
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", oldUsername).getSingleResult();
		user.setUsername(newUsername);
		em.merge(user);
	}

	public void updateName(String username,String newName){
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		user.setName(newName);
		em.merge(user);
	}

	public void resetPassword(String username,String oldPassword, String newPassword) throws Exception {
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :value1")
				.setParameter("value1", username).getSingleResult();
		if(oldPassword.equals(user.getPassword())) {
			user.setPassword(newPassword);
			em.merge(user);
		}
		else
			throw new Exception("Password incorrect");
	}

	//Admin functions
	@Transactional
	public void moveUser(String username, int oldGroupId, int newGroupId) throws RuntimeException {
		removeUserFromGroup(username,oldGroupId);
		addUserToGroup(username,newGroupId);
	}

	public void deleteUser(String username) throws Exception {
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		if(user.getUserid()==1)
			throw new Exception("Default Admin can not be deleted!");
		user.setDeleted(true);
		em.merge(user);
	}

	public void addUser(User user){
		em.persist(user);
	}

	public List<User> getAllUsers(){
		return em.createQuery("select u from User u",User.class).getResultList();
	}

	public void addUserToGroup(String username, int newGroupId){
		Group newGroup = em.find(Group.class,newGroupId);
		if(newGroup==null)
			throw new RuntimeException("Old group does not exist!");
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		List<Group> userGroups = user.getGroups();
		userGroups.add(newGroup);
		user.setGroups(userGroups);
	}
	public void removeUserFromGroup(String username, int oldGroupId) throws RuntimeException{
		Group oldGroup = em.find(Group.class,oldGroupId);
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		if(user.getUserid()==1 && oldGroupId==1)
			throw new RuntimeException("Default Admin cannot be moved from the default group!");
		List<Group> userGroups = user.getGroups();
		userGroups.remove(oldGroup);
		user.setGroups(userGroups);
	}

	public void addGroup(Group group){
		em.persist(group);
	}

	public void deleteGroup(int id) throws Exception {
		Group group = em.find(Group.class,id);
		if(group.getGroupid()==1)
			throw new Exception("Default Group can not be deleted!");
		em.remove(group);
	}

	public List<Group> getGroups(){
		return em.createQuery("select g from Group g", Group.class).getResultList();
	}

}