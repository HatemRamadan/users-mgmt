package com.sumerge.program.exception;

import java.io.Serializable;

public class DefaultAdminException  extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public DefaultAdminException() {
        super();
    }
    public DefaultAdminException(String msg)   {
        super(msg);
    }
    public DefaultAdminException(String msg, Exception e)  {
        super(msg, e);
    }
}
