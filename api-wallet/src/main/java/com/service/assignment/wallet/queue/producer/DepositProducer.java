package com.service.assignment.wallet.queue.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.assignment.wallet.dto.messaging.TransactionDepositCorrelationId;
import com.service.assignment.wallet.exception.DepositException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositProducer {

    @Value("${aws.sqs.deposit-queue-url}")
    private String depositQueueUrl;

    private final SqsClient sqsClient;

    public void send(@NotNull @Valid TransactionDepositCorrelationId transactionDepositCorrelationId) {
        try {
            var sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(depositQueueUrl)
                    .messageBody(new ObjectMapper().writeValueAsString(transactionDepositCorrelationId))
                    .build();

            sqsClient.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e) {
            throw new DepositException(e.getMessage(), e);
        }
    }
}
