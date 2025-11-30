package com.github.finncker.desktop.model.exceptions;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException (String name) {
        super("ERRO: A conta de \"" + name + "\" não foi encontrada.");
    }
    
}
