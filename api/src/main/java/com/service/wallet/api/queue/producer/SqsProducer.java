package com.service.wallet.api.queue.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.wallet.api.exception.DepositException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public abstract class SqsProducer<T> {

    protected SqsClient sqsClient;

    protected SqsProducer(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

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
