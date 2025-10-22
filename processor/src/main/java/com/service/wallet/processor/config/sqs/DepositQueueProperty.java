package com.service.wallet.processor.config.sqs;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "aws.sqs.deposit-queue")
public class DepositQueueProperty implements QueueProperty {

    String url;
    Integer maxNumberOfMessages;
}
