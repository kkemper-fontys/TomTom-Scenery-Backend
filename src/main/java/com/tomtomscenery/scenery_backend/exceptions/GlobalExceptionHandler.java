package com.tomtomscenery.scenery_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

public class GlobalExceptionHandler
{
     // handle specific exceptions
     @ExceptionHandler(PoiNotFoundException.class)
     public ResponseEntity<?> handleResourceNotFoundException(PoiNotFoundException exception, WebRequest webRequest)
     {
          ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
          return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
     }

     // handle specific exceptions
     @ExceptionHandler(APIException.class)
     public ResponseEntity<?> handleAPIException(APIException exception, WebRequest webRequest)
     {
          ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
          return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
     }

     // handle global exceptions
     @ExceptionHandler(Exception.class)
     public ResponseEntity<?> handleGlobalException(Exception exception, WebRequest webRequest)
     {
          ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
          return new ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
     }
}
