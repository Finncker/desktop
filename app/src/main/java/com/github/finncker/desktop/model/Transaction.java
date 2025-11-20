package com.github.finncker.desktop.model;

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
public class Transaction {
    
    private UUID id;
    private String categoryId;
    private double amount;
    private String description;

    @Builder.Default
    private LocalDate date = LocalDate.now();

    public Transaction(String id, String categoryId, double amount, String description) {
        this.id = UUID.randomUUID();
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.date = LocalDate.now();
    }
}
