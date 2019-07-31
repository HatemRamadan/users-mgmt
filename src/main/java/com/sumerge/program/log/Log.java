package com.sumerge.program.log;




import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "AUDITLOG")
public class Log implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int auditid;
    private String entity;
    private String actionname;
    private Timestamp actiontime;
    private String author;
    private int entityid;
    private String tablename;

    public int getAuditid() {
        return auditid;
    }

    public void setAuditid(int auditid) {
        this.auditid = auditid;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getActionname() {
        return actionname;
    }

    public void setActionname(String actionname) {
        this.actionname = actionname;
    }

    public Timestamp getActiontime() {
        return actiontime;
    }

    public void setActiontime(Timestamp actiontime) {
        this.actiontime = actiontime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getEntityid() {
        return entityid;
    }

    public void setEntityid(int entityid) {
        this.entityid = entityid;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public Log(){

    }
    public Log(String entity, String actionname, Timestamp actiontime, String author, int entityid, String tablename) {
        this.entity = entity;
        this.actionname = actionname;
        this.actiontime = actiontime;
        this.author = author;
        this.entityid = entityid;
        this.tablename = tablename;
    }
}
