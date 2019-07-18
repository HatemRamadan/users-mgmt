package com.sumerge.program.user.entity;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Ahmed Anwar
 */
@Stateless
public class UserRepository {
	@PersistenceContext
	private EntityManager em;

	public List<User> getAllUsers() {
		return em.createNamedQuery("User.findAll", User.class).getResultList();
	}
}
