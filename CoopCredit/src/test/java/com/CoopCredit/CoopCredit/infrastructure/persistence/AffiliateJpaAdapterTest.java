package com.CoopCredit.CoopCredit.infrastructure.persistence;


import com.CoopCredit.CoopCredit.domain.model.Affiliate;
import com.CoopCredit.CoopCredit.domain.model.AffiliateStatus;
import com.CoopCredit.CoopCredit.infrastructure.adapter.persistence.AffiliateJpaAdapter;
import com.CoopCredit.CoopCredit.infrastructure.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Carga solo la capa JPA
@DataJpaTest
@Import({AffiliateJpaAdapter.class})
@ActiveProfiles("test")
public class AffiliateJpaAdapterTest extends TestcontainersConfiguration {

    @Autowired
    private AffiliateJpaAdapter affiliateJpaAdapter;

    @Test
    void findByDocumentId_shouldReturnAffiliate() {
        Affiliate newAffiliate = createTestAffiliate("111222333");
        affiliateJpaAdapter.save(newAffiliate);

        // Act
        Affiliate foundAffiliate = affiliateJpaAdapter.findByDocumentId("111222333").orElse(null);

        // Assert
        assertNotNull(foundAffiliate);
        assertEquals("111222333", foundAffiliate.getDocumentId());
    }

    @Test
    void findByUserUsername_shouldReturnAffiliate() {
        // Arrange
        Affiliate newAffiliate = createTestAffiliate("999999");
        newAffiliate.setUserId("testuserjpa");

        affiliateJpaAdapter.save(newAffiliate);

        // Act
        Affiliate foundAffiliate = affiliateJpaAdapter.findByUserUsername("testuserjpa").orElse(null);

        // Assert
        assertNotNull(foundAffiliate);
        assertEquals("999999", foundAffiliate.getDocumentId());
        assertEquals("testuserjpa", foundAffiliate.getUserId());
    }

    private Affiliate createTestAffiliate(String documentId) {
        Affiliate affiliate = new Affiliate();
        affiliate.setDocumentId(documentId);
        affiliate.setFirstName("Test");
        affiliate.setLastName("User");
        affiliate.setMonthlySalary(new BigDecimal("5000.00"));
        affiliate.setStatus(AffiliateStatus.ACTIVE);
        affiliate.setJoinDate(LocalDate.now().minusYears(1));
        return affiliate;
    }
}