package com.github.finncker.desktop.model.exceptions;

public class TransactionNotFoundException extends Exception {
    
    public TransactionNotFoundException() {
        super("ERRO: Transação não encontrada.");
    }
}
