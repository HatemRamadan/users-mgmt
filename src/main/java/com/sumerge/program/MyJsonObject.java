package com.sumerge.program;

import java.io.Serializable;

public class MyJsonObject implements Serializable {
    private String newUsername;
    private String newName;
    private String oldPassword;
    private String newPassword;
    private int oldGroupID = -1;
    private int newGroupID = -1;
    private String username;

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getOldGroupID() {
        return oldGroupID;
    }

    public void setOldGroupID(int oldGroupID) {
        this.oldGroupID = oldGroupID;
    }

    public int getNewGroupID() {
        return newGroupID;
    }

    public void setNewGroupID(int newGroupID) {
        this.newGroupID = newGroupID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
