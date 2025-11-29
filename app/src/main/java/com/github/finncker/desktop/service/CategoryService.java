package com.github.finncker.desktop.service;

import com.github.finncker.desktop.model.repository.CategoryRepository;
import com.github.finncker.desktop.model.entities.Category;

public class CategoryService {
    
    private CategoryRepository catRepo = new CategoryRepository();

    public Category create(Category cat){
        return catRepo.create(cat);
    }

    public Category read(String cat){
        Category a = catRepo.read(cat);

        if(a == null){
            //throw new exception CategoryNotFound("Categoria não encontrada " + cat)
        }

        return a;
    }

    public Category update(Category cat){
        return catRepo.update(cat);
    }

    public boolean delete(String cat){
        boolean deleted = catRepo.delete(cat);

        if(!deleted){
            //throw new exception CategoryNotFound("Categoria não encontrada " + cat)
        }

        return true;
    }
}
