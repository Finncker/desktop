package com.github.finncker.desktop.model.exceptions;

public class UserAlreadyExists extends Exception {
    
    public UserAlreadyExists() {
        super("O usuário já existe.");
    }

}
