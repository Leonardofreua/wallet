package com.service.wallet.api.queue.producer;

import com.service.wallet.api.dto.messaging.TransactionCorrelationId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;

@Slf4j
@Getter(AccessLevel.PROTECTED)
@Service
public class DepositProducer extends SqsProducer<TransactionCorrelationId> {

    protected DepositProducer(SqsClient sqsClient) {
        super(sqsClient);
    }

    @Value("${aws.sqs.deposit-queue.url}")
    private String queueUrl;
}
