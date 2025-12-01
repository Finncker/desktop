package com.github.finncker.desktop.model.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("User not found.");
    }

}
