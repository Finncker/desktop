package com.github.finncker.desktop.model.exceptions;

public class UserNotFoundIdException extends Exception {
    
    public UserNotFoundIdException(String id) {
        super("Nenhum usuário encontrado com o id: " + id);
    }

}
