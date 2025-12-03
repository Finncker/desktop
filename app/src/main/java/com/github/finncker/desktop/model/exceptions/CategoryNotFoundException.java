package com.github.finncker.desktop.model.exceptions;

import java.util.UUID;

public class CategoryNotFoundException extends Exception {

    public CategoryNotFoundException(UUID uuid) {
        super("ERRO: A categoria com id \"" + uuid + "\" de transação não foi encontrada.");
    }
}
