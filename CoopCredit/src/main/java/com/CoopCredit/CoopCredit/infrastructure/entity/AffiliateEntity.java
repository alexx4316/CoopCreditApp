package com.CoopCredit.CoopCredit.infrastructure.entity;

import com.CoopCredit.CoopCredit.domain.model.AffiliateStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "affiliates")
@Getter
@Setter
public class AffiliateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id", unique = true, nullable = false)
    private String documentId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false) // Añadido
    private String lastName;

    @Column(name = "email", unique = true, nullable = false) // Añadido
    private String email;

    @Column(name = "phone") // Añadido
    private String phone;

    @Column(name = "monthly_salary", nullable = false)
    private BigDecimal monthlySalary;

    @Column(name = "date_of_birth") // Añadido
    private LocalDate dateOfBirth;

    @Column(name = "join_date", nullable = false) // Añadido
    private LocalDate joinDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AffiliateStatus status;

    @Column(name = "user_id", unique = true) // Añadido
    private String userId; // Asumiendo que userId es un String en la entidad, como en el modelo de dominio

    // Relación @OneToMany: Un afiliado puede tener muchas solicitudes
    @OneToMany(mappedBy = "affiliate", fetch = FetchType.LAZY)
    private List<CreditApplicationEntity> applications;
}
