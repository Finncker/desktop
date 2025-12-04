package com.github.finncker.desktop.model.entities;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void shouldCreateAccountWithBuilder() {
        Account account = Account.builder()
                .name("Conta Corrente")
                .accountType("Corrente")
                .institution("Banco X")
                .accountNumber("123")
                .color("#FFFFFF")
                .initialBalance(new BigDecimal("1000"))
                .build();

        assertThat(account.getUuid()).isNotNull();
        assertThat(account.getTransactions()).isEmpty();
        assertThat(account.getName()).isEqualTo("Conta Corrente");
    }
}
