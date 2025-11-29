package com.github.finncker.desktop.model.exceptions;

public class EmailAlreadyRegisteredException extends Exception {
    
    public EmailAlreadyRegisteredException() {
        super("ERRO: E-mail já cadastrado.");
    }

}
