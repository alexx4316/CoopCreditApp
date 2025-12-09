package com.CoopCredit.CoopCredit.domain.port.out;


import com.CoopCredit.CoopCredit.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByDocumentId(String documentId);
}