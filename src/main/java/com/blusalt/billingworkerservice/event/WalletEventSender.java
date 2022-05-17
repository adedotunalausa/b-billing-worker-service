package com.blusalt.billingworkerservice.event;

import com.blusalt.billingworkerservice.dto.response.FundWalletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletEventSender {

    private final RabbitMessagingTemplate rabbitMessagingTemplate;

    private final MappingJackson2MessageConverter messageConverter;

    @Value("${queue.fund_wallet_response}")
    private String FUND_WALLET_RESPONSE_QUEUE;

    public void pushCompletedFundWalletTransactionToQueue(FundWalletResponse fundWalletResponse) {

        Map message = new ObjectMapper().convertValue(fundWalletResponse, Map.class);
        rabbitMessagingTemplate.setMessageConverter(messageConverter);
        rabbitMessagingTemplate.convertAndSend(FUND_WALLET_RESPONSE_QUEUE, message);
        log.info("pushed completed fund wallet transaction: {} to queue", message);
    }

}
