package com.blusalt.billingworkerservice.service.impl;

import com.blusalt.billingworkerservice.dto.request.ChargeAccountRequest;
import com.blusalt.billingworkerservice.dto.response.FundWalletResponse;
import com.blusalt.billingworkerservice.enums.TransactionStatus;
import com.blusalt.billingworkerservice.event.WalletEventSender;
import com.blusalt.billingworkerservice.service.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final WalletEventSender walletEventSender;

    @Override
    public void processFundWalletTransaction(ChargeAccountRequest requestFromBillingService) {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            requestFromBillingService.setTransactionStatus(TransactionStatus.SUCCESS.getAlias());
            FundWalletResponse fundWalletResponse = getFundWalletResponse(requestFromBillingService);
            walletEventSender.pushCompletedFundWalletTransactionToQueue(fundWalletResponse);
        } catch (Exception exception) {
            log.error("There was an error while processing fund wallet transaction: {}", exception.getMessage());
        }
    }

    private FundWalletResponse getFundWalletResponse(ChargeAccountRequest chargeAccountRequest) {
        Double currentBalance = Double.parseDouble(chargeAccountRequest.getCurrentWalletBalance());
        Double amount = Double.parseDouble(chargeAccountRequest.getAmount());
        double newWalletBalance = currentBalance + amount;
        return new FundWalletResponse(
                chargeAccountRequest.getCustomerId(),
                chargeAccountRequest.getAmount(),
                chargeAccountRequest.getWalletId(),
                chargeAccountRequest.getCurrentWalletBalance(),
                Double.toString(newWalletBalance),
                TransactionStatus.SUCCESS.getAlias(),
                chargeAccountRequest.getTransactionId()
        );
    }
}
