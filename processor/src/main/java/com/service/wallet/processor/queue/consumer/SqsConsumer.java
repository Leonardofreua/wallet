package com.service.wallet.processor.queue.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.wallet.processor.config.sqs.QueueProperty;
import com.service.wallet.processor.exception.QueueException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsConsumer {

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    public <T> List<T> consume(@Valid @NotNull QueueProperty queueProperty, Class<T> targetType) throws QueueException {
        try {
            var receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueProperty.getUrl())
                    .maxNumberOfMessages(queueProperty.getMaxNumberOfMessages())
                    .visibilityTimeout(100) // TODO parametrizar
                    .build();

            List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
            if (CollectionUtils.isEmpty(messages)) {
                return Collections.emptyList();
            }

            List<T> objectsByTargetType = new ArrayList<>();
            for (var message : messages) {
                objectsByTargetType.add(objectMapper.readValue(message.body(), targetType));
            }

            return objectsByTargetType;
        } catch (Exception e) {
            throw new QueueException(e.getMessage(), e);
        }
    }
}
