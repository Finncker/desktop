package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.User;

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
        return readAll().stream()
                        .filter(u -> u.getEmail().equalsIgnoreCase(email))
                        .findFirst()
                        .orElse(null);
    }
}
