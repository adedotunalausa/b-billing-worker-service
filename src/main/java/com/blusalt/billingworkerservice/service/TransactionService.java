package com.blusalt.billingworkerservice.service;

import com.blusalt.billingworkerservice.dto.request.ChargeAccountRequest;

public interface TransactionService {
    void processFundWalletTransaction(ChargeAccountRequest requestFromBillingService);
}
