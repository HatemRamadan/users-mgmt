//import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
//import com.sumerge.program.MyJsonObject;
//import com.sumerge.program.entity.Group;
//import com.sumerge.program.entity.User;
//import org.glassfish.jersey.client.ClientProperties;
//import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
//import org.junit.*;
//import org.junit.runners.MethodSorters;
//
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.util.List;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class TestingClassIT {
//    static String username;
//    static HttpAuthenticationFeature adminCred;
//    static int groupID;
//
//    @BeforeClass
//    public static void init(){
//        username = (int)((Math.random() * 9000000)+1000000)+"";
//        groupID = (int)((Math.random() * 9000000)+1000000);
//        adminCred = HttpAuthenticationFeature.basic("admin", "admin");
//    }
//
//    @Test
//    public void test00getAllUsers(){
//        HttpAuthenticationFeature userCred = HttpAuthenticationFeature.basic("ahmed", "ahmed");
//        Client client= ClientBuilder.newClient();
//        List<User> usersAsAdmin = client.target("http://localhost:8880/").register(adminCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .get(List.class);
//        List<User> usersAsUser = client.target("http://localhost:8880/").register(userCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .get(List.class);
//
//        Assert.assertTrue(usersAsAdmin.size()>0);
//        Assert.assertTrue(usersAsUser.size()>0);
//        Assert.assertFalse(usersAsAdmin.equals(usersAsUser));
//        Assert.assertTrue(usersAsAdmin.size()>=usersAsUser.size());
//    }
//
//    @Test
//    public void test01AddUser(){
//        //Create User
//        User user = new User();
//        user.setUsername(username);
//        user.setName("test");
//        user.setPassword("9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"); // password => test
//        user.setDeleted(false);
//        user.setRole("user");
//
//        Client client= ClientBuilder.newClient();
//
//        Response response = client.target("http://localhost:8880/user").register(adminCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(user, MediaType.APPLICATION_JSON));
//
//        User user2 = client.target("http://localhost:8880/user/"+user.getUsername()).register(adminCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .get(User.class);
//        Assert.assertTrue(user.getUsername().equals(user2.getUsername()));
//    }
//
//    @Test
//    public void test02deleteUser(){
//        Client client= ClientBuilder.newClient();
//        Response response = client.target("http://localhost:8880/user/"+username).register(adminCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .delete();
//        User user2 = client.target("http://localhost:8880/user/"+username).register(adminCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .get(User.class);
//        Assert.assertTrue(user2.isDeleted()==true);
//    }
//
//    @Test
//    public void test03undoDeleteUser(){
//        Client client= ClientBuilder.newClient();
//        client.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
//        Response response = client.target("http://localhost:8880/user/"+username).register(adminCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .put(null);
//
//        User user2 = client.target("http://localhost:8880/user/"+username).register(adminCred).register(JacksonJsonProvider.class)
//                .request(MediaType.APPLICATION_JSON)
//                .get(User.class);
//        Assert.assertTrue(user2.isDeleted()==false);
//    }
//
////    @Test
////    public void test04addGroup(){
////        Group group = new Group();
////        group.setName("Groupp");
////        group.setDescription("Desc");
////        group.setGroupid(groupID);
////
////        Client client= ClientBuilder.newClient();
////        Response response = client.target("http://localhost:8880/group").register(adminCred).register(JacksonJsonProvider.class)
////                .request(MediaType.APPLICATION_JSON)
////                .post(Entity.entity(group,MediaType.APPLICATION_JSON));
////
////    }
//}