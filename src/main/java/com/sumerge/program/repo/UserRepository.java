package com.sumerge.program.repo;

import com.sumerge.program.entity.Group;
import com.sumerge.program.entity.Log;
import com.sumerge.program.entity.User;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;



@Stateless
public class UserRepository {

	private static final Logger LOGGER = Logger.getLogger(UserRepository.class.getName());

	@PersistenceContext
	private EntityManager em;

	public List<User> getUsersNames(){
		return em.createQuery("select u.name,u.username from User u where u.deleted=false", User.class).getResultList();
	}

	@Transactional
	public void updateUsername(String oldUsername,String newUsername, String mUsername){
		System.out.println("old:	"+oldUsername);
		System.out.println("new	:"+newUsername);
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", oldUsername).getSingleResult();
		user.setUsername(newUsername);
		em.merge(user);

		//Audit Log
		em.persist(createAuditLog(user,mUsername,"Update Username","Users",user.getUserid()));
	}

	@Transactional
	public void updateName(String username,String newName, String mUsername){
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		user.setName(newName);
		em.merge(user);

		//Audit Log
		em.persist(createAuditLog(user,mUsername,"Update Name","Users",user.getUserid()));
	}

	@Transactional
	public void resetPassword(String username,String oldPassword, String newPassword, String mUsername) throws Exception {
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :value1")
				.setParameter("value1", username).getSingleResult();
		if(hash(oldPassword).equals(user.getPassword())) {
			user.setPassword(hash(newPassword));
			em.merge(user);
		}
		else
			throw new Exception("Password incorrect");

		//Audit Log
		em.persist(createAuditLog(user,mUsername,"Update Password","Users",user.getUserid()));
	}

	//Admin functions
	@Transactional
	public void deleteUser(String username, String mUsername) throws Exception {
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		if(user.getUserid()==1)
			throw new Exception("Default Admin can not be deleted!");
		user.setDeleted(true);
		em.merge(user);

		//Audit Log
		em.persist(createAuditLog(user,mUsername,"Delete user","Users",user.getUserid()));
	}

	@Transactional
	public void undoUserDeletion(String username, String mUsername) throws Exception {
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		if(user==null)
			throw new RuntimeException("user does not exist");
		user.setDeleted(false);
		em.merge(user);

		//Audit Log
		em.persist(createAuditLog(user,mUsername,"undo user deletion","Users",user.getUserid()));
	}
	@Transactional
	public void addUser(User user,String mUsername) throws Exception{
		user.setPassword(hash(user.getPassword()));
		em.persist(user);

		//Audit Log
		em.persist(createAuditLog(user,mUsername,"Add user","Users",user.getUserid()));
	}

	public List<User> getAllUsers(){
		return em.createQuery("select u from User u",User.class).getResultList();
	}

	public String getEmail(String username){
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		if(user==null)
			throw new RuntimeException("user does not exist");
		return user.getEmail();
	}

	@Transactional
	public void resetForgottenPassword(String username, String newPassword) throws Exception{
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :value1")
				.setParameter("value1", username).getSingleResult();
		user.setPassword(hash(newPassword));
		em.merge(user);

		//Audit Log
		em.persist(createAuditLog(user,username,"Update Password","Users",user.getUserid()));
	}
	protected static Log createAuditLog(Object entity, String author, String name, String table,int id){
		ObjectMapper objectMapper = new ObjectMapper();

		Date date= new Date();
		long time = date.getTime();
		Log auditLog = null;
		try {
			auditLog = new Log(objectMapper.writeValueAsString(entity),name, new Timestamp(time),author,id,table);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return auditLog;
	}

	private static String hash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md5 = MessageDigest.getInstance("SHA-256");
		byte[] digest = md5.digest(input.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < digest.length; ++i) {
			sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	public List<Group> getGroups(){
		return em.createQuery("select g from Group g", Group.class).getResultList();
	}

}