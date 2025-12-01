package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.exceptions.TransactionNotFoundException;
import com.github.finncker.desktop.model.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionService {

    private TransactionRepository transRepo = new TransactionRepository();

    public Transaction create(Transaction tct) {
        log.info("Criando transação: id = {}", tct.getId());
        return transRepo.create(tct);
    }

    public Transaction read(String id) throws TransactionNotFoundException {
         log.info("Buscando transação id = {}", id);
        Transaction a = transRepo.read(id);

        if (a == null) {
            log.warn("Transação não encontrada: id = {}", id);
            throw new TransactionNotFoundException();
        }

        return a;
    }

    public Transaction update(Transaction tct) {
        log.info("Atualizando transação id = {}", tct.getId());
        return transRepo.update(tct);
    }

    public boolean delete(String id) throws TransactionNotFoundException {
        log.info("Tentando deletar transação id = {}", id);
        boolean deleted = transRepo.delete(id);

        if (!deleted) {
            log.error("Erro ao deletar transação: id = {}", id);
            throw new TransactionNotFoundException();
        }

        log.info("Transação deletada com sucesso: id={}", id);
        return true;
    }
}
