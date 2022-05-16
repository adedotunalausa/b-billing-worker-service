package com.blusalt.billingworkerservice.service;

import com.blusalt.billingworkerservice.dto.response.FundWalletResponse;

public interface TransactionService {
    void processFundWalletTransaction(FundWalletResponse requestFromBillingService);
}
