package com.sumerge.program.user.entity;

import com.sumerge.program.group.entity.Group;
import com.sumerge.program.log.Log;
import com.sumerge.program.uuid.Uuid;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
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
	public void moveUser(String username, int oldGroupId, int newGroupId,String mUsername) throws RuntimeException {
		removeUserFromGroup(username,oldGroupId,mUsername);
		addUserToGroup(username,newGroupId,mUsername);
	}
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
		em.persist(createAuditLog(user,mUsername,"Add User to Group","Users",user.getUserid()));
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
		em.persist(createAuditLog(user,mUsername,"Remove User from Group","Users",user.getUserid()));
	}
	@Transactional
	public void addGroup(Group group, String mUsername){
		em.persist(group);

		//Audit Log
		em.persist(createAuditLog(group,mUsername,"Add group","Groups",group.getGroupid()));
	}
	@Transactional
	public void deleteGroup(int id,String mUsername) throws Exception {
		Group group = em.find(Group.class,id);
		if(group.getGroupid()==1)
			throw new Exception("Default Group can not be deleted!");
		em.remove(group);


		//Audit Log
		em.persist(createAuditLog(group,mUsername,"Delete Group","Groups",group.getGroupid()));
	}

	public String getEmail(String username){
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :username")
				.setParameter("username", username).getSingleResult();
		if(user==null)
			throw new RuntimeException("user does not exist");
		return user.getEmail();
	}
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
	@Transactional
	public void resetForgottenPassword(String username, String newPassword) throws Exception{
		User user = (User) em.createQuery("SELECT u FROM User u where u.username = :value1")
				.setParameter("value1", username).getSingleResult();
		user.setPassword(hash(newPassword));
		em.merge(user);

		//Audit Log
		em.persist(createAuditLog(user,username,"Update Password","Users",user.getUserid()));
	}
	private Log createAuditLog(Object entity, String author, String name, String table,int id){
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
	private Uuid createUUID(String username){
		UUID uuid = UUID.randomUUID();
		return(new Uuid(username,uuid.toString()));
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