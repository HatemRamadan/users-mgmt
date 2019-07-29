package com.sumerge.program.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.jaxrs.json.annotation.JSONP;
import com.sumerge.program.group.entity.Group;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "USERS")
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT e FROM User e")
})
@XmlRootElement
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userid;
    private String name;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private boolean deleted;
    @JsonIgnore
    private String role;
    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "GROUPMEMBER",
            joinColumns = @JoinColumn(name = "USERID"),
            inverseJoinColumns = @JoinColumn(name = "GROUPID"))
private List<Group> groups;


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
