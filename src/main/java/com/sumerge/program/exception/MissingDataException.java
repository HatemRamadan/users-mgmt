package com.sumerge.program.exception;

import java.io.Serializable;

public class MissingDataException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    public MissingDataException() {
        super();
    }
    public MissingDataException(String msg)   {
        super(msg);
    }
    public MissingDataException(String msg, Exception e)  {
        super(msg, e);
    }
}
