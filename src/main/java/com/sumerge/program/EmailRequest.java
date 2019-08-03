package com.sumerge.program;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EmailRequest {
    public static void sendEmail(String username,String email, String key)throws MailjetException, MailjetSocketTimeoutException, JSONException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient("d36f29f13e3eefe8c9e87aca533eb194", "d314bfa20d4db2e6222c7f2c82522caa", new ClientOptions("v3.1"));
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                .put(new JSONObject()
                .put(Emailv31.Message.FROM, new JSONObject()
                .put("Email", "hramadan@sumerge.com")
                .put("Name", "Users Management System"))
                .put(Emailv31.Message.TO, new JSONArray()
                .put(new JSONObject()
                .put("Email", email)
                .put("Name", "User")))
                .put(Emailv31.Message.SUBJECT, "Reset Your Password")
                .put(Emailv31.Message.TEXTPART, "Reset Your Password")
                .put(Emailv31.Message.HTMLPART, "<h4>Dear "+username+", follow this link to reset your password <a href='http://localhost:8880/resetPassword/"+key+"'> Reset Password</a>!</h4><br />")
                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        System.out.println(response.getStatus());
        System.out.println(response.getData());

    }
}
