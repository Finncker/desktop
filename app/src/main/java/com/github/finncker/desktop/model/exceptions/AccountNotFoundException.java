package com.github.finncker.desktop.model.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(UUID uuid) {
        super("ERRO: A conta com o id \"" + uuid + "\" não foi encontrada.");
    }
}
