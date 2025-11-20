package com.github.finncker.desktop.model;

import java.util.ArrayList;
import java.util.List;
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
@ToString(exclude = "accounts")
public class User {

    private String id;
    private String fullName;
    private String email;

    @Builder.Default
    private List<Account> accounts = new ArrayList<Account>();

    @Builder
    public User(String id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.accounts = (accounts == null ? new ArrayList<>() : accounts);
    }
}
