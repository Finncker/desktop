package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.repository.UserRepository;

public class UserService {

    private UserRepository userRepo = new UserRepository();

    public User create(User user) {
        User existing = userRepo.readByEmail(user.getEmail());

        if (existing != null) {
            // CRIAR ESSA EXCEPTION PERSONALIZADA
            throw new IllegalArgumentException("Email já cadastrado!");
        }

        return userRepo.create(user);
    }

    public User read(String id) {
        User u = userRepo.read(id);

        if (u == null) {
            // CRIAR AQUI A EXCEPTION DE UserNotFoundException e chamar.
            // Deixar a mensagem na classe personalizada.
            // throw new UserNotFoundException("Usuário não encontrado: " + id);
        }

        return u;
    }

    public User readByEmail(String email) {
        User u = userRepo.readByEmail(email);

        if (u == null) {
            // CRIAR AQUI A EXCEPTION DE UserNotFoundException e chamar.
            // Deixar a mensagem na classe personalizada.
            // throw new UserNotFoundException("Email não encontrado: " + email);
        }

        return u;
    }

    public User update(User user) {
        return userRepo.update(user);
    }

    public boolean delete(String id) {
        boolean deleted = userRepo.delete(id);
        
        if (!deleted) {
            // CRIAR AQUI A EXCEPTION DE UserNotFoundException e chamar.
            // Deixar a mensagem na classe personalizada.
            // throw new UserNotFoundException("Usuário não encontrado");
        }

        return true;
    }

}
