package com.github.finncker.desktop.model.exceptions;

public class CreditLimitExceededException extends Exception {
    
    public CreditLimitExceededException () {
        super("ERRO: Limite de crédito excedido");
    }
}
