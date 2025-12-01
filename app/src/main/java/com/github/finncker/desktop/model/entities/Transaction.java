package com.github.finncker.desktop.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

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
public class Transaction implements Serializable {

    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String categoryId;
    private BigDecimal amount;
    private String description;

    @Builder.Default
    private LocalDate date = LocalDate.now();
}
