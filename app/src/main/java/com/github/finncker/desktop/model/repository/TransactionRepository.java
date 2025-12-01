package com.github.finncker.desktop.model.repository;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

public class TransactionRepository extends AbstractRepository {

    public TransactionRepository() {
        super();
    }

    public void create(UUID accountUUID, Transaction transaction) throws UserNotFoundException {
        User user = getUser();

        for (Account account : user.getAccounts()) {
            if (account.getUuid().equals(accountUUID)) {
                account.getTransactions().add(transaction);
                setUser(user);
            }
        }
    }

    public Transaction read(UUID accountUUID, UUID TransactionUUID) throws UserNotFoundException {
        User user = getUser();

        for (Account account : user.getAccounts()) {
            if (account.getUuid().equals(accountUUID)) {
                for (Transaction transaction : account.getTransactions()) {
                    if (transaction.getUuid().equals(TransactionUUID)) {
                        return transaction;
                    }
                }
            }
        }

        return null;
    }

    public void update(UUID accountUUID, Transaction transaction) throws UserNotFoundException {
        User user = getUser();

        for (Account account : user.getAccounts()) {
            if (account.getUuid().equals(accountUUID)) {
                for (int i = 0; i < account.getTransactions().size(); i++) {
                    if (account.getTransactions().get(i).equals(transaction)) {
                        account.getTransactions().set(i, transaction);
                        setUser(user);
                        return;
                    }
                }
            }
        }

    }

    public void delete(UUID accountUUID, UUID transactionUUID) throws UserNotFoundException {
        User user = getUser();

        for (Account account : user.getAccounts()) {
            if (account.getUuid().equals(accountUUID)) {
                for (int i = 0; i < account.getTransactions().size(); i++) {
                    if (account.getTransactions().get(i).getUuid().equals(transactionUUID)) {
                        account.getTransactions().remove(i);
                        setUser(user);
                        return;
                    }
                }
            }
        }
    }
}
