package com.github.finncker.desktop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
@ToString(exclude = "transactions")
public class Account implements Serializable {
    
    private String id;
    private String name;
    private double initialBalance;

    @Builder.Default
    private List<Transaction> transactions = new ArrayList<Transaction>();

    @Builder
    public Account(String id, String name, double initialBalance) {
        this.id = id;
        this.name = name;
        this.initialBalance = initialBalance;
        this.transactions = (transactions == null ? new ArrayList<Transaction>() : transactions);
    }
}
