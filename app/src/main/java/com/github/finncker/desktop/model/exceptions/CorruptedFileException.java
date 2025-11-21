package com.github.finncker.desktop.model.exceptions;

public class CorruptedFileException extends Exception {
    
    public CorruptedFileException (String message) {
        super("Erro: O arquivo \"" + message + "\" está corrompido.");
    }
}
