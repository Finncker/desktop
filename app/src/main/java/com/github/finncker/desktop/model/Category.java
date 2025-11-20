package com.github.finncker.desktop.model;

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
@ToString(exclude = "transactionsCategory")
public class Category {
    
    private String id;
    private String userId;
    private String name;
    private String type;

    @Builder.Default
    private List<Transaction> transactionsCategory = new ArrayList<Transaction>();
}
