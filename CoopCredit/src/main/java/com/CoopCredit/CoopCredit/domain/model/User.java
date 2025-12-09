package com.CoopCredit.CoopCredit.domain.model;

public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String documentId;
    private UserRole role;
    private Boolean isActive;

    public User(Long id, String username, String passwordHash, String documentId, UserRole role, Boolean isActive) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.documentId = documentId;
        this.role = role;
        this.isActive = isActive;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}