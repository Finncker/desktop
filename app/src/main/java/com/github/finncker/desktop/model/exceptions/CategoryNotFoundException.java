package com.github.finncker.desktop.model.exceptions;

public class CategoryNotFoundException extends Exception {
    
    public CategoryNotFoundException (String message) {
        super("ERRO: A categoria \"" + message + "\" de transação não foi encontrada.");
    }
}
