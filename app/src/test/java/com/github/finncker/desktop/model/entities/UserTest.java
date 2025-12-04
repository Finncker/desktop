package com.github.finncker.desktop.model.entities;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void shouldCreateUser() {
        User user = User.builder()
                .fullName("João")
                .email("joao@email.com")
                .build();

        assertThat(user.getAccounts()).isEmpty();
        assertThat(user.getCategories()).isEmpty();
    }
}
