package com.CoopCredit.CoopCredit.domain.model;

public enum UserRole {
    ROLE_AFILIADO,
    ROLE_ANALISTA,
    ROLE_ADMIN;

    public String getAuthorityName() {
        return this.name();
    }
}
