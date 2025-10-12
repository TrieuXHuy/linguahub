package com.huy.linguahub.controller.error;

public class ResourceAlreadyExistsException extends Exception{
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
