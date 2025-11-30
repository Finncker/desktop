package com.github.finncker.desktop.model.exceptions;

public class NegativeValueException extends Exception {

    public NegativeValueException (Double valor) {
        super("ERRO: O valor R$ " + valor + " não é aceito, pois é negativo.");
    }
    
}
