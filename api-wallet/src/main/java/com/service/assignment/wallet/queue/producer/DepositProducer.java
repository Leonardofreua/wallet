package com.service.assignment.wallet.queue.producer;

import lombok.AccessLevel;
import lombok.Getter;
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
@Getter(AccessLevel.PROTECTED)
@Service
@RequiredArgsConstructor
public class DepositProducer extends SqsProducer<TransactionDepositCorrelationId> {

    @Value("${aws.sqs.deposit-queue.url}")
    private String queueUrl;
}
