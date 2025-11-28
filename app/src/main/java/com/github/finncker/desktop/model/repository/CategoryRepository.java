package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.Category;

public class CategoryRepository extends AbstractRepository<Category> {
    
    public CategoryRepository() {
        super("categories.dat");
    }

    @Override
    protected String getId(Category entity) {
        return entity.getId();
    }

    @Override
    protected boolean matchId(Category entity, String id) {
        return entity.getId().equals(id);
    }

    public Category readByName(String name) {
        return readAll().stream()
                        .filter(c -> c.getName().equalsIgnoreCase(name))
                        .findFirst()
                        .orElse(null);
    }
}
