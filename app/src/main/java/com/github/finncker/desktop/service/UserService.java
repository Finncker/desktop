package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserAlreadyExists;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;
import com.github.finncker.desktop.model.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

    private UserRepository userRepository = new UserRepository();

    public boolean userExists() {
        return userRepository.userExists();
    }

    public void create(String fullName, String email) throws UserAlreadyExists {
        if (userRepository.userExists()) {
            throw new UserAlreadyExists();
        }

        User user = User.builder().fullName(fullName).email(email).build();

        userRepository.createUser(user);

        log.info("Usuário {} criado com sucesso.", user.getEmail());
    }

    public User read() throws UserNotFoundException {
        log.info("Buscando usuário.");

        return userRepository.readUser();
    }

    public void update(String fullName, String email) throws UserNotFoundException {
        userRepository.updateUser(fullName, email);
        log.info("Usuário atualizado com sucesso.");
    }

    public void delete() {
        userRepository.deleteUser();
        log.info("Usuário deletado com sucesso.");
    }
}
