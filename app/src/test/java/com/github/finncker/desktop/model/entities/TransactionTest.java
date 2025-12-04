package com.github.finncker.desktop.model.entities;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void shouldCreateTransaction() {
        Transaction transaction = Transaction.builder()
                .categoryUUID(UUID.randomUUID())
                .amount(new BigDecimal("50"))
                .description("Almoço")
                .build();

        assertThat(transaction.getUuid()).isNotNull();
        assertThat(transaction.getDate()).isNotNull();
    }
}
