package com.github.finncker.desktop.model.exceptions;

public class UserNotFoundEmailException extends Exception {
    
    public UserNotFoundEmailException(String id) {
        super("Nenhum usuário encontrado com o e-mail: " + id);
    }

}
