package com.github.finncker.desktop.model.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.CategoryNotFoundException;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CategoryRepository extends AbstractRepository {

    public CategoryRepository() {
        super();
    }

    public void create(Category category) {
        try {
            User user = getUser();
            user.getCategories().put(category.getUuid(), category);
            setUser(user);
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao criar categoria, usuário inexistente: {}", unfe);
        }
    }

    public Category read(UUID uuid) throws CategoryNotFoundException {
        Category category = null;

        try {
            User user = getUser();
            category = user.getCategories().get(uuid);
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao criar categoria, usuário inexistente: {}", unfe);
        }

        if (category == null) {
            throw new CategoryNotFoundException(uuid);
        }

        return category;
    }

    public void update(Category category) throws CategoryNotFoundException {
        try {
            User user = getUser();

            if (user.getCategories().containsKey(category.getUuid())) {
                user.getCategories().put(category.getUuid(), category);
                setUser(user);
            } else {
                throw new CategoryNotFoundException(category.getUuid());
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao atualizar categoria, usuário inexistente: {}", unfe);
        }
    }

    public void delete(UUID uuid) {
        try {
            User user = getUser();

            if (user.getCategories().containsKey(uuid)) {
                user.getCategories().remove(uuid);
                setUser(user);
            }
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao deletar categoria, usuário inexistente: {}", unfe);
        }
    }

    public List<Category> getAll() {
        try {
            return new ArrayList<>(getUser().getCategories().values());
        } catch (UserNotFoundException unfe) {
            log.error("Erro ao listar categorias, usuário inexistente: {}", unfe);
        }

        return new ArrayList<>();
    }
}
