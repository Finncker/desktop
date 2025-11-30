package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.exceptions.AccountNotFoundException;
import com.github.finncker.desktop.model.repository.AccountRepository;
import com.github.finncker.desktop.model.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountService {

    private AccountRepository accRepo = new AccountRepository();
    private TransactionRepository transRepo = new TransactionRepository();

    public Account create(Account acc) {
        log.info("Criando conta: {}", acc.getName());
        return accRepo.create(acc);
    }

    public Account read(String id) throws AccountNotFoundException {
        log.info("Buscando conta com id = {}", id);
        Account a = accRepo.read(id);

        if (a == null) {
            log.info("Conta não encontrada: id = {}", id);
            throw new AccountNotFoundException(id);
        }

        return a;
    }

    public Account update(Account acc) {
        log.info("Atualizado conta: id = {}", acc.getId());
        return accRepo.update(acc);
    }

    public boolean delete(String id) throws AccountNotFoundException {
        log.info("Tentando deletar conta: id = {}", id);
        boolean deleted = accRepo.delete(id);

        if (!deleted) {
            log.error("Erro ao deletar conta: id = {}", id);
            throw new AccountNotFoundException(id);
        }

        log.info("Conta deletada com sucesso: id = {}", id);
        return true;
    }

    public double calculateBalance(Account acc) {
        double total = acc.getTransactions()
                          .stream()
                          .mapToDouble(Transaction::getAmount)
                          .sum() + acc.getInitialBalance();
        
        log.debug("Saldo calculado para conta id = {} : R$ {}", acc.getId(), total);

        return total;
    }

    public void addTransaction(Account acc, Transaction t) {
        log.info("Adicionando transação {} à conta id = {}", t.getId(), acc.getId());

        acc.getTransactions().add(t);
        transRepo.create(t);
        accRepo.update(acc);
    }
}
