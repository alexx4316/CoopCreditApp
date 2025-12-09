package com.CoopCredit.CoopCredit.infrastructure.adapter.persistence;

import com.CoopCredit.CoopCredit.domain.model.User;
import com.CoopCredit.CoopCredit.domain.port.out.UserRepositoryPort;
import com.CoopCredit.CoopCredit.infrastructure.adapter.persistence.SpringUserRepository;
import com.CoopCredit.CoopCredit.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Component
public class UserJpaAdapter implements UserRepositoryPort {

    private final SpringUserRepository repository;
    private final UserMapper mapper;

    public UserJpaAdapter(SpringUserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public User save(User user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByDocumentId(String documentId) {
        return repository.findByDocumentId(documentId)
                .map(mapper::toDomain);
    }
}