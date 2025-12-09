package com.CoopCredit.CoopCredit.domain.port.in;

import com.CoopCredit.CoopCredit.domain.model.Affiliate;

public interface RegisterAffiliatePort {
    Affiliate register(Affiliate newAffiliate, String password);
}
