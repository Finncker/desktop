package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserRepository extends AbstractRepository {
    public UserRepository() {
        super();
    }

    public boolean userExists() {
        try {
            getUser();
            return true;
        } catch (UserNotFoundException unfe) {
            return false;
        }
    }

    public void createUser(User user) {
        setUser(user);
    }

    public User readUser() throws UserNotFoundException {
        return getUser();
    }

    public void updateUser(String fullName, String email) throws UserNotFoundException {
        User currentUser = getUser();

        User newUser = User.builder().fullName(fullName).email(email)
                .accounts(currentUser.getAccounts()).categories(currentUser.getCategories()).build();

        setUser(newUser);
    }

    public void deleteUser() {
        setUser(null);
    }
}
