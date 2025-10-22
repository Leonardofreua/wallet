package com.service.wallet.processor.config.sqs;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Value
@ConfigurationProperties(prefix = "aws.sqs.transfer-queue")
public class TransferQueueProperty implements QueueProperty {

    String url;
    Integer maxNumberOfMessages;
}
