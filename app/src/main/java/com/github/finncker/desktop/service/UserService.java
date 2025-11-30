package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserAlreadyExists;
import com.github.finncker.desktop.model.exceptions.UserNotFoundEmailException;
import com.github.finncker.desktop.model.exceptions.UserNotFoundIdException;
import com.github.finncker.desktop.model.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

    private UserRepository userRepo = new UserRepository();

    public User create(User user) throws UserAlreadyExists {
        log.info("Criando usuário: {}", user.getEmail());
        User existing = userRepo.readByEmail(user.getEmail());

        if (existing != null) {
            log.warn("Usuário já existe: {}", user.getEmail());
            throw new UserAlreadyExists();
        }

        log.info("Usuário criado com sucesso: {}", user.getEmail());
        return userRepo.create(user);
    }

    public User read(String id) throws UserNotFoundIdException {
        log.info("Buscando usuário: id = {}", id);
        User u = userRepo.read(id);

        if (u == null) {
            log.error("Usuário não encontrado: id = {}", id);
            throw new UserNotFoundIdException(id);
        }

        return u;
    }

    public User readByEmail(String email) throws UserNotFoundEmailException {
        log.info("Buscando usuário email = {}", email);
        User u = userRepo.readByEmail(email);

        if (u == null) {
            log.warn("Usuário não encontrado por email: {}", email);
            throw new UserNotFoundEmailException(email);
        }

        return u;
    }

    public User update(User user) {
        log.info("Atualizando usuário id = {}", user.getId());
        return userRepo.update(user);
    }

    public boolean delete(String id) throws UserNotFoundIdException {
        log.info("Tentando deletar usuário: id = {}", id);
        boolean deleted = userRepo.delete(id);

        if (!deleted) {
            log.error("Erro ao deletar usuário: id = {}", id);
            throw new UserNotFoundIdException(id);
        }

        log.info("Usuário deletado com sucesso: id = {}", id);
        return true;
    }
}
