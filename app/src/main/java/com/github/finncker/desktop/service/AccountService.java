package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.exceptions.AccountNotFoundException;
import com.github.finncker.desktop.model.repository.AccountRepository;
import com.github.finncker.desktop.model.repository.TransactionRepository;

public class AccountService {

    private AccountRepository accRepo = new AccountRepository();
    private TransactionRepository transRepo = new TransactionRepository();

    public Account create(Account acc) {
        return accRepo.create(acc);
    }

    public Account read(String id) throws AccountNotFoundException {
        Account a = accRepo.read(id);

        if (a == null) {
            throw new AccountNotFoundException(id);
        }

        return a;
    }

    public Account update(Account acc) {
        return accRepo.update(acc);
    }

    public boolean delete(String id) throws AccountNotFoundException {
        boolean deleted = accRepo.delete(id);

        if (!deleted) {
            throw new AccountNotFoundException(id);
        }

        return true;
    }

    public double calculateBalance(Account acc) {
        return acc.getTransactions()
                  .stream()
                  .mapToDouble(Transaction::getAmount)
                  .sum() + acc.getInitialBalance();
    }

    public void addTransaction(Account acc, Transaction t) {
        acc.getTransactions().add(t);
        transRepo.create(t);
        accRepo.update(acc);
    }

}
