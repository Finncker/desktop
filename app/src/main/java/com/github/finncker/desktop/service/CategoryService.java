package com.github.finncker.desktop.service;

import java.util.UUID;

import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.exceptions.CategoryNotFoundException;
import com.github.finncker.desktop.model.repository.CategoryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CategoryService {

  private CategoryRepository categoryRepository = new CategoryRepository();

  public void create(Category category) {
    log.info("Criando categoria: {}", category.getName());
    categoryRepository.create(category);
  }

  public Category read(UUID uuid) throws CategoryNotFoundException {
    log.info("Buscando categoria id = {}", uuid);

    Category category = categoryRepository.read(uuid);

    return category;
  }

  public void update(Category category) throws CategoryNotFoundException {
    log.info("Atualizando categoria id = {}", category.getUuid());

    categoryRepository.update(category);
  }

  public void delete(UUID uuid) {
    log.info("Tentando deletar categoria id = {}", uuid);

    categoryRepository.delete(uuid);
  }
}
