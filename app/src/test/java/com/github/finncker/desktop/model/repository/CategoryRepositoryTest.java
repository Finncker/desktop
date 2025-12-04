package com.github.finncker.desktop.model.repository;

import com.github.finncker.desktop.model.entities.Category;
import com.github.finncker.desktop.model.entities.User;
import com.github.finncker.desktop.model.enums.CategoryType;
import com.github.finncker.desktop.model.exceptions.CategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class CategoryRepositoryTest {

    private CategoryRepository repository;
    private UUID categoryUUID;

    @BeforeEach
    void setUp() {
        repository = new CategoryRepository();

        User user = User.builder()
                .fullName("Teste")
                .email("teste@email.com")
                .build();

        new UserRepository().createUser(user);

        categoryUUID = UUID.randomUUID();
    }

    @Test
    void shouldCreateAndReadCategory() throws CategoryNotFoundException {
        Category category = new Category(
                categoryUUID,
                "Alimentação",
                CategoryType.EXPENSE,
                "icon-food",
                "#FF0000",
                BigDecimal.valueOf(500)
        );

        repository.create(category);

        Category saved = repository.read(categoryUUID);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Alimentação");
        assertThat(saved.getBudget()).isEqualByComparingTo("500");
    }

    @Test
    void shouldUpdateCategory() throws CategoryNotFoundException {
        Category original = new Category(
                categoryUUID,
                "Lazer",
                CategoryType.EXPENSE,
                "icon",
                "#000",
                BigDecimal.valueOf(200)
        );

        repository.create(original);

        Category updated = new Category(
                categoryUUID,
                "Lazer Atualizado",
                CategoryType.EXPENSE,
                "icon-new",
                "#111",
                BigDecimal.valueOf(400)
        );

        repository.update(updated);

        Category result = repository.read(categoryUUID);

        assertThat(result.getName()).isEqualTo("Lazer Atualizado");
        assertThat(result.getBudget()).isEqualByComparingTo("400");
    }

    @Test
    void shouldDeleteCategory() {
        Category category = new Category(
                categoryUUID,
                "Transporte",
                CategoryType.EXPENSE,
                "icon",
                "#999",
                BigDecimal.TEN
        );

        repository.create(category);
        repository.delete(categoryUUID);

        assertThatThrownBy(() -> repository.read(categoryUUID))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void shouldReturnAllCategories() {
        assertThat(repository.getAll()).isEmpty();
    }
}
