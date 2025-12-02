package com.github.finncker.desktop.service;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.exceptions.TransactionNotFoundException;
import com.github.finncker.desktop.model.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionService {

  private TransactionRepository transactionRepository = new TransactionRepository();

  public void create(UUID accountUUID, Transaction transaction) {
    log.info("Criando transação: id = {}", transaction.getUuid());

    transactionRepository.create(accountUUID, transaction);
  }

  public Transaction read(UUID accountUUID, UUID uuid) throws TransactionNotFoundException {
    log.info("Buscando transação id = {}", uuid);

    Transaction transaction = transactionRepository.read(accountUUID, uuid);

    return transaction;
  }

  public void update(UUID accountUUID, Transaction transaction) throws TransactionNotFoundException {
    log.info("Atualizando transação id = {}", transaction.getUuid());

    transactionRepository.update(accountUUID, transaction);
  }

  public void delete(UUID accountUUID, UUID uuid) {
    log.info("Tentando deletar transação id = {}", uuid);

    transactionRepository.delete(accountUUID, uuid);
  }

  public java.util.List<Transaction> getAll() {
    return transactionRepository.getAll();
  }
}
