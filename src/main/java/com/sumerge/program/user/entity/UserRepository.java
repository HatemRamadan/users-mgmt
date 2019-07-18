package com.sumerge.program.user.entity;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.util.logging.Level.SEVERE;

/**
 * @author Ahmed Anwar
 */
@Stateless
public class UserRepository {

	private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

	@PersistenceContext
	private EntityManager em;

	public List<User> getAllUsers() {
		LOGGER.info("Fetching users list");
		try {
			return em.createNamedQuery("User.findAll", User.class).getResultList();
		} catch (Exception e) {
			LOGGER.log(SEVERE, e.getMessage(), e);
			throw e;
		}
	}

	public void addUser(User user) {
		LOGGER.info("Saving new user " + user);
		try {
			em.persist(user);
		} catch (Exception e) {
			LOGGER.log(SEVERE, e.getMessage(), e);
			throw e;
		}
	}
}
