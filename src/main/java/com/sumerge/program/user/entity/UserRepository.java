package com.sumerge.program.user.entity;

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

}
