package com.service.wallet.api.queue.producer;

import com.service.wallet.api.dto.messaging.TransactionCorrelationId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

@Slf4j
@Getter(AccessLevel.PROTECTED)
@Service
public class TransferProducer extends SqsProducer<TransactionCorrelationId> {

    protected TransferProducer(SqsClient sqsClient) {
        super(sqsClient);
    }

    @Value("${aws.sqs.transfer-queue.url}")
    private String queueUrl;
}
