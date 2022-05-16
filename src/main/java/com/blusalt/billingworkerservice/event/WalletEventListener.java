package com.blusalt.billingworkerservice.event;

import com.blusalt.billingworkerservice.dto.response.FundWalletResponse;
import com.blusalt.billingworkerservice.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletEventListener {

    private final TransactionService transactionService;

    @Transactional
    @RabbitListener(queues = "${queue.fund_wallet_request}")
    public void listen(Message message) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String rawMessageString = new String(message.getBody());
            FundWalletResponse requestFromBillingService = objectMapper.readValue(rawMessageString, FundWalletResponse.class);
            log.info("Fund wallet response for customer: {}", requestFromBillingService.getCustomerId());
            transactionService.processFundWalletTransaction(requestFromBillingService);
        } catch (Exception exception) {
            log.error("There was an error in fund wallet response listener: {}", exception.getMessage());
            exception.printStackTrace();
        }
    }
}
