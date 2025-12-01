package com.github.finncker.desktop.model.repository;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.AccountNotFoundException;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountRepository extends AbstractRepository {
    public AccountRepository() {
        super();
    }

    public void create(Account account) {
        try {
            User user = getUser();
            user.getAccounts().add(account);
            setUser(user);
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao criar conta, usuário inexistente: {}", unfe);
        }
    }

    public Account read(UUID uuid) throws AccountNotFoundException {
        try {
            User user = getUser();

            for (Account accounts : user.getAccounts()) {
                if (accounts.getUuid().equals(uuid)) {
                    return accounts;
                }
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao ler conta, usuário inexistente: {}", unfe);
        }

        throw new AccountNotFoundException(uuid);

    }

    public void update(Account account) throws AccountNotFoundException {
        try {
            User user = getUser();

            for (int i = 0; i < user.getAccounts().size(); i++) {
                if (user.getAccounts().get(i).equals(account)) {
                    user.getAccounts().set(i, account);
                    setUser(user);
                    return;
                }
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao atualizar conta, usuário inexistente: {}", unfe);
        }

        throw new AccountNotFoundException(account.getUuid());
    }

    public void delete(UUID uuid) {
        try {
            User user = getUser();

            for (int i = 0; i < user.getAccounts().size(); i++) {
                if (user.getAccounts().get(i).getUuid().equals(uuid)) {
                    user.getAccounts().remove(i);
                    setUser(user);
                }
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao deletar conta, usuário inexistente: {}", unfe);
        }
    }
}
