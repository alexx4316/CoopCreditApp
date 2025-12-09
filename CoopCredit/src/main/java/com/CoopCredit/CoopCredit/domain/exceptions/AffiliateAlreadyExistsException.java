package com.CoopCredit.CoopCredit.domain.exceptions;

public class AffiliateAlreadyExistsException extends RuntimeException {
    public AffiliateAlreadyExistsException(String message) {
        super(message);
    }
}
