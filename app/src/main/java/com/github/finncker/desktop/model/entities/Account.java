package com.github.finncker.desktop.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
@ToString(exclude = "transactions")
public class Account implements Serializable {

    @Builder.Default
    private UUID uuid = UUID.randomUUID();

    private String name;
    private BigDecimal initialBalance;

    @Builder.Default
    private List<Transaction> transactions = new ArrayList<Transaction>();
}
