package com.github.finncker.desktop.model.repository;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.exceptions.UserNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CategoryRepository extends AbstractRepository {

    public CategoryRepository() {
        super();
    }

    public void create(Category category) throws UserNotFoundException {
        User user = getUser();
        user.getCategories().put(category.getUuid(), category);
        setUser(user);
    }

    public Category read(UUID uuid) throws UserNotFoundException {
        User user = getUser();

        return user.getCategories().get(uuid);
    }

    public void update(Category category) throws UserNotFoundException {
        User user = getUser();

        if (user.getCategories().containsKey(category.getUuid())) {
            user.getCategories().put(category.getUuid(), category);
            setUser(user);
        }
    }

    public void delete(UUID uuid) throws UserNotFoundException {
        User user = getUser();

        if (user.getCategories().containsKey(uuid)) {
            user.getCategories().remove(uuid);
            setUser(user);
        }
    }
}
