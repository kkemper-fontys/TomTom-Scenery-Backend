package com.tomtomscenery.scenery_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class APIException extends RuntimeException{
     private static final long serialVersionUID = 1L;

     public APIException (String message)
     {
          super(message);
     }
}
