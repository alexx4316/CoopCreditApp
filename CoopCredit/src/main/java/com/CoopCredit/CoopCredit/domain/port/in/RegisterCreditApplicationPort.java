package com.CoopCredit.CoopCredit.domain.port.in;

import com.CoopCredit.CoopCredit.domain.model.CreditApplication;

public interface RegisterCreditApplicationPort {
    CreditApplication registerAndEvaluate(CreditApplication application, String username);
}
