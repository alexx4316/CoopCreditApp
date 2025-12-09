package com.CoopCredit.CoopCredit.domain.service;

import com.CoopCredit.CoopCredit.domain.exceptions.AffiliateAlreadyExistsException;
import com.CoopCredit.CoopCredit.domain.model.Affiliate;
import com.CoopCredit.CoopCredit.domain.model.User;
import com.CoopCredit.CoopCredit.domain.model.UserRole;
import com.CoopCredit.CoopCredit.domain.port.in.RegisterAffiliatePort;
import com.CoopCredit.CoopCredit.domain.port.out.AffiliateRepositoryPort;
import com.CoopCredit.CoopCredit.domain.port.out.UserRepositoryPort;
import com.CoopCredit.CoopCredit.domain.service.validation.AffiliateValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterAffiliateService implements RegisterAffiliatePort {

    private final AffiliateRepositoryPort affiliateRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public RegisterAffiliateService(
            AffiliateRepositoryPort affiliateRepositoryPort,
            UserRepositoryPort userRepositoryPort,
            PasswordEncoder passwordEncoder) {
        this.affiliateRepositoryPort = affiliateRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Affiliate register(Affiliate newAffiliate, String password) {
        // 1. **Regla de Negocio: Documento Único**
        if (affiliateRepositoryPort.findByDocumentId(newAffiliate.getDocumentId()).isPresent()) {
            throw new AffiliateAlreadyExistsException("Affiliate with document " + newAffiliate.getDocumentId() + " already registered.");
        }

        // 2. **Reglas de Dominio/Validación Estática**
        AffiliateValidator.validateNewAffiliate(newAffiliate);

        // 3. Crear y guardar el usuario
        User newUser = new User();
        newUser.setUsername(newAffiliate.getEmail()); // Usar email como username
        newUser.setPasswordHash(passwordEncoder.encode(password));
        newUser.setDocumentId(newAffiliate.getDocumentId());
        newUser.setRole(UserRole.ROLE_AFILIADO);
        newUser.setActive(true);
        User savedUser = userRepositoryPort.save(newUser);

        // 4. Asociar usuario con afiliado y guardar
        newAffiliate.setUserId(savedUser.getId().toString());
        return affiliateRepositoryPort.save(newAffiliate);
    }
}
