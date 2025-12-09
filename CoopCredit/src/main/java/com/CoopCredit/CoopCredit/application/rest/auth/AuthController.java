package com.CoopCredit.CoopCredit.application.rest.auth;

import com.CoopCredit.CoopCredit.application.rest.auth.dto.AuthResponse;
import com.CoopCredit.CoopCredit.application.rest.auth.dto.LoginRequest;
import com.CoopCredit.CoopCredit.application.rest.auth.dto.RegisterRequest;
import com.CoopCredit.CoopCredit.domain.model.Affiliate;
import com.CoopCredit.CoopCredit.domain.port.in.RegisterAffiliatePort;
import com.CoopCredit.CoopCredit.infrastructure.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterAffiliatePort registerAffiliatePort;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthController(
            RegisterAffiliatePort registerAffiliatePort,
            JwtTokenProvider jwtTokenProvider,
            AuthenticationManager authenticationManager) {

        this.registerAffiliatePort = registerAffiliatePort;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    // --- ENDPOINT DE REGISTRO ---
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerAffiliate(@Valid @RequestBody RegisterRequest request) {

        // 1. Mapear DTO a Modelo de Dominio
        Affiliate newAffiliate = mapToDomain(request);

        // 2. Invocar el Caso de Uso
        registerAffiliatePort.register(newAffiliate, request.getPassword());

        // 3. Generar Token de respuesta
        String token = jwtTokenProvider.generateToken(newAffiliate.getEmail());

        return new ResponseEntity<>(new AuthResponse(token, "Affiliate successfully registered."), HttpStatus.CREATED);
    }

    private Affiliate mapToDomain(RegisterRequest request) {
        Affiliate affiliate = new Affiliate();
        affiliate.setDocumentId(request.getDocumentId());
        affiliate.setFirstName(request.getFirstName());
        affiliate.setLastName(request.getLastName());
        affiliate.setEmail(request.getEmail());
        affiliate.setPhone(request.getPhone());
        affiliate.setMonthlySalary(request.getMonthlySalary());
        affiliate.setDateOfBirth(request.getDateOfBirth());
        return affiliate;
    }

    // --- ENDPOINT DE LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // 1. Autenticar usando el AuthenticationManager de Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Establecer la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generar Token
        String token = jwtTokenProvider.generateToken(loginRequest.getUsername());

        return ResponseEntity.ok(new AuthResponse(token, "Authentication successful."));
    }
}
