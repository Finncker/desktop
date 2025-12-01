package com.github.finncker.desktop.model.repository;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.TransactionNotFoundException;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionRepository extends AbstractRepository {

    public TransactionRepository() {
        super();
    }

    public void create(UUID accountUUID, Transaction transaction) {
        try {
            User user = getUser();

            for (Account account : user.getAccounts()) {
                if (account.getUuid().equals(accountUUID)) {
                    account.getTransactions().add(transaction);
                    setUser(user);
                }
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao criar transação, usuário inexistente: {}", unfe);
        }
    }

    public Transaction read(UUID accountUUID, UUID TransactionUUID) throws TransactionNotFoundException {
        try {
            User user = getUser();

            for (Account account : user.getAccounts()) {
                if (account.getUuid().equals(accountUUID)) {
                    for (Transaction transaction : account.getTransactions()) {
                        if (transaction.getUuid().equals(TransactionUUID)) {
                            return transaction;
                        }
                    }
                    break;
                }
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao ler transação, usuário inexistente: {}", unfe);
        }

        throw new TransactionNotFoundException();
    }

    public void update(UUID accountUUID, Transaction transaction) throws TransactionNotFoundException {
        try {
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
                    break;
                }
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao atualizar transação, usuário inexistente: {}", unfe);
        }

        throw new TransactionNotFoundException();
    }

    public void delete(UUID accountUUID, UUID transactionUUID) {
        try {
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
                    break;
                }
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao deletar transação, usuário inexistente: {}", unfe);
        }
    }
}
