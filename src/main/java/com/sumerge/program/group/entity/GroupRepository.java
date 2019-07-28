package com.sumerge.program.group.entity;

import com.sumerge.program.user.entity.UserRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;

@Stateless
public class GroupRepository {
    private static final Logger LOGGER = Logger.getLogger(GroupRepository.class.getName());

    @PersistenceContext
    private EntityManager em;

}
