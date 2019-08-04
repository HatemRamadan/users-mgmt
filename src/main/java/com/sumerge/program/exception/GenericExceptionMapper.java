package com.sumerge.program.exception;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.json.JSONException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.UnsupportedEncodingException;
import java.lang.Throwable;
import java.security.NoSuchAlgorithmException;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable throwable) {
        if(throwable instanceof MissingDataException){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(throwable.getMessage())
                    .build();
        } else if(throwable instanceof NoSuchAlgorithmException || throwable instanceof UnsupportedEncodingException){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(throwable.getMessage())
                    .build();

        } else if(throwable instanceof NoSuchUser)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(throwable.getMessage())
                    .build();

        else if(throwable instanceof IncorrectPassword)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(throwable.getMessage())
                    .build();

        else if(throwable instanceof AuditLogException)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(throwable.getMessage())
                    .build();

        else if(throwable instanceof DefaultAdminException)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(throwable.getMessage())
                    .build();

        else if(throwable instanceof DatabaseException)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(throwable.getMessage())
                    .build();

        else if(throwable instanceof MailjetException || throwable instanceof JSONException||throwable instanceof MailjetSocketTimeoutException)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(throwable.getMessage())
                    .build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(throwable.getMessage())
                .build();
    }
}
