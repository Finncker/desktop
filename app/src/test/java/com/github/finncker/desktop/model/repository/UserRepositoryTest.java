package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();
    }

    @Test
    void shouldCreateUser() throws UserNotFoundException {
        User user = User.builder()
                .fullName("João")
                .email("joao@email.com")
                .build();

        repository.createUser(user);

        User saved = repository.readUser();

        assertThat(saved).isNotNull();
        assertThat(saved.getFullName()).isEqualTo("João");
    }

    @Test
    void shouldUpdateUser() throws UserNotFoundException {
        repository.createUser(
                User.builder().fullName("A").email("a@email").build()
        );

        repository.updateUser("B", "b@email");

        User updated = repository.readUser();

        assertThat(updated.getFullName()).isEqualTo("B");
        assertThat(updated.getEmail()).isEqualTo("b@email");
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithoutUser() {
        assertThatThrownBy(() ->
                repository.updateUser("X", "x@email")
        ).isInstanceOf(UserNotFoundException.class);
    }
}
