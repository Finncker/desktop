package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRepository extends AbstractRepository<User> {
    
    public UserRepository() {
        super("users.dat");
    }

    @Override
    protected String getId(User entity) {
        return entity.getId();
    }

    @Override
    protected boolean matchId(User entity, String id) {
        return entity.getId().equals(id);
    }

    public User readByEmail(String email) {
        log.debug("Buscando usuário pelo email: {}", email);
        return readAll().stream()
                        .filter(u -> u.getEmail().equalsIgnoreCase(email))
                        .findFirst()
                        .orElse(null);
    }
}
