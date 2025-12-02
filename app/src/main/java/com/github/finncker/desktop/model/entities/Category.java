package com.github.finncker.desktop.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import com.github.finncker.desktop.model.enums.CategoryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "uuid")
@ToString
public class Category implements Serializable {

    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String name;
    private CategoryType type;
    private String icon;
    private String color;

    @Builder.Default
    private BigDecimal budget = null;
}
