package com.service.assignment.wallet.queue.producer;

import com.service.assignment.wallet.dto.messaging.TransactionDepositCorrelationId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Getter(AccessLevel.PROTECTED)
@Service
@RequiredArgsConstructor
public class TransferProducer extends SqsProducer<TransactionDepositCorrelationId> {

    @Value("${aws.sqs.transfer-queue.url}")
    private String queueUrl;
}
