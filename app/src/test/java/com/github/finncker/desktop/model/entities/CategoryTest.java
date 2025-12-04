package com.github.finncker.desktop.model.entities;

import static org.assertj.core.api.Assertions.*;

import com.github.finncker.desktop.model.enums.CategoryType;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void shouldCreateCategory() {
        Category category = Category.builder()
                .name("Alimentação")
                .type(CategoryType.EXPENSE)
                .icon("food")
                .color("#000")
                .build();

        assertThat(category.getUuid()).isNotNull();
        assertThat(category.getBudget()).isNull();
    }
}
