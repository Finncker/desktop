package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.exceptions.TransactionNotFoundException;
import com.github.finncker.desktop.model.repository.TransactionRepository;

public class TransactionService {

    private TransactionRepository transRepo = new TransactionRepository();

    public Transaction create(Transaction tct) {
        return transRepo.create(tct);
    }

    public Transaction read(String tct) throws TransactionNotFoundException {
        Transaction a = transRepo.read(tct);

        if (a == null) {
            throw new TransactionNotFoundException();
        }

        return a;
    }

    public Transaction update(Transaction tct) {
        return transRepo.update(tct);
    }

    public boolean delete(String tct) throws TransactionNotFoundException {
        boolean deleted = transRepo.delete(tct);

        if (!deleted) {
            throw new TransactionNotFoundException();
        }

        return true;
    }
}
