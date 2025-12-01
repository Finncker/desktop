package com.github.finncker.desktop.service;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.exceptions.AccountNotFoundException;
import com.github.finncker.desktop.model.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountService {

    private AccountRepository accountRepository = new AccountRepository();

    public void create(Account account) {
        log.info("Criando conta: {}", account.getName());

        accountRepository.create(account);

        log.info("Conta criada com sucesso.");
    }

    public Account read(UUID uuid) throws AccountNotFoundException {
        log.info("Buscando conta com id = {}", uuid);

        Account account = accountRepository.read(uuid);

        return account;
    }

    public void update(Account account) throws AccountNotFoundException {
        log.info("Atualizado conta: id = {}", account.getUuid());

        accountRepository.update(account);
    }

    public void delete(UUID uuid) {
        accountRepository.delete(uuid);
        log.info("Conta deletada com sucesso: id = {}", uuid);
    }
}
