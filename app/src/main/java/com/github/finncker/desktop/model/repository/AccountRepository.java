package com.github.finncker.desktop.model.repository;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountRepository extends AbstractRepository {
    public AccountRepository() {
        super();
    }

    public void create(Account account) throws UserNotFoundException {
        User user = getUser();
        user.getAccounts().add(account);
        setUser(user);
    }

    public Account read(UUID uuid) throws UserNotFoundException {
        User user = getUser();

        for (Account account : user.getAccounts()) {
            if (account.getUuid().equals(uuid)) {
                return account;
            }
        }

        return null;
    }

    public void update(Account account) throws UserNotFoundException {
        User user = getUser();

        for (int i = 0; i < user.getAccounts().size(); i++) {
            if (user.getAccounts().get(i).equals(account)) {
                user.getAccounts().set(i, account);
                setUser(user);
            }
        }

    }

    public void delete(UUID uuid) throws UserNotFoundException {
        User user = getUser();

        for (int i = 0; i < user.getAccounts().size(); i++) {
            if (user.getAccounts().get(i).getUuid().equals(uuid)) {
                user.getAccounts().remove(i);
                setUser(user);
            }
        }

    }
}
