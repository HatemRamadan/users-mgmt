package com.sumerge.program.group.entity;

import com.sumerge.program.user.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "APPGROUPS")
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int groupid;
private String name;
private String description;
@ManyToMany
@JoinTable(name = "GROUPMEMBER",
        joinColumns = @JoinColumn(name = "GROUPID"),
        inverseJoinColumns = @JoinColumn(name = "USERID"))
private List<User> users;

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
