package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.Account;

public class AccountRepository extends AbstractRepository<Account> {

    public AccountRepository() {
        super("accounts.dat");
    }

    @Override
    protected String getId(Account entity) {
        return entity.getId();
    }

    @Override
    protected boolean matchId(Account entity, String id) {
        return entity.getId().equals(id);
    }

    public Account readByName(String name) {
        return readAll().stream()
                        .filter(a -> a.getName().equalsIgnoreCase(name))
                        .findFirst()
                        .orElse(null);
    }
}
