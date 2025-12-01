package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.Account;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.debug("Buscando conta pelo nome: {}", name);
        return readAll().stream()
                        .filter(a -> a.getName().equalsIgnoreCase(name))
                        .findFirst()
                        .orElse(null);
    }
}
