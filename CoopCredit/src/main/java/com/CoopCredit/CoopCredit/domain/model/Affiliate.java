package com.CoopCredit.CoopCredit.domain.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Affiliate {
    private Long id;
    private String documentId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private BigDecimal monthlySalary;
    private LocalDate dateOfBirth;
    private LocalDate joinDate;
    private AffiliateStatus status;
    private String userId;
    private List<CreditApplication> applications;

    public Affiliate() {
    }

    public Affiliate(Long id, String documentId, String firstName, String lastName, String email, String phone, BigDecimal monthlySalary, LocalDate dateOfBirth, LocalDate joinDate, AffiliateStatus status, String userId, List<CreditApplication> applications) {
        this.id = id;
        this.documentId = documentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.monthlySalary = monthlySalary;
        this.dateOfBirth = dateOfBirth;
        this.joinDate = joinDate;
        this.status = status;
        this.userId = userId;
        this.applications = applications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(BigDecimal monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }

    public AffiliateStatus getStatus() {
        return status;
    }

    public void setStatus(AffiliateStatus status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CreditApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<CreditApplication> applications) {
        this.applications = applications;
    }
}