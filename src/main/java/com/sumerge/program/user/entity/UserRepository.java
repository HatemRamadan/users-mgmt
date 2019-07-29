package com.sumerge.program.user.entity;

import com.sumerge.program.group.entity.Group;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import static java.util.logging.Level.SEVERE;


@Stateless
public class UserRepository {

	private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

	@PersistenceContext
	private EntityManager em;

	public List<User> getAllUsers(){
		return em.createQuery("SELECT u from User u",User.class).getResultList();
	}
	public List<User> getUsersNames(){
		return em.createQuery("select u.name,u.username from User u where u.deleted=false",User.class).getResultList();
	}
	public void delete(int id){
		User user = em.find(User.class,id);
		System.out.println("before "+user.isDeleted());
		user.setDeleted(true);
		em.merge(user);
		System.out.println("after "+em.find(User.class,id).isDeleted());
	}

	public List<Group> getGroups(){
		return em.createQuery("select g from Group g", Group.class).getResultList();
	}
}
