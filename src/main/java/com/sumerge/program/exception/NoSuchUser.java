package com.sumerge.program.exception;

import java.io.Serializable;

public class NoSuchUser extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public NoSuchUser() {
        super();
    }
    public NoSuchUser(String msg)   {
        super(msg);
    }
    public NoSuchUser(String msg, Exception e)  {
        super(msg, e);
    }
}