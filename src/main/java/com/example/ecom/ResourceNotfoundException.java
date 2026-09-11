package com.example.ecom;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotfoundException extends RuntimeException {
    public ResourceNotfoundException(String message){
        super(message);
    }
}
