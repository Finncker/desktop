package com.github.finncker.desktop.model.exceptions;

public class DuplicatedTransactionExcepcion extends Exception {

    public DuplicatedTransactionExcepcion () {
        super("ERRO: Transação duplicada.");
    }
    
}
