package com.sumerge.program.uuid;

import javax.persistence.*;

@Entity
@Table(name = "UUID")
public class Uuid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String uuid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public Uuid(){

    }

    public Uuid(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
    }
}
