package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.Account;
import com.github.finncker.desktop.model.entities.Transaction;
import com.github.finncker.desktop.model.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class AccountRepositoryTest {

    private AccountRepository repository;
    private UUID accountUUID;

    @BeforeEach
    void setUp() {
        repository = new AccountRepository();
        accountUUID = UUID.randomUUID();
    }

    @Test
    void shouldCreateAndReadAccount() throws AccountNotFoundException {
        Account account = new Account(
                accountUUID,
                "Carteira",
                "Pessoal",
                "Nubank",
                "12345-6",
                "#FFFFFF",
                BigDecimal.valueOf(100),
                new ArrayList<Transaction>()
        );

        repository.create(account);

        Account saved = repository.read(accountUUID);

        assertThat(saved).isNotNull();
        assertThat(saved.getUuid()).isEqualTo(accountUUID);
        assertThat(saved.getInitialBalance()).isEqualByComparingTo("100");
    }

    @Test
    void shouldUpdateAccount() throws AccountNotFoundException {
        Account original = new Account(
                accountUUID,
                "Carteira",
                "Pessoal",
                "Nubank",
                "12345-6",
                "#FFFFFF",
                BigDecimal.valueOf(100),
                new ArrayList<Transaction>()
        );

        repository.create(original);

        Account atualizado = new Account(
                accountUUID, // MESMO UUID
                "Carteira Atualizada",
                "Pessoal",
                "Inter",
                "99999-9",
                "#000000",
                BigDecimal.valueOf(500),
                new ArrayList<Transaction>()
        );

        repository.update(atualizado);

        Account result = repository.read(accountUUID);

        assertThat(result.getName()).isEqualTo("Carteira Atualizada");
        assertThat(result.getInstitution()).isEqualTo("Inter");
        assertThat(result.getInitialBalance()).isEqualByComparingTo("500");
    }

    @Test
    void shouldDeleteAccount() throws AccountNotFoundException {
        Account account = new Account(
                accountUUID,
                "Conta Teste",
                "Tipo",
                "Banco",
                "0000",
                "#123456",
                BigDecimal.valueOf(50),
                new ArrayList<Transaction>()
        );

        repository.create(account);
        repository.delete(accountUUID);

        assertThatThrownBy(() -> repository.read(accountUUID))
                .isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void shouldReturnEmptyListWhenNoAccounts() {
        assertThat(repository.getAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        assertThatThrownBy(() -> repository.read(UUID.randomUUID()))
                .isInstanceOf(AccountNotFoundException.class);
    }
}
