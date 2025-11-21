package com.github.finncker.desktop.model.exceptions;

public class InsufficientBalanceException extends Exception {

    public InsufficientBalanceException (Double saldo) {
        super("ERRO: Saldo insuficiente! Seu saldo: R$ %.2f" + saldo);
    }
    
}
