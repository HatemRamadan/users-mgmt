package com.sumerge.program.exception;

import java.io.Serializable;

public class AuditLogException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public AuditLogException() {
        super();
    }
    public AuditLogException(String msg)   {
        super(msg);
    }
    public AuditLogException(String msg, Exception e)  {
        super(msg, e);
    }
}
