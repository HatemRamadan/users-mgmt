package com.sumerge.program.log;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "AUDITLOG")
public class Log implements Serializable {
    @Id
    private int auditid;
}
