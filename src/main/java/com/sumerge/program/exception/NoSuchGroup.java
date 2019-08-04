package com.sumerge.program.exception;

import java.io.Serializable;

public class NoSuchGroup extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    public NoSuchGroup() {
        super();
    }
    public NoSuchGroup(String msg)   {
        super(msg);
    }
    public NoSuchGroup(String msg, Exception e)  {
        super(msg, e);
    }
}
