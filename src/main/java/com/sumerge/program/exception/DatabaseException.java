package com.sumerge.program.exception;

import java.io.Serializable;

public class DatabaseException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public DatabaseException() {
        super();
    }
    public DatabaseException(String msg)   {
        super(msg);
    }
    public DatabaseException(String msg, Exception e)  {
        super(msg, e);
    }
}
