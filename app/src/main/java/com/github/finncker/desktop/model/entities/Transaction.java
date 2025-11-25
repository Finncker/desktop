package com.github.finncker.desktop.model.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Transaction implements Serializable {
    
    private UUID id;
    private String categoryId;
    private double amount;
    private String description;

    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Builder
    public Transaction(UUID id, String categoryId, double amount, String description) {
        this.id = (id == null ? UUID.randomUUID() : id);
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.date = (date == null ? LocalDate.now() : date);
    }
}
