package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.*;
import com.github.finncker.desktop.model.exceptions.TransactionNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class TransactionRepositoryTest {

    private TransactionRepository repository;
    private UUID accountUUID;
    private UUID transactionUUID;

    @BeforeEach
    void setUp() {
        repository = new TransactionRepository();

        accountUUID = UUID.randomUUID();
        transactionUUID = UUID.randomUUID();

        Account account = new Account(
                accountUUID,
                "Conta Teste",
                "Tipo",
                "Banco",
                "123",
                "#FFF",
                BigDecimal.valueOf(1000),
                new ArrayList<>()
        );

        User user = User.builder()
                .fullName("Usuário")
                .email("user@email.com")
                .accounts(new ArrayList<>())
                .build();

        // adiciona a conta ao usuário
        user.getAccounts().add(account);

        // cria o usuário usando o método correto create(...)
        new UserRepository().createUser(user);
    }

    @Test
    void shouldCreateAndReadTransaction() throws TransactionNotFoundException {
        Transaction transaction = new Transaction(
                transactionUUID,
                UUID.randomUUID(),
                BigDecimal.valueOf(200),
                "Compra",
                null
        );

        repository.create(accountUUID, transaction);

        Transaction saved = repository.read(accountUUID, transactionUUID);

        assertThat(saved.getDescription()).isEqualTo("Compra");
        assertThat(saved.getAmount()).isEqualByComparingTo("200");
    }

    @Test
    void shouldUpdateTransaction() throws TransactionNotFoundException {
        Transaction original = new Transaction(
                transactionUUID,
                UUID.randomUUID(),
                BigDecimal.valueOf(100),
                "Inicial",
                null
        );

        repository.create(accountUUID, original);

        Transaction updated = new Transaction(
                transactionUUID,
                UUID.randomUUID(),
                BigDecimal.valueOf(300),
                "Atualizada",
                null
        );

        repository.update(accountUUID, updated);

        Transaction result = repository.read(accountUUID, transactionUUID);

        assertThat(result.getAmount()).isEqualByComparingTo("300");
        assertThat(result.getDescription()).isEqualTo("Atualizada");
    }

    @Test
    void shouldDeleteTransaction() {
        Transaction transaction = new Transaction(
                transactionUUID,
                UUID.randomUUID(),
                BigDecimal.TEN,
                "Teste",
                null
        );

        repository.create(accountUUID, transaction);
        repository.delete(accountUUID, transactionUUID);

        assertThatThrownBy(() ->
                repository.read(accountUUID, transactionUUID))
                .isInstanceOf(TransactionNotFoundException.class);
    }

    @Test
    void shouldReturnAllTransactions() {
        // setUp criou usuário+conta, mas não adicionou transações => deve estar vazio
        assertThat(repository.getAll()).isEmpty();
    }
}
