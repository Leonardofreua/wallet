package com.service.assignment.wallet.queue.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.assignment.wallet.exception.DepositException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public abstract class SqsProducer<T> {

    private SqsClient sqsClient;

    public void send(@NotNull @Valid T content) {
        try {
            var sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(getQueueUrl())
                    .messageBody(new ObjectMapper().writeValueAsString(content))
                    .build();

            sqsClient.sendMessage(sendMessageRequest);
        } catch (JsonProcessingException e) {
            throw new DepositException(e.getMessage(), e);
        }
    }

    protected abstract String getQueueUrl();
}
