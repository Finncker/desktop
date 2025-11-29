package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserAlreadyExists;
import com.github.finncker.desktop.model.exceptions.UserNotFoundEmailException;
import com.github.finncker.desktop.model.exceptions.UserNotFoundIdException;
import com.github.finncker.desktop.model.repository.UserRepository;

public class UserService {

    private UserRepository userRepo = new UserRepository();

    public User create(User user) throws UserAlreadyExists {
        User existing = userRepo.readByEmail(user.getEmail());

        if (existing != null) {
            throw new UserAlreadyExists();
        }

        return userRepo.create(user);
    }

    public User read(String id) throws UserNotFoundIdException {
        User u = userRepo.read(id);

        if (u == null) {
            throw new UserNotFoundIdException(id);
        }

        return u;
    }

    public User readByEmail(String email) throws UserNotFoundEmailException {
        User u = userRepo.readByEmail(email);

        if (u == null) {
            throw new UserNotFoundEmailException(email);
        }

        return u;
    }

    public User update(User user) {
        return userRepo.update(user);
    }

    public boolean delete(String id) throws UserNotFoundIdException {
        boolean deleted = userRepo.delete(id);

        if (!deleted) {
            throw new UserNotFoundIdException(id);
        }

        return true;
    }

}
