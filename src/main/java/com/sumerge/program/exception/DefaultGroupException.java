package com.sumerge.program.exception;

import java.io.Serializable;

public class DefaultGroupException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public DefaultGroupException() {
        super();
    }
    public DefaultGroupException(String msg)   {
        super(msg);
    }
    public DefaultGroupException(String msg, Exception e)  {
        super(msg, e);
    }
}