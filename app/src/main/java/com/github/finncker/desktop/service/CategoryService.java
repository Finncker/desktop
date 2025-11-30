package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.exceptions.CategoryNotFoundException;

@Slf4j
public class CategoryService {

    private CategoryRepository catRepo = new CategoryRepository();

    public Category create(Category cat) {
        log.info("Criando categoria: {}", cat.getName());
        return catRepo.create(cat);
    }

    public Category read(String id) throws CategoryNotFoundException {
        log.info("Buscando categoria id = {}", id);
        Category a = catRepo.read(id);

        if (a == null) {
            log.warn("Categoria não encontrada: id = {}", id);
            throw new CategoryNotFoundException(id);
        }

        return a;
    }

    public Category update(Category cat) {
        log.info("Atualizando categoria id = {}", cat.getId());
        return catRepo.update(cat);
    }

    public boolean delete(String id) throws CategoryNotFoundException {
        log.info("Tentando deletar categoria id = {}", id);
        boolean deleted = catRepo.delete(id);

        if (!deleted) {
            log.error("Erro ao deletar categoria: id = {}", id);
            throw new CategoryNotFoundException(id);
        }

        return true;
    }
}
