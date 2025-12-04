package com.github.finncker.desktop.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@AllArgsConstructor
@ToString(exclude = { "accounts", "categories" })
public class User implements Serializable {

    private String fullName;
    private String email;

    @Builder.Default
    private List<Account> accounts = new ArrayList<>();

    @Builder.Default
    private Map<UUID, Category> categories = new HashMap<>();
}
