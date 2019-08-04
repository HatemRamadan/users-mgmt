package com.sumerge.program.exception;

import java.io.Serializable;

public class IncorrectPassword extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public IncorrectPassword() {
        super();
    }
    public IncorrectPassword(String msg)   {
        super(msg);
    }
    public IncorrectPassword(String msg, Exception e)  {
        super(msg, e);
    }
}